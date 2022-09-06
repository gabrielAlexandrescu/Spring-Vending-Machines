package com.VendingMachines.root.model;

import com.VendingMachines.root.commons.Utils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.UUID;
@NoArgsConstructor
@Component
public class UserDTO {
    @Id
    @Getter
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID ID;
    @Getter @Setter
    private String username;
    @Getter @Setter
    private String password;
    @Getter @Setter
    private List<MoneyDTO> userWallet;

    public UserDTO(UUID ID, String username, String password,List<MoneyDTO> userWallet) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.userWallet = userWallet;
    }

    public UserDTO(UUID ID, String username, String password) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        Utils.buildGenericWallet(this.userWallet);
    }
}
