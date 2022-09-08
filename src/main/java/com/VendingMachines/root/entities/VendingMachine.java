package com.VendingMachines.root.entities;

import com.VendingMachines.root.commons.Utils;
import com.VendingMachines.root.enums.ProductType;
import com.VendingMachines.root.model.MoneyDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
@NoArgsConstructor
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
    @JoinColumn(name = "fk_vm_id",referencedColumnName = "ID")
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
    @JoinColumn(name = "fk_vm_wallet_id", referencedColumnName = "ID")
    private List<MoneyDTO> vmWallet;
    @Getter
    @Setter
    @Transient
    private List<MoneyDTO> userInsertedMoney;
    @Getter
    @Setter
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
    private String address;


    public VendingMachine(int limitOfProductPerSlot, List<Slot> slots, int limitOfMoneyPerValue, List<ProductType> productType,String name,String address) {
        this.limitOfProductPerSlot = limitOfProductPerSlot;
        this.slots = slots;
        this.limitOfMoneyPerValue = limitOfMoneyPerValue;
        this.productType = productType;
        this.vmWallet = new ArrayList<>();
        this.userInsertedMoney = new ArrayList<>();
        this.change = new ArrayList<>();
        Utils.buildGenericWallet(vmWallet);
        Utils.buildGenericWallet(userInsertedMoney);
        Utils.buildGenericWallet(change);
        currentMoneyAddedByUser = 0;
        this.name = name;
        this.address = address;
    }
}
