package com.VendingMachines.root.Model;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import javax.persistence.*;

//TODO turn into DTO
@Component
@Entity
@Table(name="Users")
public class User {
    private final String COLUMN_ID = "ID";
    private final String COLUMN_NAME = "username";
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

    public User(String username) {
        this.username = username;
    }

    public User() {

    }
}
