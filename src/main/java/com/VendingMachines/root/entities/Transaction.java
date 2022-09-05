package com.VendingMachines.root.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Transaction {
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
    private UUID userID;
    @Getter
    private LocalDateTime timeOfTransaction;
    @Getter
    @OneToOne(cascade = CascadeType.ALL)
    private Product product;

    public Transaction(UUID userID, LocalDateTime timeOfTransaction, Product product) {
        this.userID = userID;
        this.timeOfTransaction = timeOfTransaction;
        this.product = product;
    }

    public Transaction() {
    }
}
