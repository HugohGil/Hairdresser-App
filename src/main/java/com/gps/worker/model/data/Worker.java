package com.gps.worker.model.data;

import com.gps.server.Register;
import com.gps.shared_resources.Request;
import com.gps.shared_resources.RequestsType;
import com.gps.shared_resources.Service;
import com.gps.shared_resources.User;
import javafx.util.Pair;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

public class Worker {
    private static int PORT = 8080;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Request request;
    private ResponseThread responseThread;

    public Worker(PropertyChangeSupport pcs) {
        request = new Request();
        request.setId(-1);
        try {
            Socket socket = new Socket("localhost",PORT);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        responseThread = new ResponseThread(this,pcs);
        responseThread.start();
    }

    public void rLogin(Pair<String, String> info) {
        writeToSocket(RequestsType.LOGIN_WORKER, info);
    }

    public void rRegister(Register register) {
        writeToSocket(RequestsType.REGISTER, register);
    }
    public boolean rUpdateServices() {
        writeToSocket(RequestsType.UPDATE_SERVICES, null);
        //serviceList = (ArrayList<Service>) readFromSocket();
        return true;
    }

    public void rAddService(Service service) {
        writeToSocket(RequestsType.ADD_SERVICE, service);
    }

    public void rEditService(Service service, int serviceId) {
        writeToSocket(RequestsType.EDIT_SERVICE, new Pair<>(service,serviceId));
        //return (Boolean) readFromSocket();
    }

    public void rRemoveService(int serviceId) {
        writeToSocket(RequestsType.REMOVE_SERVICE, serviceId);
    }

    public void rUpdateCalender(Date date) {
        writeToSocket(RequestsType.UPDATE_CALENDAR_WORKER, new Pair<>(new User(request.getId(), ""), date));
    }

    public void disconnect() {
        writeToSocket(RequestsType.DISCONNECT,null);
    }

    private void writeToSocket(RequestsType requestType, Object info) {
        try {
            request.setType(requestType);
            request.setRequest(info);
            oos.writeUnshared(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object readFromSocket() {
        try {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Object getResponse() {
        return responseThread.getResponse();
    }

    public Request getRequest() {
        return request;
    }

    public void rUpdateWeeklyCalender(User worker, Date date) {writeToSocket(RequestsType.UPDATE_WEEK,new Pair<>(worker,date));}

    public void getWorkers() {
        writeToSocket(RequestsType.GET_WORKERS,null);
    }

    public void getTypeOfServices() {
        writeToSocket(RequestsType.GET_TYPE_OF_SERVICE,null);
    }

    public void rUpdateUserWeeklyCalendar(User user, Date date) {writeToSocket(RequestsType.UPDATE_USER_WEEK,new Pair<>(user,date));}

    public void rConcludeService(int serviceId) {
        writeToSocket(RequestsType.CONCLUDE_SERVICE,serviceId);
    }
}
