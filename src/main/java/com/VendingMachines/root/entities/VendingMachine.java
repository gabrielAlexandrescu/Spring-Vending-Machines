package com.VendingMachines.root.entities;

import com.VendingMachines.root.commons.Utils;
import com.VendingMachines.root.enums.ProductType;
import com.VendingMachines.root.exceptions.VendingMachineException;
import com.VendingMachines.root.model.MoneyDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Entity
@Table(name = "VendingMachines")
public class VendingMachine {
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
    private int limitOfProductPerSlot;
    @OneToMany
    @Getter
    @Setter
    private List<Slot> slots;
    @Getter
    @Setter
    private int limitOfMoneyPerValue;
    @Getter
    @Setter
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<ProductType> productType;
    @Getter
    @Setter
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_vm_id", referencedColumnName = "ID")
    private List<MoneyDTO> vm_wallet;
    @Getter
    @Transient
    private List<MoneyDTO> userInsertedMoney;
    @Getter
    @Transient
    private List<MoneyDTO> change;
    @Getter
    @Transient
    @Setter
    private User loggedUser;
    @Getter
    @Setter
    @Transient
    private int currentMoneyAddedByUser;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String adress;

    public VendingMachine(int limitOfProductPerSlot, List<Slot> slots, int limitOfMoneyPerValue, List<ProductType> productType,String name,String adress) {
        this.limitOfProductPerSlot = limitOfProductPerSlot;
        this.slots = slots;
        this.limitOfMoneyPerValue = limitOfMoneyPerValue;
        this.productType = productType;
        this.vm_wallet = new ArrayList<>();
        this.userInsertedMoney = new ArrayList<>();
        this.change = new ArrayList<>();
        Utils.buildGenericWallet(vm_wallet);
        Utils.buildGenericWallet(userInsertedMoney);
        Utils.buildGenericWallet(change);
        currentMoneyAddedByUser = 0;
        this.name = name;
        this.adress = adress;
    }

    public VendingMachine() {

    }


}
