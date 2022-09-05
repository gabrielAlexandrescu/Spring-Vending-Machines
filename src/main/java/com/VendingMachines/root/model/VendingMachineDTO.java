package com.VendingMachines.root.model;

import com.VendingMachines.root.entities.Slot;
import com.VendingMachines.root.enums.ProductType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.UUID;

@Component
public class VendingMachineDTO {
    @Id
    @Getter
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID ID;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String adress;
    @Getter @Setter
    private int limitOfProductPerSlot;
    @Getter @Setter
    private int limitOfMoneyPerValue;
    @Getter @Setter
    private List<ProductType> productType;
    @Getter @Setter
    private List<Slot> slots;

}
