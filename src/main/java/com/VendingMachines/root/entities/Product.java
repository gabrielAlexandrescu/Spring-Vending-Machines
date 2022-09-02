package com.VendingMachines.root.entities;

import com.VendingMachines.root.enums.ProductType;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name="Products")
public class Product {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @Getter
    private String name;
    @Getter
    private ProductType productType;
}
