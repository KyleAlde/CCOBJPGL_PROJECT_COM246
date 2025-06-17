package com.example;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TransactionHistoryController {
    ObservableList<Transaction> transacList = FXCollections.observableArrayList();

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, Transaction> transactionCol;

    @FXML
    private TableColumn<Transaction, String> typeCol;

    @FXML
    private TableColumn<Transaction, Float> amountCol;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private User currentUser;
    private BlippiCard blippiCard;
    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.blippiCard = user.getBlippi();
        System.out.println(blippiCard.getCardNumber());
        loadData();
    }

    @FXML
    public void initialize() {
        System.out.println("transactionCol initialized? " + (transactionCol != null));
    }

    private void loadData() {
        transacList.clear();
        transactionTable.setVisible(true);
        if (transactionCol == null) {
            System.out.println("transactionCol is null! Check your FXML.");
        }
        System.out.println("Loading data...");

        try {
            File transacFile = new File("transactions.txt");
            
            if(transacFile.exists()) {
                Scanner filescanner = new Scanner(transacFile);

                while(filescanner.hasNextLine()) {
                    String data = filescanner.nextLine();

                    String id_from_file = data.split(";")[0];
                    String cardnum_from_file = data.split(";")[1];
                    String type_from_file = data.split(";")[2];
                    String amount_from_file = data.split(";")[3];
                    String date_from_file = data.split(";")[4];

                    if(cardnum_from_file.equals(blippiCard.getCardNumber())) {
                        Transaction latestTransac = new Transaction(id_from_file, cardnum_from_file, type_from_file, Float.valueOf(amount_from_file), date_from_file);
                        transacList.add(latestTransac);
                        System.out.println("Transaction added");
                    } else {
                        System.out.println("Transaction not added");
                    }
                }
                filescanner.close();

            } else {
                System.out.println("File can't be read");
            }

            // Set up the TableView columns
            transactionCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
            transactionCol.setCellFactory(col -> new TableCell<Transaction, Transaction>() {
                @Override
                protected void updateItem(Transaction tx, boolean empty) {
                    super.updateItem(tx, empty);
                    if (empty || tx == null) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        VBox vbox = new VBox(2);
                        Label mainLabel = new Label(tx.getType());            // uses String from SimpleStringProperty
                        Label dateLabel = new Label(tx.getDate());            // uses String from SimpleStringProperty
                        dateLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");
                        vbox.getChildren().addAll(mainLabel, dateLabel);
                        setGraphic(vbox);
                        setText(null);
                    }
                }
            });
            // For the type column with conditional text based on transaction type
            typeCol.setCellValueFactory(cellData -> {
                Transaction tx = cellData.getValue();
                String typeStr = tx.getType();
                String display = ("blippi Buy Load".equals(typeStr) || "blippi Rewards".equals(typeStr)) ? "-" : "LRT/MRT Service";
                return new SimpleStringProperty(display);
            });
            amountCol.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
            // Optionally, add a custom cell factory to format or style the float
            amountCol.setCellFactory(col -> new TableCell<Transaction, Float>() {
                @Override
                protected void updateItem(Float amount, boolean empty) {
                    super.updateItem(amount, empty);
                    if (empty || amount == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        // Format like currency + nearby style
                        String formatted = (amount >= 0 ? "+" : "") + String.format("%.2f", amount);
                        setText(formatted);
                        setStyle(amount >= 0 ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
                    }
                }
            });

            System.out.println("Transactions count: " + transacList.size());
            transactionTable.setItems(transacList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backButtonHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Home.fxml"));
            root = loader.load();

            HomeController homeController = loader.getController();
            homeController.setCurrentUser(currentUser);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

