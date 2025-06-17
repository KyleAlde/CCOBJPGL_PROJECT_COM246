package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.List;
import java.util.Random;

public class LoadRewardDetailsController {
    @FXML
    private Label usernamelabel;

    @FXML
    private Label accnum;

    @FXML
    private Label amount;

    @FXML
    private Label points;

    @FXML
    TextField amountTextField;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private User currentUser;
    private BlippiCard blippiCard;
    private float currentPoints;
    private float reqPoints;
    private float loadAmount;
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.blippiCard = user.getBlippi();
        this.currentPoints = blippiCard.getRewards();
        String username = currentUser.getUsername();
        usernamelabel.setText(username);
        accnum.setText(blippiCard.getCardNumber());
    }

    public void setAmount(float loadAmount, float reqPoints) {
        this.reqPoints = reqPoints;
        this.loadAmount = loadAmount;
        String formattedNumber = NumberFormat.getInstance().format(reqPoints);
        points.setText(formattedNumber);
        String strAmount = String.format("₱%.2f", loadAmount);
        amount.setText(strAmount);
    }

    @FXML
    public boolean confirmButtonHandler(ActionEvent event) throws IOException {
        // Deduct reward points from selected blippi card
        float newPoints = currentPoints - reqPoints;
        blippiCard.setRewards(newPoints);
        String strPoints = String.format("%.0f", newPoints);

        float currentBalance = blippiCard.getBalance();
        float newBalance = currentBalance + loadAmount;
        blippiCard.setBalance(newBalance);
        String strBalance = String.format("%.0f", newBalance);

        String targetCardNum = blippiCard.getCardNumber();
        List<String> updatedLines = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader("blippicards.txt"))) {
            String line;
            while((line = reader.readLine()) != null) {
                if(!line.trim().isEmpty()) {
                    String[] parts = line.split(";");

                    if(parts.length == 6 && parts[0].equals(targetCardNum)) {
                        updatedLines.add(parts[0] + ";" + strBalance + ";" + parts[2] + ";" + parts[3] + ";" + strPoints + ";" + parts[5]);
                    } else {
                        updatedLines.add(line);
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter("blippicards.txt"))) {
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

        //Add new transaction to ArrayList
        newTransaction(blippiCard, loadAmount);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("Redeem Successful!");
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
        } catch (Exception e) {
            System.out.println("There is an error");
            return false;
        }
    }

    @FXML
    public void backButtonHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/RedeemLoad.fxml"));
            root = loader.load();

            RedeemLoadController redeem = loader.getController();
            redeem.setCurrentUser(currentUser);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch(IOException e) {
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

        Transaction transac = new Transaction(transacId, blippi.getCardNumber(), "blippi Rewards", loadAmount, formattedDate);
        blippi.addTransaction(transac);

        try {
            BufferedWriter myWriter = new BufferedWriter(new FileWriter("transactions.txt", true));

            myWriter.newLine();
            myWriter.write(transacId + ";" + blippi.getCardNumber() + ";" + "blippi Rewards" + ";" + loadAmount + ";" + formattedDate);
            myWriter.close();

        } catch(IOException e) {
            System.out.println("An error occurred.");
        }
    }
}
