package com.example.foodshare.classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Request implements Serializable {
    private static int lastId = 0;
    public int Id;
    public int OfferId;
    public int UserId;
    public List<Product> Products;
    public boolean Accepted;
    public LocalDate CreationDate;

    public Request() {
        Id = ++lastId;
        Accepted = false;
        CreationDate = LocalDate.now();
    }

    public Request(int offerId, int userId, List<Product> products) {
        Id = ++lastId;
        OfferId = offerId;
        UserId = userId;
        Products = products;
        Accepted = false;
        CreationDate = LocalDate.now();
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getOfferId() {
        return OfferId;
    }

    public void setOfferId(int offerId) {
        OfferId = offerId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public List<Product> getProducts() {
        return Products;
    }

    public void setProducts(List<Product> products) {
        Products = products;
    }

    public boolean isAccepted() {
        return Accepted;
    }

    public void setAccepted(boolean accepted) {
        Accepted = accepted;
    }

    public LocalDate getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        CreationDate = creationDate;
    }
}
