package com.gps.server;

import java.io.Serializable;
import java.util.Date;

public class Register implements Serializable {
    private String name;
    private String email;
    private String password;
    private Date date;

    private String validationCode;

    public Register(String name, String email, String password, Date date) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.date = date;
        this.validationCode = null;
    }

    public String getValidationCode() {
        return validationCode;
    }

    public Register(String name, String email, String password, Date date, String validationCode) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.date = date;
        this.validationCode = validationCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
