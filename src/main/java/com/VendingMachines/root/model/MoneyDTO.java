package com.VendingMachines.root.model;

import com.VendingMachines.root.enums.MoneyType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.UUID;


@Component
@NoArgsConstructor
@Table(name="wallets")
@Entity
public class MoneyDTO {
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private MoneyType moneyType;
    @Getter
    @Setter
    private int amount;
    @Id
    @Getter
    @Setter
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID ID;

    public MoneyDTO(MoneyType moneyType, int amount) {
        this.moneyType = moneyType;
        this.amount = amount;
    }

}
