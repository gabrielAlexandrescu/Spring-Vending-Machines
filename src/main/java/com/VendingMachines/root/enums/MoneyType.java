package com.VendingMachines.root.enums;

import javax.persistence.Embeddable;

@Embeddable
public enum MoneyType {
    FIFTY_EUROS(5000),
    TWENTY_EUROS(2000),
    TEN_EUROS(1000),
    FIVE_EUROS(500),
    TWO_EUROS(200),
    ONE_EURO(100),
    FIFTY_CENTS(50),
    TWENTY_CENTS(20),
    TEN_CENTS(10),
    FIVE_CENTS(5),
    ONE_CENT(1);

    MoneyType(int value) {
    }

    MoneyType() {

    }
}
