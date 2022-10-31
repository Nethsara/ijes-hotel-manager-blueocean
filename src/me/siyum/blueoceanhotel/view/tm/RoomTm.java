package me.siyum.blueoceanhotel.view.tm;

import javafx.scene.control.Button;

public class RoomTm {
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

    public String isMaintenance() {
        return maintenance;
    }

    public void setMaintenance(String maintenance) {
        this.maintenance = maintenance;
    }

    public String isReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    private String id;
    private String type;
    private double price;
    private String maintenance;
    private String reserved;
    private Button btn;

    public RoomTm() {
    }

    public RoomTm(String id, String type, double price, String maintenance, String reserved, Button btn) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.maintenance = maintenance;
        this.reserved = reserved;
        this.btn = btn;
    }
}
