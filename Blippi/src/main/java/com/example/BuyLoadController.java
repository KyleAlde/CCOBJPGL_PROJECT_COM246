package com.example;

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BuyLoadController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private User currentUser;
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    public void backButtonController(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Home.fxml"));
        root = loader.load();

        //Set current user for the blippi card set-up and add card to home page
        HomeController homeController = loader.getController();
        homeController.setCurrentUser(currentUser);
        homeController.addCard(searchCard(currentUser));
        homeController.initializeUsername();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public BlippiCard searchCard(User user) {
        String userId = user.getId();

        File blippiFile = new File("blippicards.txt");

        if(blippiFile.exists()) {
            Scanner filescanner;

            try {
                filescanner = new Scanner(blippiFile);

                //Look for the card with the matching userId
                while(filescanner.hasNextLine()) {
                    String data = filescanner.nextLine();

                    String cardnum_from_file = data.split(";")[0];
                    String balance_from_file = data.split(";")[1];
                    String label_from_file = data.split(";")[2];
                    String exp_from_file = data.split(";")[3];
                    String id_from_file = data.split(";")[4];

                    if(id_from_file.equals(userId)) {
                        //Create BlippiCard object from database info
                        BlippiCard blippi = new BlippiCard(cardnum_from_file, Integer.valueOf(balance_from_file), label_from_file, exp_from_file, id_from_file);
                        return blippi;
                    }
                }
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("blippicards.txt file cannot be read");
        }
        return null;
    }
    
}
