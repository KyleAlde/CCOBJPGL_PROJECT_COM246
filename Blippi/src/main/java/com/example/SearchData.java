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

                    if(id_from_file.equals(phoneOrEmail)) {
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
                    String id_from_file = data.split(";")[4];

                    if(id_from_file.equals(userId)) {
                        //Create BlippiCard object from database info
                        ArrayList<Transaction> transacList = new ArrayList<>();
                        BlippiCard blippi = new BlippiCard(cardnum_from_file, Integer.valueOf(balance_from_file), label_from_file, exp_from_file, id_from_file, transacList);
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
                    String id_from_file = data.split(";")[4];

                    if(cardnum_from_file.equals(cardNum)) {
                        //Create BlippiCard object from database info
                        ArrayList<Transaction> transacList = new ArrayList<>();
                        BlippiCard blippi = new BlippiCard(cardnum_from_file, Integer.valueOf(balance_from_file), label_from_file, exp_from_file, id_from_file, transacList);
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
}
