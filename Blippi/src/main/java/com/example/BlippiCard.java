package com.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;

public class BlippiCard {
    private SimpleStringProperty cardNumber;
    private SimpleFloatProperty balance;
    private SimpleStringProperty label;
    private SimpleStringProperty expDate;
    private String userId;

    public BlippiCard(String cnumber, float bal, String lbl, String exp, String id) {
        this.cardNumber = new SimpleStringProperty(cnumber);
        this.balance = new SimpleFloatProperty(bal);
        this.label = new SimpleStringProperty(lbl);
        this.expDate = new SimpleStringProperty(exp);
        this.userId = id;
    }

    public String getCardNumber() { return cardNumber.get(); }
    public float getBalance() {return balance.get(); }
    public String getLabel() { return label.get(); }
    public String getExpDate() { return expDate.get(); }
    public String getUserId() { return userId; }

    public void setBalance(float newBalance) { this.balance.set(newBalance); }
}
