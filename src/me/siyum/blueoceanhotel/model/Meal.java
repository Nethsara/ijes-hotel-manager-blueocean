package me.siyum.blueoceanhotel.model;

public class Meal {
    private String id;
    private double price;
    private String type;

    public Meal() {
    }

    public Meal(String id, double price, String type) {
        this.id = id;
        this.price = price;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
