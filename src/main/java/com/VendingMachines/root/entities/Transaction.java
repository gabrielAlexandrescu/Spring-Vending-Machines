package com.VendingMachines.root.entities;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @Getter
    private Long userID;
    @Getter
    private LocalDateTime timeOfTransaction;
    @Getter
    @OneToOne(mappedBy = "Products")
    private Product product;

}
