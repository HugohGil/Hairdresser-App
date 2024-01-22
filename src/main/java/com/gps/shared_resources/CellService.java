package com.gps.shared_resources;

import com.gps.shared_resources.utils.CellType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CellService implements Serializable {
    private int idService;
    private int idUser;
    private String nameWorker;
    private int idWorker;
    private boolean isMine;
    private int isConcluded;
    private String horaInicio;
    private String horaFim;
    private List<String> typeOfServices;
    int duration;
    private CellType isDayOff;

    public CellService(int idService, int idUser, String nameWorker, int idWorker, boolean isMine, String horaInicio, String horaFim, List<String> typeOfServices,int duration,int isConcluded) {
        this.idService = idService;
        this.idUser = idUser;
        this.nameWorker = nameWorker;
        this.idWorker = idWorker;
        this.isMine = isMine;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.typeOfServices = typeOfServices;
        this.duration = duration;
        this.isConcluded = isConcluded;
    }

    public CellService(int idService, CellType isDayOff) {
        this.idService = idService;
        this.isDayOff = isDayOff;
    }

    public int getIdService() {
        return idService;
    }

    public int getIsConcluded(){return isConcluded;}

    public CellType getIsDayOff() {
        return isDayOff;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getNameWorker() {
        return nameWorker;
    }

    public int getIdWorker() {
        return idWorker;
    }

    public boolean isMine() {
        return isMine;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public List<String> getTypeOfServices() {
        return typeOfServices;
    }

    public int getDuration() {
        return duration;
    }
}
