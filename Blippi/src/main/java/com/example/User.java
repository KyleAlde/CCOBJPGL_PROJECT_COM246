package com.example;

import javafx.beans.property.SimpleStringProperty;

public class User {
    private SimpleStringProperty username;
    private SimpleStringProperty password;
    private SimpleStringProperty phoneOrEmail;

    public User(String uname, String pword, String phone) {
        this.username = new SimpleStringProperty(uname);
        this.password = new SimpleStringProperty(pword);
        this.phoneOrEmail = new SimpleStringProperty(phone);
    }

    public String getUsername() { return username.get(); }
    public String getPassword() { return password.get(); }
    public String getContact() { return phoneOrEmail.get(); }
}