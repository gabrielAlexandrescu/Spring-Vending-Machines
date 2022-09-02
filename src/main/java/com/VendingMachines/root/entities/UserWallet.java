package com.VendingMachines.root.entities;

import com.VendingMachines.root.enums.MoneyType;
import lombok.Getter;

import javax.persistence.*;

@Embeddable
public class UserWallet {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @Getter
    @Embedded
    private MoneyType moneyType;
}
