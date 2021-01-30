package com.sky.tickety.model;

public class Discount {
    public String provider, id, name;
    public Double percentage;

    public Discount(String provider, String id, String name, Double percentage) {
        this.provider = provider;
        this.id = id;
        this.name = name;
        this.percentage = percentage;
    }
}
