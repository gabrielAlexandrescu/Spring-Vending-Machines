package com.VendingMachines.root.enums;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Arrays;

public enum ProductType {
    COKE,
    BAKE_ROLLS,
    SPRITE,
    DUREX,
    KENT,
    WINSTON,
    FANTA,
    SEVEN_UP,
    BREAD,
    SNACK_DAY,
    CHIPS,
    PEANUTS;


    @Override
    public String toString() {
        return Arrays.toString(ProductType.values());
    }
}
