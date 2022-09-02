package com.VendingMachines.root.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "Users")
public class User {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String role;
    @Embedded
    @Getter
    private UserWallet userWallet;
    @Getter
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.userWallet = new UserWallet();
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
