package com.example.fitty;

public class Order {
    String orderBy;
    String direction;
    String title;

    public Order(String orderBy, String direction, String title) {
        this.orderBy = orderBy;
        this.direction = direction;
        this.title = title;
    }
}
