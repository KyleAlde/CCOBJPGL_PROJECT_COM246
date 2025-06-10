package com.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class BlippiCard {
    private SimpleStringProperty cardNumber;
    private SimpleIntegerProperty balance;
    private SimpleStringProperty label;
    private String userId;

    public BlippiCard(String cnumber, int bal, String lbl, String id) {
        this.cardNumber = new SimpleStringProperty(cnumber);
        this.balance = new SimpleIntegerProperty(bal);
        this.label = new SimpleStringProperty(lbl);
        this.userId = id;
    }

    public String getCardNumber() { return cardNumber.get(); }
    public int getBalance() {return balance.get(); }
    public String getLabel() { return label.get(); }
    public String getUserId() { return userId; }
}
