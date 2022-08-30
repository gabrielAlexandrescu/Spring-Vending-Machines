package com.VendingMachines.root.enums;

public enum ProductType {
    COLA(150,"cola"),
    CHIPS(120,"chips"),
    MASK(350,"mask"),
    KENT(250,"kent"),
    WATER(180,"water");
    private int price;
    private String code;

    ProductType(int price, String code) {
        this.price = price;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
