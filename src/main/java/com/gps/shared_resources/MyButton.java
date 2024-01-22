package com.gps.shared_resources;


import javafx.scene.control.Button;

import java.util.Date;

public class MyButton {
    private Date date;
    private String style;
    private final Button button;

    public MyButton(Button button,Date date) {
        this.date = date;
        this.button = button;
    }

    public Date getDate() {
        return date;
    }

    public Button getButton() {
        return button;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
