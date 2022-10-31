package me.siyum.blueoceanhotel.model;

public class Room {
    private String id;
    private String type;
    private double price;
    private boolean maintenance;
    private boolean reserved;

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

    public boolean isMaintenance() {
        return maintenance;
    }

    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public Room() {
    }

    public Room(String id, String type, double price, boolean maintenance, boolean reserved) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.maintenance = maintenance;
        this.reserved = reserved;
    }
}
