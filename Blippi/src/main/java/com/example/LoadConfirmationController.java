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

public class LoadConfirmationController {

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
    private boolean continueButtonHandler(ActionEvent event) throws IOException {
        float currentBalance = selectedBlippi.getBalance();
        float newBalance = currentBalance + loadAmount;
        selectedBlippi.setBalance(newBalance);

        String strBalance = String.format("%.0f", newBalance);

        if(selectedBlippi.getCardNumber().equals(blippiCard.getCardNumber())) {
            System.out.println("blippi objects match");
            blippiCard.setBalance(newBalance);
            newTransaction(blippiCard, newBalance);
        } else {
            newTransaction(selectedBlippi, newBalance);
        }

        String targetCardNum = selectedBlippi.getCardNumber();
        List<String> updatedLines = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader("blippicards.txt"))) {
            String line;
            while((line = reader.readLine()) != null) {
                if(!line.trim().isEmpty()) {
                    String[] parts = line.split(";");

                    if(parts.length == 5 && parts[0].equals(targetCardNum)) {
                        updatedLines.add(parts[0] + ";" + strBalance + ";" + parts[2] + ";" + parts[3] + ";" + parts[4]);
                    } else {
                        updatedLines.add(line);
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("blippicards.txt"))) {
            for (int i = 0; i < updatedLines.size(); i++) {
                writer.write(updatedLines.get(i));
                if (i < updatedLines.size() - 1) {
                    writer.newLine();
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("Load Successful!");
        String content = String.format("₱%.2f has been loaded to blippi card %s", loadAmount, targetCardNum);
        alert.setContentText(content);
        alert.showAndWait();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Home.fxml"));
            root = loader.load();

            HomeController homeController = loader.getController();
            homeController.setCurrentUser(currentUser);
            

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            return true;
        } catch(IOException e) {
            System.out.println("There is an error");
            return false;
        }
        
    }

    @FXML
    public void backButtonHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/BuyLoadCard.fxml"));
        root = loader.load();

        //Set current user for the blippi card set-up and add card to home page
        BuyLoadCardController buyLoadCardController = loader.getController();
        buyLoadCardController.setCurrentUser(currentUser);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
