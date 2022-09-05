package com.VendingMachines.root.entities;

import com.VendingMachines.root.commons.Utils;
import com.VendingMachines.root.enums.MoneyType;
import com.VendingMachines.root.model.MoneyDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "Users")
public class User {
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
    private String username;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String role;
    @OneToMany(cascade = CascadeType.ALL)
    @Getter
    @Setter
    @JoinColumn(name="fk_user_id",referencedColumnName = "ID")
    private List<MoneyDTO> userWallet;
    @Getter
    @Setter
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="fk_transactions_id",referencedColumnName = "ID")
    private List<Transaction> transactions;

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.userWallet = new ArrayList<>();
        Utils.buildGenericWallet(userWallet);
        this.transactions = new ArrayList<>();
        if (isAdmin) {
            this.role = "admin";
        } else {
            this.role = "user";
        }
    }


    public User() {

    }


}
