package com.VendingMachines.root.commons;

import com.VendingMachines.root.entities.Product;
import com.VendingMachines.root.enums.MoneyType;
import com.VendingMachines.root.model.MoneyDTO;
import com.VendingMachines.root.model.ProductDTO;

import java.util.List;

public class Utils {
    public static List<MoneyDTO> buildGenericWallet(List<MoneyDTO> wallet){
        wallet.add(new MoneyDTO(MoneyType.FIFTY_EUROS,0));
        wallet.add(new MoneyDTO(MoneyType.TWENTY_EUROS,0));
        wallet.add(new MoneyDTO(MoneyType.TEN_EUROS,0));
        wallet.add(new MoneyDTO(MoneyType.FIVE_EUROS,0));
        wallet.add(new MoneyDTO(MoneyType.TWO_EUROS,0));
        wallet.add(new MoneyDTO(MoneyType.ONE_EURO,0));
        wallet.add(new MoneyDTO(MoneyType.FIFTY_CENTS,0));
        wallet.add(new MoneyDTO(MoneyType.TWENTY_CENTS,0));
        wallet.add(new MoneyDTO(MoneyType.TEN_CENTS,0));
        wallet.add(new MoneyDTO(MoneyType.FIVE_CENTS,0));
        wallet.add(new MoneyDTO(MoneyType.ONE_CENT,0));
        return wallet;
    }
    public static Product convertProductDTOToEntity(ProductDTO DTO){
        return new Product(DTO.getID(),DTO.getName(),DTO.getProductType());
    }
}
