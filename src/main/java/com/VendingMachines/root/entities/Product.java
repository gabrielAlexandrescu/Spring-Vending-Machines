package com.VendingMachines.root.entities;

import com.VendingMachines.root.enums.ProductType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="Products")
public class Product {
    @Getter
    @Id
    @Setter
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID ID;
    @Getter
    @Setter
    @Column(unique = true)
    private String name;
    @Getter
    @Setter
    private ProductType productType;

    public Product(String name, ProductType productType) {
        this.name = name;
        this.productType = productType;
    }

    public Product() {

    }
}
