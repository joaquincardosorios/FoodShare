package com.example.foodshare.classes;

import java.io.Serializable;
import java.util.List;

public class Request implements Serializable {
    private static int lastId = 0;
    public int Id;
    public int OfferId;
    public int UserId;
    public List<Product> Products;

    public Request() {
    }

    public Request(int id, int offerId, int userId, List<Product> products) {
        Id = id;
        OfferId = offerId;
        UserId = userId;
        Products = products;
    }

    
}
