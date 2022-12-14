package com.VendingMachines.root.model;

import com.VendingMachines.root.entities.Slot;
import com.VendingMachines.root.enums.ProductType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.UUID;

@Component
@NoArgsConstructor
public class VendingMachineDTO {

    @Getter @Setter
    private String name;
    @Getter @Setter
    private String address;
    @Getter @Setter
    private int limitOfProductPerSlot;
    @Getter @Setter
    private int limitOfMoneyPerValue;
    @Getter @Setter
    private List<ProductType> productType;
    @Getter @Setter
    private List<Slot> slots;
    @Getter @Setter
    private UUID ID;

    public VendingMachineDTO(String name, String address, int limitOfProductPerSlot, int limitOfMoneyPerValue, List<ProductType> productType, List<Slot> slots) {

        this.name = name;
        this.address = address;
        this.limitOfProductPerSlot = limitOfProductPerSlot;
        this.limitOfMoneyPerValue = limitOfMoneyPerValue;
        this.productType = productType;
        this.slots = slots;
    }


}
