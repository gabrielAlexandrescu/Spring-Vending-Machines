package com.VendingMachines.root.entities;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "VendingMachines")
public class VendingMachine {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @Getter
    private int limitOfProductPerSlot;
    @ManyToMany
    @Getter
    private List<Slot> slots;
    @Getter
    private int limitOfMoneyPerValue;
}
