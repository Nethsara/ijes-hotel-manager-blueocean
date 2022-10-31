package me.siyum.blueoceanhotel.view.tm;

import javafx.scene.control.Button;

public class MealTm {
    private String id;
    private String type;
    private double price;
    private Button btn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public MealTm() {
    }

    public MealTm(String id, String type, double price, Button btn) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.btn = btn;
    }
}
