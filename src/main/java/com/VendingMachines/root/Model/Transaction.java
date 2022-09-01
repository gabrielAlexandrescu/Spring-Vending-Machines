package com.VendingMachines.root.Model;

import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
//TODO Turn into DTO
@Component
@Entity
@Table(name="Transactions")
public class Transaction {

    public final String COLUMN_USER_ID ="userID";
    private final String COLUMN_NAME = "productName";
    private final String COLUMN_PRICE = "productPrice";
    private final String COLUMN_TIME_OF_PURCHASE = "timeOfPurchase";
    private final String COLUMN_ID = "ID";

    @Column(name=COLUMN_USER_ID)
    @Getter
    private final Long userID;
    @Getter
    @Column(name = COLUMN_NAME)
    private final String productName;
    @Getter
    @Column(name = COLUMN_PRICE)
    private final int price;
    @Getter
    @Column(name =COLUMN_TIME_OF_PURCHASE)
    private final LocalDateTime timeOfPurchase;
    @Column(name= COLUMN_ID)
    @Id
    @SequenceGenerator(
            name ="transaction_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "transaction_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long Id;

    public Transaction(Long userID,Product product) {
        this.userID = userID;
        this.productName = product.getName();
        this.price = product.getPrice();
        this.timeOfPurchase = LocalDateTime.now();
    }

    public Transaction() {
        productName = null;
        timeOfPurchase = null;
        price = 0;
        userID = 0L;
    }
}
