package com.VendingMachines.root.model;

import com.VendingMachines.root.commons.Utils;
import com.VendingMachines.root.entities.Transaction;
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
    @Getter @Setter
    private String username;
    @Getter @Setter
    private String password;
    @Getter @Setter
    private List<MoneyDTO> userWallet;
    @Getter @Setter
    private UUID ID;
    @Getter @Setter
    private String role;


    public UserDTO(String username, String password,boolean isAdmin) {
        this.username = username;
        this.password = password;
        if(isAdmin){
            role = "admin";
        }
        else{
            role = "user";
        }
    }

    public UserDTO(String username, String password) {

        this.username = username;
        this.password = password;
        Utils.buildGenericWallet(this.userWallet);
    }
}
