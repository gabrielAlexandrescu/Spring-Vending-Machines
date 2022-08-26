package com.VendingMachines.root.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
//TODO Turn into DTO
@Component
public class Product {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private int price;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Product() {
        name = null;
        price = 0;
    }

    @Override
    public String toString() {
        return name+" : "+(double)price/100;
    }
}
