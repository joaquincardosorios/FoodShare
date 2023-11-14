package com.example.foodshare.classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Offer implements Serializable {
    private static int lastId = 0;
    public int Id;
    public int UserId;
    public String Description;
    public LocalDate CreationDate;
    public double Lat;
    public double Long;
    public Product[] Products;

    public boolean isClosed() {
        return Closed;
    }

    public void setClosed(boolean closed) {
        Closed = closed;
    }

    public boolean Closed;

    public Offer() {
        this.Closed = false;
    }

    public Offer(int userId, String description, LocalDate creationDate, double lat, double aLong, Product[] products) {
        Id = ++lastId;
        UserId = userId;
        Description = description;
        CreationDate = creationDate;
        Lat = lat;
        Long = aLong;
        Products = products;
        Closed = false;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public LocalDate getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        CreationDate = creationDate;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLong() {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }

    public Product[] getProducts() {
        return Products;
    }

    public void setProducts(Product[] products) {
        Products = products;
    }
}
