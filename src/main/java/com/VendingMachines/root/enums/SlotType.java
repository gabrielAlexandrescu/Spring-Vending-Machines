package com.VendingMachines.root.enums;

public enum SlotType {
    A1("A1"),
    A2("A2"),
    A3("A3"),
    A4("A4"),
    B1("B1"),
    B2("B2"),
    B3("B3"),
    B4("B4"),
    C1("C1"),
    C2("C2"),
    C3("C3"),
    C4("C4");
    private String code;

    SlotType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
