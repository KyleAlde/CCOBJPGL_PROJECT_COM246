package com.example;

import javafx.beans.property.SimpleStringProperty;
import java.util.ArrayList;
import javafx.beans.property.SimpleFloatProperty;

public class BlippiCard {
    private SimpleStringProperty cardNumber;
    private SimpleFloatProperty balance;
    private SimpleStringProperty label;
    private SimpleStringProperty expDate;
    private SimpleFloatProperty rewardPoints;
    private String userId;
    private ArrayList<Transaction> transacList = new ArrayList<>(); 

    public BlippiCard(String cnumber, float bal, String lbl, String exp, int rewards, String id, ArrayList<Transaction> transactions) {
        this.cardNumber = new SimpleStringProperty(cnumber);
        this.balance = new SimpleFloatProperty(bal);
        this.label = new SimpleStringProperty(lbl);
        this.expDate = new SimpleStringProperty(exp);
        this.rewardPoints = new SimpleFloatProperty(rewards);
        this.userId = id;
        this.transacList = transactions != null ? transactions : new ArrayList<>();
    }

    public String getCardNumber() { return cardNumber.get(); }
    public float getBalance() {return balance.get(); }
    public String getLabel() { return label.get(); }
    public String getExpDate() { return expDate.get(); }
    public String getUserId() { return this.userId; }
    public float getRewards() { return rewardPoints.get(); }
    public ArrayList<Transaction> getTransac() { return transacList; }

    public void setBalance(float newBalance) { this.balance.set(newBalance); }
    public void setRewards(float newPoints) { this.rewardPoints.set(newPoints); }
    public void addTransaction(Transaction transaction) { transacList.add(transaction); }
}
