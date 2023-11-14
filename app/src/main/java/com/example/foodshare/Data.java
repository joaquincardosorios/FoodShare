package com.example.foodshare;

import com.example.foodshare.classes.Offer;
import com.example.foodshare.classes.Product;
import com.example.foodshare.classes.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Data {
    private static LocalDate formatearFecha(String dateStr){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate newDate = LocalDate.parse(dateStr, formatter);
        return newDate;
    }
    public static List<Offer> getOffersList(){
        List<Offer> offerList = new ArrayList<>();
        offerList.add(new Offer(
                2,
                "Viajo mañana y no los quiero tirar",
                formatearFecha("17-11-2023"),
                -36.827378301186194, -73.05701209543153,
                new Product[]{
                    new Product("Tomates", "1 kg", formatearFecha("18-11-2023")),
                    new Product("Lechugas", "1 un", formatearFecha("18-11-2023"))
                }
            )
        );
        offerList.add(new Offer(
                        3,
                        "Dono huevos de campos a quien los necesite",
                        formatearFecha("15-11-2023"),
                    -36.83535413311839, -73.05913157690149,
                        new Product[]{
                                new Product("Huevos", "1 docena", formatearFecha("22-11-2023"))
                        }
                )
        );


        return offerList;
    }

    public static  List<User> getUsersList(){
        List<User> userList = new ArrayList<>();
        userList.add(new User(1,"Joaquin Rios","joaquin.rios@virginiogomez.cl", "111111"));
        userList.add(new User(2,"Octavio Gutierrez","octavio_gutierrez@virginiogomez.cl", "111111"));
        userList.add(new User(3,"Monica Vergara","monica_vergara@virginiogomez.cl", "111111"));
        userList.add(new User(4,"Kristell Molina","kristell_molina@virginiogomez.cl", "111111"));
        return userList;
    }

    public static User getUser(){
        User user = new User(1,"Joaquin Rios","joaquin.rios@virginiogomez.cl", "111111");
        return user;
    }
}
