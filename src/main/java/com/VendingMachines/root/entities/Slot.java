package com.VendingMachines.root.entities;
import com.sun.istack.NotNull;
import lombok.Getter;

import javax.persistence.*;

@Entity
public class Slot {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @Getter
    private String code;
    @Getter
    @OneToOne(mappedBy = "Products")
    private Product product;
    @Getter
    private int amount;
    @Getter
    private Long vendingMachineID;


}
