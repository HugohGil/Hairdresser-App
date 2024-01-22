package com.gps.shared_resources;

import com.gps.client.model.data.Client;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Service implements Serializable {

    private int id;
    private int idClient;
    private int idWorker;
    private Date dateStart;
    private Date dateEnd;
    private List<TypeService> type;
    private String clientName;


    public Service(int id, int idWorker, Date dateStart, Date dateEnd, List<TypeService> type) {
        this.id = id;
        this.idWorker = idWorker;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.type = type;
        this.clientName = null;
    }
    public Service(int id, int idWorker, Date dateStart, Date dateEnd, List<TypeService> type, String clientName) {
        this.id = id;
        this.idWorker = idWorker;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.type = type;
        this.clientName = clientName;
    }
    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public int getId() {
        return id;
    }
    public String getClientName() {
        return clientName;
    }

    public int getIdClient() {
        return idClient;
    }

    public int getIdWorker() {
        return idWorker;
    }

    public List<TypeService> getType() {
        return type;
    }
}
