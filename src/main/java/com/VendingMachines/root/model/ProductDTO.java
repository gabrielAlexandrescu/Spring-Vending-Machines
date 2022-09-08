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
    @Getter
    private String name;
    @Getter
    private ProductType productType;
    @Getter @Setter
    private UUID ID;

    public ProductDTO(String name, ProductType productType) {
        this.name = name;
        this.productType = productType;
    }

}
