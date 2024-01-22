package com.gps.server.model.data;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientAcception extends Thread {
    private static final int PORT = 8080;
    private final List<ThreadClient> clients = new ArrayList<>();
    private DBHandler db;
    private boolean isConnected = true;

    private PropertyChangeSupport pcs;
    public ClientAcception(DBHandler db, PropertyChangeSupport pcs) {
        this.db = db;
        this.pcs = pcs;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while(isConnected) {
                Socket socket = serverSocket.accept();
                ThreadClient c = new ThreadClient(socket,db,clients,pcs);
                c.start();
                clients.add(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<ThreadClient> getClients() {
        return clients;
    }
}
