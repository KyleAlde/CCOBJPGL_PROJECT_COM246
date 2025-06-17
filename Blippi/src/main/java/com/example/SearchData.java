package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SearchData {
    public User searchUser(String phoneOrEmail) {
        File accountsFile = new File("accounts.txt");

        if(accountsFile.exists()) {
            Scanner filescanner;

            try {
                filescanner = new Scanner(accountsFile);

                //Look for the card with the matching userId
                while(filescanner.hasNextLine()) {
                    String data = filescanner.nextLine();

                    String name_from_file = data.split(";")[0];
                    String pword_from_file = data.split(";")[1];
                    String contact_from_file = data.split(";")[2];
                    String id_from_file = data.split(";")[3];

                    if(contact_from_file.equals(phoneOrEmail)) {
                        //Create User object from database info
                        User user = new User(name_from_file, pword_from_file, contact_from_file, id_from_file, searchCard(id_from_file));
                        filescanner.close();
                        return user;
                    }
                }
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("accounts.txt file cannot be read");
        }
        return null;
    }

    public BlippiCard searchCard(String userId) {
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
                    String points_from_file = data.split(";")[4];
                    String id_from_file = data.split(";")[5];
                    System.out.println(id_from_file);

                    if(id_from_file.equals(userId)) {
                        //Create BlippiCard object from database info
                        ArrayList<Transaction> transacList = searchTransactions(cardnum_from_file);
                        BlippiCard blippi = new BlippiCard(cardnum_from_file, Float.valueOf(balance_from_file), label_from_file, exp_from_file, Integer.valueOf(points_from_file), id_from_file, transacList);
                        System.out.println("Blippi search result: " + cardnum_from_file);
                        filescanner.close();
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

    public BlippiCard searchByCardNum(String cardNum) {
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
                    String points_from_file = data.split(";")[4];
                    String id_from_file = data.split(";")[5];

                    if(cardnum_from_file.equals(cardNum)) {
                        //Create BlippiCard object from database info
                        ArrayList<Transaction> transacList = searchTransactions(cardNum);
                        BlippiCard blippi = new BlippiCard(cardnum_from_file, Float.valueOf(balance_from_file), label_from_file, exp_from_file, Integer.valueOf(points_from_file), id_from_file, transacList);
                        filescanner.close();
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

    public ArrayList<Transaction> searchTransactions(String cardNum) {
        File transacFile = new File("transactions.txt");

        if(transacFile.exists()) {
            Scanner fileScanner;

            try {
                ArrayList<Transaction> transacList = new ArrayList<>();
                fileScanner = new Scanner(transacFile);

                //Look for all transactions with matching card number
                while(fileScanner.hasNextLine()) {
                    String data = fileScanner.nextLine();

                    String id_from_file = data.split(";")[0];
                    String cardnum_from_file = data.split(";")[1];
                    String type_from_file = data.split(";")[2];
                    String amount_from_file = data.split(";")[3];
                    String date_from_file = data.split(";")[4];

                    if(cardnum_from_file.equals(cardNum)) {
                        Transaction transac = new Transaction(id_from_file, cardnum_from_file, type_from_file, Float.valueOf(amount_from_file), date_from_file);
                        transacList.add(transac);
                    }
                }

                fileScanner.close();
                return transacList;
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            }
            
        } else {
            System.out.println("transactions.txt file cannot be read");
        }
        return null;
    }
}
