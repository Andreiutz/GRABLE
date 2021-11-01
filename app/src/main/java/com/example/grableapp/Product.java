package com.example.grableapp;

public class Product {

    private String name;
    private double price;
    private String description;
    private double weight;
    private String id;

    public Product(String id, String name, double price,double weight,String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.description=description;

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
    public void setDescription(String description){ this.description=description;}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
    public double getWeight(){return  weight;}
}
