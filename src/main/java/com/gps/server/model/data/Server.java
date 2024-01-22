package com.gps.server.model.data;

import com.gps.shared_resources.User;
import com.gps.shared_resources.responses.AddServiceResponse;
import com.gps.shared_resources.responses.EditServiceResponse;
import com.gps.shared_resources.responses.RemoveServiceResponse;
import com.gps.shared_resources.responses.UpdateCalendarAssinc;
import com.gps.shared_resources.utils.CellType;
import javafx.util.Pair;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Server {
    private DBHandler db;
    private ClientAcception clientAcception;

    private PropertyChangeSupport pcs;
    public Server(PropertyChangeSupport pcs) {
        try {
            this.pcs = pcs;
            db = new DBHandler();
            clientAcception = new ClientAcception(db,pcs);
            clientAcception.start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getWorkers() {
        return db.getWorkers();
    }

    public Pair<Boolean,String> removeWorker(User user) {
        return db.removeWorker(user);
    }

    public Map<String, Pair<Integer, Integer>> getStatistic(User user) {
        return db.getStatistic(user);
    }

    public Pair<Boolean, String> toggleDay(User user, Date date) {
        Pair<Boolean,String> pair =  db.handleDay(user,date);

        for(var client : clientAcception.getClients()) {
            try {
                client.getOos().writeObject(new AddServiceResponse(true,""));
                client.getOos().writeObject(new EditServiceResponse(true,""));
                client.getOos().reset();
                client.getOos().writeObject(new UpdateCalendarAssinc(true));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return pair;
    }

    public List<CellType> updateCalendar(User user, Date date) {
        return db.updateCalendarManager(user,date);
    }
}
