package com.VendingMachines.root.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Component
@NoArgsConstructor
@Table(name="slots")
public class Slot {
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
    private String code;
    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name ="fk_product_id",referencedColumnName = "ID")
    private Product product;
    @Getter
    @Setter
    private int amount;
    @Getter
    @Setter
    private int price;
    public Slot(String code,int price) {
        product = null;
        this.code = code;
        this.price = price;
    }

}
