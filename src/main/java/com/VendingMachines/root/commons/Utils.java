package com.VendingMachines.root.commons;

import com.VendingMachines.root.entities.Product;
import com.VendingMachines.root.entities.Slot;
import com.VendingMachines.root.entities.User;
import com.VendingMachines.root.entities.VendingMachine;
import com.VendingMachines.root.enums.MoneyType;
import com.VendingMachines.root.exceptions.VendingMachineException;
import com.VendingMachines.root.model.MoneyDTO;
import com.VendingMachines.root.model.ProductDTO;
import com.VendingMachines.root.model.UserDTO;
import com.VendingMachines.root.model.VendingMachineDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if(DTO == null){
            return null;
        }
        return new Product(DTO.getID(),DTO.getName(),DTO.getProductType());
    }
    public static List<MoneyDTO> buildWallet(int fiftyEuros,int twentyEuros, int tenEuros,int fiveEuros, int twoEuros, int oneEuro , int fiftyCents,int twentyCents, int tenCents,int fiveCents,int oneCent){
        List<MoneyDTO> wallet = new ArrayList<>();
        wallet.add(new MoneyDTO(MoneyType.FIFTY_EUROS,fiftyEuros));
        wallet.add(new MoneyDTO(MoneyType.TWENTY_EUROS,twentyEuros));
        wallet.add(new MoneyDTO(MoneyType.TEN_EUROS,tenEuros));
        wallet.add(new MoneyDTO(MoneyType.FIVE_EUROS,fiveEuros));
        wallet.add(new MoneyDTO(MoneyType.TWO_EUROS,twoEuros));
        wallet.add(new MoneyDTO(MoneyType.ONE_EURO,oneEuro));
        wallet.add(new MoneyDTO(MoneyType.FIFTY_CENTS,fiftyCents));
        wallet.add(new MoneyDTO(MoneyType.TWENTY_CENTS,twentyCents));
        wallet.add(new MoneyDTO(MoneyType.TEN_CENTS,tenCents));
        wallet.add(new MoneyDTO(MoneyType.FIVE_CENTS,fiveCents));
        wallet.add(new MoneyDTO(MoneyType.ONE_CENT,oneCent));
        return wallet;
    }
    public static List<Slot> buildSlotList(String firstSlot,String lastSlot) throws VendingMachineException {
        if (firstSlot.charAt(0) > lastSlot.charAt(0)) {
            throw new VendingMachineException("The first slot letter has to be lower/equal to the last one");
        } else if (Integer.parseInt(firstSlot.substring(1)) > Integer.parseInt(lastSlot.substring(1))) {
            throw new VendingMachineException("The first slot number cannot be higher than the last one's!");
        } else {
            List <Slot> slotsForMachine = new ArrayList<>();
            for (char row = firstSlot.charAt(0); row <= lastSlot.charAt(0); row++) {
                for (int i = 1; i <= Integer.parseInt(lastSlot.substring(1)); i++) {
                    String newCode = row + String.valueOf(i);
                    Slot slot = new Slot(newCode, 0);
                    slotsForMachine.add(slot);
                }
            }
            return slotsForMachine;
        }
    }
    public static UserDTO convertUserToDTO(User user) {
        boolean isAdmin = Objects.equals(user.getRole(), "admin");
        UserDTO userDTO = new UserDTO(user.getUsername(), user.getPassword(),isAdmin);
        userDTO.setID(user.getID());
        userDTO.setUserWallet(user.getUserWallet());
        return userDTO;
    }
    public static ProductDTO convertProductToDTO(Product product){
        ProductDTO productDTO = new ProductDTO(product.getName(),product.getProductType());
        productDTO.setID(product.getID());
        return productDTO;
    }
    public static VendingMachineDTO convertVendingMachineToDTO(VendingMachine vendingMachine){
        VendingMachineDTO vendingMachineDTO = new VendingMachineDTO(vendingMachine.getName(),vendingMachine.getAddress(),vendingMachine.getLimitOfProductPerSlot(),vendingMachine.getLimitOfMoneyPerValue(),vendingMachine.getProductType(),vendingMachine.getSlots());
        vendingMachineDTO.setID(vendingMachine.getID());
        return  vendingMachineDTO;
    }
}
