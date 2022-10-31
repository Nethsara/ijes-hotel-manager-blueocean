package me.siyum.blueoceanhotel.view.tm;

import javafx.scene.control.Button;

public class BookTm {
    private String id;
    private String customerID;
    private String date;
    private String time;
    private String roomID;
    private String mealID;
    private double price;
    private Button btn;

    public BookTm() {
    }

    public BookTm(String id, String customerID, String date, String time, String roomID, String mealID, double price, Button btn) {
        this.id = id;
        this.customerID = customerID;
        this.date = date;
        this.time = time;
        this.roomID = roomID;
        this.mealID = mealID;
        this.price = price;
        this.btn = btn;
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

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }


}
