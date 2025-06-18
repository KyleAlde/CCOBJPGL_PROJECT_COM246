package com.example;

import java.io.IOException;

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

public class QRConfirmationController {

    @FXML
    private Label operatorname;

    @FXML
    private Label routeqr;

    @FXML
    private Label startlabel;

    @FXML
    private Label destinationlabel;

    @FXML
    private Label amount;

    @FXML
    private Label totalAmount;

    @FXML
    private Label paymentMethodLabel;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private float loadAmount;
    private float newAmount;
    private User currentUser;
    private BlippiCard blippiCard;
    private String startTerminal;
    private String destinationTerminal;
    private String operator;
    private String route;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.blippiCard = user.getBlippi();
    }

    public void setAmount(float inputAmount, BlippiCard blippi, String paymentMethod) {
        this.loadAmount = inputAmount;
        paymentMethodLabel.setText("Credit/Debit Card");
        float processFee = inputAmount * 0.03f;
        newAmount = loadAmount + processFee;
        String strAmount = String.format("₱%.2f", loadAmount);
        amount.setText(strAmount);
        String strTotal = String.format("₱%.2f", newAmount);
        totalAmount.setText(strTotal);
    }

    public void setRoute(String operator, String route, String startTerminal, String destinationTerminal) {
        this.operator = operator;
        this.route = route;
        this.startTerminal = startTerminal;
        this.destinationTerminal = destinationTerminal;

        operatorname.setText(operator);
        routeqr.setText(route);
        startlabel.setText(startTerminal);
        destinationlabel.setText(destinationTerminal);
    }

    @FXML
    public void confirmButtonHandler(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("Purchase Successful!");
        String content = String.format("You have successfully purchased a bus ticket for %s", route);
        alert.setContentText(content);
        alert.showAndWait();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/HomeV2.fxml"));
            root = loader.load();

            HomeController homeController = loader.getController();
            homeController.setCurrentUser(currentUser);
            
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch(IOException e) {
            System.out.println("There is an error");
        }
    }

    @FXML
    public void backButtonHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/QrPaymentV2.fxml"));
            root = loader.load();

            //Set current user for the blippi card set-up and add card to home page
            QrPaymentController qrPaymentController = loader.getController();
            qrPaymentController.setCurrentUser(currentUser);
            qrPaymentController.setAmount(loadAmount);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
