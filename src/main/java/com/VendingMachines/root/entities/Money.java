package com.VendingMachines.root.entities;

import javax.persistence.*;

@Entity
@Table(name = "money")
public class Money {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String moneyTypeCode;
    private int moneyStock;

    public Money() {
    }

    public Money(String moneyTypeCode) {
        this.moneyTypeCode = moneyTypeCode;
    }

    public int getMoneyStock() {
        return moneyStock;
    }

    public void setMoneyStock(int moneyStock) {
        this.moneyStock = moneyStock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMoneyTypeCode() {
        return moneyTypeCode;
    }

    public void setMoneyTypeCode(String moneyTypeCode) {
        this.moneyTypeCode = moneyTypeCode;
    }
}
