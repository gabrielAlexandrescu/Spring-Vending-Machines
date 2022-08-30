package com.VendingMachines.root.enums;

public enum MoneyType {
    FIFTY_EURO(5000,"fifty_euro"),
    TWENTY_EURO(2000,"twenty_euro"),
    TEN_EURO(1000,"ten_euro");
    private int currency;
    private String code;

    MoneyType(int currency, String code) {
        this.currency = currency;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }
}
