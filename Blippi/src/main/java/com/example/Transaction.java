package com.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;

public class Transaction {
    private SimpleStringProperty id;
    private SimpleStringProperty cardNumber;
    private SimpleStringProperty type;
    private SimpleFloatProperty amount;
    private SimpleStringProperty date;

    public Transaction(String transacId, String cardnum, String transacType, float transacAmount, String transacDate) {
        this.id = new SimpleStringProperty(transacId);
        this.cardNumber = new SimpleStringProperty(cardnum);
        this.type = new SimpleStringProperty(transacType);
        this.amount = new SimpleFloatProperty(transacAmount);
        this.date = new SimpleStringProperty(transacDate);
    }

    public String getId() { return id.get(); }
    public String getCardnum() { return cardNumber.get(); }
    public String getType() { return type.get(); }
    public float getAmount() { return amount.get(); }
    public String getDate() { return date.get(); }
}