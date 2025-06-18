package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PaymentMethodController {
    @FXML
    private Label usernamelabel;

    @FXML
    private Label amount;

    @FXML
    private Label accnum;

    @FXML
    private Label totalAmount;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private float loadAmount;
    private float newAmount;
    private User currentUser;
    private BlippiCard blippiCard;
    private BlippiCard selectedBlippi;

    public void setAmount(float inputAmount, BlippiCard blippi) {
        this.loadAmount = inputAmount;
        this.selectedBlippi = blippi;
        float processFee = inputAmount * 0.03f;
        newAmount = loadAmount + processFee;
        accnum.setText(selectedBlippi.getCardNumber());
        String strAmount = String.format("₱%.2f", loadAmount);
        amount.setText(strAmount);
        String strTotal = String.format("₱%.2f", newAmount);
        totalAmount.setText(strTotal);
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.blippiCard = user.getBlippi();
        String username = currentUser.getUsername();
        usernamelabel.setText(username);
    }

    @FXML
    private void continueButtonHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/PaymentV2.fxml"));
            root = loader.load();

            PaymentController paymentController = loader.getController();
            paymentController.setCurrentUser(currentUser);
            paymentController.setAmount(loadAmount, selectedBlippi);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backButtonHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/BuyLoadCardV2.fxml"));
            root = loader.load();

            //Set current user for the blippi card set-up and add card to home page
            BuyLoadCardController buyLoadCardController = loader.getController();
            buyLoadCardController.setCurrentUser(currentUser);
            buyLoadCardController.setAmount(loadAmount);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newTransaction(BlippiCard blippi, float loadAmount) {
        //Generate transaction id number
        Random r = new Random();
        int r1 = r.nextInt(1000000);
        String transacId = Integer.toString(r1);

        //Generate date today
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a");
        String formattedDate = now.format(format);

        Transaction transac = new Transaction(transacId, blippi.getCardNumber(), "blippi Buy Load", loadAmount, formattedDate);
        blippi.addTransaction(transac);

        try {
            BufferedWriter myWriter = new BufferedWriter(new FileWriter("transactions.txt", true));

            myWriter.newLine();
            myWriter.write(transacId + ";" + blippi.getCardNumber() + ";" + "blippi Buy Load" + ";" + loadAmount + ";" + formattedDate);
            myWriter.close();

        } catch(IOException e) {
            System.out.println("An error occurred.");
        }
    }
}
