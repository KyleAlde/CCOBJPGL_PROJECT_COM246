package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ForgotPasswordController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private AnchorPane container;

    @FXML
    private TextField numberField;

    @FXML
    private TextField passwordField1;

    @FXML
    private TextField passwordField2;

    @FXML
    public boolean newPassButtonHandler(ActionEvent event) throws IOException {
        String contact = numberField.getText();
        String pass1 = passwordField1.getText();
        String pass2 = passwordField2.getText();

        SearchData search = new SearchData();
        if(search.searchUser(contact) == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("Incorrect email or phone number");
            alert.showAndWait();
            return false;
        }

        if(!pass1.equals(pass2)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText("Input not valid");
            alert.setContentText("Password and confirmation do not match. Please try again.");
            alert.showAndWait();
            return false;
        }
        String targetUser = search.searchUser(contact).getId();
        List<String> updatedLines = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            while((line = reader.readLine()) != null) {
                if(!line.trim().isEmpty()) {
                    String[] parts = line.split(";");

                    if(parts.length == 4 && parts[3].equals(targetUser)) {
                        updatedLines.add(parts[0] + ";" + pass1 + ";" + parts[2] + ";" + parts[3]);
                        System.out.println(parts[0] + ";" + pass1 + ";" + parts[2] + ";" + parts[3]);
                    } else {
                        updatedLines.add(line);
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("accounts.txt"))) {
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
        alert.setHeaderText("Reset Successful");
        alert.setContentText("Your password has been updated");
        alert.showAndWait();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/HomeV2.fxml"));
            root = loader.load();

            HomeController homeController = loader.getController();
            homeController.setCurrentUser(search.searchUser(contact));
            
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
    public void loginLinkHandler(ActionEvent event) throws IOException{
        try {
            // Load Login.fxml when login link is clicked
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/LoginV2.fxml"));
            root = loader.load();

            // Load stage and scene
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
