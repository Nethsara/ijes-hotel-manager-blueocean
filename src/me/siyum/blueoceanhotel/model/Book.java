package me.siyum.blueoceanhotel.model;

public class Book {
    private String id;
    private String customerID;
    private String date;
    private String time;
    private String roomID;
    private String mealID;
    private double price;

    public Book() {
    }

    public Book(String id, String customerID, String date, String time, String roomID, String mealID, double price) {
        this.id = id;
        this.customerID = customerID;
        this.date = date;
        this.time = time;
        this.roomID = roomID;
        this.mealID = mealID;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getMealID() {
        return mealID;
    }

    public void setMealID(String mealID) {
        this.mealID = mealID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
