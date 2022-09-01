package com.VendingMachines.root.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

//TODO Turn into DTO
public class Admin {
    private final String COLUMN_ID = "ID";
    private final String COLUMN_NAME = "username";
    private final String COLUMN_PASSWORD = "password";
    @Column(name=COLUMN_ID)
    @Id
    @SequenceGenerator(
            name ="user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "user_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long Id;
    @Column(name= COLUMN_NAME)
    @Getter
    @Setter
    private String username;
    @Getter @Setter
    @Column(name=COLUMN_PASSWORD)
    private String password;

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
