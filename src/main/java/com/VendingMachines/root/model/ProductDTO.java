package com.VendingMachines.root.model;

import com.VendingMachines.root.enums.ProductType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;
@NoArgsConstructor
@Data
public class ProductDTO {
    @Id
    @Getter
    @Setter
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID ID;
    @Getter
    private String name;
    @Getter
    private ProductType productType;

    public ProductDTO(String name, ProductType productType) {
        this.name = name;
        this.productType = productType;
    }

    public ProductDTO(UUID ID, String name, ProductType productType) {
        this.ID = ID;
        this.name = name;
        this.productType = productType;
    }

}
