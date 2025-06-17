package com.example;

import javafx.beans.property.SimpleStringProperty;

public class User {
    private SimpleStringProperty username;
    private SimpleStringProperty password;
    private SimpleStringProperty phoneOrEmail;
    private String id;
    private BlippiCard blippi;

    public User(String uname, String pword, String phone, String id, BlippiCard card) {
        this.username = new SimpleStringProperty(uname);
        this.password = new SimpleStringProperty(pword);
        this.phoneOrEmail = new SimpleStringProperty(phone);
        this.id = id;
        this.blippi = card;
    }

    public String getUsername() { return username.get(); }
    public String getPassword() { return password.get(); }
    public String getContact() { return phoneOrEmail.get(); }
    public String getId() { return this.id; }
    public BlippiCard getBlippi() { return this.blippi; }

    public void setBlippi(BlippiCard blippiCard) { this.blippi = blippiCard; }
}