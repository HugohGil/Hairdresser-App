package com.gps.server.model.data;

import com.gps.client.model.ModelManager;
import com.gps.shared_resources.Request;
import com.gps.shared_resources.RequestsType;
import com.gps.shared_resources.responses.*;
import javafx.application.Platform;

import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ThreadClient extends Thread {
    private ObjectInputStream ois;
    private DBHandler db;
    private ObjectOutputStream oos;

    private List<ThreadClient> clients;
    private Request request;

    private PropertyChangeSupport pcs;
    public ThreadClient(Socket socket, DBHandler db, List<ThreadClient> clients, PropertyChangeSupport pcs) {
        System.out.println("New client joined!");
        try {
            this.pcs = pcs;
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            this.clients = clients;
            this.db = db;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                request = (Request) ois.readObject();
                System.out.println("Executing " + request.getType());
                switch (request.getType()) {
                    case LOGIN -> db.login(request, oos);
                    case LOGIN_WORKER -> db.loginWorker(request, oos);
                    case EDIT -> {
                    }
                    case DISCONNECT -> db.disconnect(request, oos);
                    case REGISTER -> db.register(request,oos);
                    case ADD_SERVICE -> db.addService(request, oos);
                    case EDIT_SERVICE -> db.editService(request, oos);
                    case REMOVE_SERVICE -> db.removeService(request, oos);
                    case UPDATE_SERVICES -> db.getServiceList(oos);
                    case UPDATE_CALENDAR -> db.updateCalendar(request,oos);
                    case UPDATE_WEEK -> db.updateWeaklyCalendar(request,oos);
                    case GET_WORKERS -> db.getWorkers(oos);
                    case GET_TYPE_OF_SERVICE -> db.getTypeOfServices(oos);
                    case UPDATE_USER_WEEK -> db.updateUserWeeklyCalendar(request, oos);
                    case UPDATE_CALENDAR_WORKER -> db.updateCalendarWorker(request, oos);
                    case CONCLUDE_SERVICE -> db.concludeService(request,oos);
                }

                for (var client : clients) {
                    if (client != this) {
                        switch (request.getType()) {
                            case ADD_SERVICE ->  client.getOos().writeObject(new AddServiceResponse(true, "Success"));
                            case EDIT_SERVICE -> client.getOos().writeObject(new EditServiceResponse(true, "Success"));
                            case REMOVE_SERVICE -> client.getOos().writeObject(new RemoveServiceResponse(true, "Success"));
                        }
                        client.getOos().reset();
                        client.getOos().writeObject(new UpdateCalendarAssinc(true));
                    }
                    Platform.runLater(() -> pcs.firePropertyChange(ModelManager.PROP_STATE,null,null));
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                clients.remove(this);
                oos = null;
                if (!(request.getType() == RequestsType.DISCONNECT)) {
                    request.setType(RequestsType.DISCONNECT);
                    db.disconnect(request, null);
                }
                break;
            }
        }
    }

    public ObjectOutputStream getOos() {
        return oos;
    }
}
