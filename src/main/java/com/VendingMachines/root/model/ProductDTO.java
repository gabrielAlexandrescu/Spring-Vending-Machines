package com.VendingMachines.root.model;

import com.VendingMachines.root.enums.ProductType;
import lombok.Data;
import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class ProductDTO {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @Getter
    private String name;
    @Getter
    private ProductType productType;

    public ProductDTO(String name, ProductType productType) {
        this.name = name;
        this.productType = productType;
    }

}
