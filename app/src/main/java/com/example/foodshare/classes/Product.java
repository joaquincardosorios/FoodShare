package com.example.foodshare.classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Product implements Serializable {

    public String Name;
    public String Quantity;
    public LocalDate ExpirationDate;
    public Product() {
    }

    public Product(String name, String quantity, LocalDate expirationDate) {
        this.Name = name;
        this.Quantity = quantity;
        this.ExpirationDate = expirationDate;

    }



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public LocalDate getExpirationDate() {
        return ExpirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        ExpirationDate = expirationDate;
    }

}
