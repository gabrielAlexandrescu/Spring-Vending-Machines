import com.VendingMachines.root.commons.Utils;
import com.VendingMachines.root.entities.Slot;
import com.VendingMachines.root.entities.User;
import com.VendingMachines.root.enums.MoneyType;
import com.VendingMachines.root.enums.ProductType;
import com.VendingMachines.root.exceptions.ProductException;
import com.VendingMachines.root.exceptions.UserException;
import com.VendingMachines.root.exceptions.VendingMachineException;
import com.VendingMachines.root.model.MoneyDTO;
import com.VendingMachines.root.model.ProductDTO;
import com.VendingMachines.root.model.VendingMachineDTO;
import com.VendingMachines.root.services.ProductService;
import com.VendingMachines.root.services.VendingMachineService;
import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = SpringVendingMachinesTests.class)
@ExtendWith(MockitoExtension.class)
class SpringVendingMachinesTests {
    private final List<Slot> sodasVmSlots;

    {
        try {
            sodasVmSlots = Utils.buildSlotList("A1", "C3");
        } catch (VendingMachineException e) {
            throw new RuntimeException(e);
        }
    }

    private final List<Slot> snacksVmSlots;

    {
        try {
            snacksVmSlots = Utils.buildSlotList("A1", "C3");
        } catch (VendingMachineException e) {
            throw new RuntimeException(e);
        }
    }

    private final List<Slot> utilitiesVmSlots;

    {
        try {
            utilitiesVmSlots = Utils.buildSlotList("A1", "A4");
        } catch (VendingMachineException e) {
            throw new RuntimeException(e);
        }
    }

    private final List<ProductType> sodaTypes = List.of(ProductType.COKE, ProductType.SPRITE);
    private final List<ProductType> snackTypes = List.of(ProductType.PEANUTS, ProductType.CHIPS, ProductType.SKITTLES);
    private final List<ProductType> utilitiesTypes = List.of(ProductType.CIGAR, ProductType.GUM, ProductType.FPP2_MASK, ProductType.MAGNET);

    private final VendingMachineDTO sodasVendingMachine = new VendingMachineDTO("sodas", "Strada Valea Luncului 10", 5, 100, sodaTypes, sodasVmSlots);
    private final VendingMachineDTO snacksVendingMachine = new VendingMachineDTO("snacks", "Bulevardul Muncii 23", 5, 100, snackTypes, snacksVmSlots);
    private final VendingMachineDTO utilitiesVendingMachine = new VendingMachineDTO("utilities", "Aleea Bravului 69", 5, 100, utilitiesTypes, utilitiesVmSlots);
    @Autowired
    @Inje
    private ProductService productService;
    @Autowired
    private VendingMachineService vendingMachineService;




    // 0 = FIFTY_EUROS
    // 1 = TWENTY_EUROS
    // 2 = TEN_EUROS
    // 3 = FIVE_EUROS
    // 4 = TWO_EUROS
    // 5 = ONE_EURO
    // 6 = FIFTY_CENTS
    // 7 = TWENTY_CENTS
    // 8 = TEN_CENTS
    // 9 = FIVE_CENTS
    // 10 = ONE_CENTS


    @Test
    void customTest() throws ProductException, UserException, VendingMachineException {
        List<MoneyDTO> money = Utils.buildWallet(10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10);
        User u1 = new User("User1", "password1", false);
        User u2 = new User("User2", "password2", false);
        User u3 = new User("User3", "password3", false);
        User admin = new User("admin", "admin", true);
        List<MoneyDTO> walletOne = Utils.buildWallet(0, 0, 0, 0, 0, 2, 1, 1, 0, 0, 0);
        List<MoneyDTO> walletTwo = Utils.buildWallet(0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        List<MoneyDTO> walletThree = Utils.buildWallet(0, 0, 0, 0, 1, 1, 0, 0, 0, 6, 0);
        u1.setUserWallet(walletOne);
        u2.setUserWallet(walletTwo);
        u3.setUserWallet(walletThree);
        //=========================================================================================================//
        vendingMachineService.add(sodasVendingMachine);
        vendingMachineService.add(snacksVendingMachine);
        vendingMachineService.add(utilitiesVendingMachine);
        //=========================================================================================================//
        vendingMachineService.operateOnVendingMachine(sodasVendingMachine.getID());
        vendingMachineService.getCurrentVendingMachine().setLoggedUser(admin);
        vendingMachineService.getCurrentVendingMachine().setVmWallet(money);
        //=========================================================================================================//
        vendingMachineService.operateOnVendingMachine(snacksVendingMachine.getID());
        vendingMachineService.getCurrentVendingMachine().setLoggedUser(admin);
        vendingMachineService.getCurrentVendingMachine().setVmWallet(money);
        //=========================================================================================================//
        vendingMachineService.operateOnVendingMachine(utilitiesVendingMachine.getID());
        vendingMachineService.getCurrentVendingMachine().setLoggedUser(admin);
        vendingMachineService.getCurrentVendingMachine().setVmWallet(money);
        //=========================================================================================================//
        ProductDTO coke = new ProductDTO("Coke", ProductType.COKE);
        ProductDTO fanta = new ProductDTO("Fanta", ProductType.FANTA);
        ProductDTO sprite = new ProductDTO("Sprite", ProductType.SPRITE);
        //=======================================================================================================//
        ProductDTO peanuts = new ProductDTO("Peanuts", ProductType.PEANUTS);
        ProductDTO chips = new ProductDTO("Lays", ProductType.CHIPS);
        ProductDTO skittles = new ProductDTO("Skittles", ProductType.SKITTLES);
        //=======================================================================================================//
        ProductDTO mask = new ProductDTO("Mask", ProductType.FPP2_MASK);
        ProductDTO magnet = new ProductDTO("Magnet", ProductType.MAGNET);
        ProductDTO gum = new ProductDTO("Hubba Bubba", ProductType.GUM);
        ProductDTO cigar = new ProductDTO("Cigar", ProductType.CIGAR);
        //======================================================================================================//
        productService.add(coke);
        productService.add(fanta);
        productService.add(sprite);
        productService.add(peanuts);
        productService.add(chips);
        productService.add(skittles);
        productService.add(mask);
        productService.add(magnet);
        productService.add(gum);
        productService.add(cigar);
        //========================================================================================================//
        vendingMachineService.setCurrentVendingMachine(null);
        vendingMachineService.operateOnVendingMachine(sodasVendingMachine.getID());
        vendingMachineService.getCurrentVendingMachine().setLoggedUser(admin);
        vendingMachineService.setProductOfSlot("A1", coke.getName());
        vendingMachineService.setProductOfSlot("A2", fanta.getName());
        vendingMachineService.setProductOfSlot("A3", sprite.getName());
        vendingMachineService.setProductOfSlot("B1", coke.getName());
        vendingMachineService.setProductOfSlot("B2", fanta.getName());
        vendingMachineService.setProductOfSlot("C1", sprite.getName());
        vendingMachineService.setProductOfSlot("C3", sprite.getName());
        vendingMachineService.loadProduct("A1");
        vendingMachineService.loadProduct("A1");
        vendingMachineService.loadProduct("A2");
        vendingMachineService.loadProduct("B2");
        vendingMachineService.loadProduct("C1");
        vendingMachineService.loadProduct("C1");
        vendingMachineService.loadProduct("C1");
        vendingMachineService.loadProduct("C3");
        //========================================================================================================//
        vendingMachineService.setCurrentVendingMachine(null);
        vendingMachineService.operateOnVendingMachine(sodasVendingMachine.getID());
        vendingMachineService.getCurrentVendingMachine().setLoggedUser(admin);
        vendingMachineService.setProductOfSlot("A2", peanuts.getName());
        vendingMachineService.setProductOfSlot("C1", peanuts.getName());
        vendingMachineService.setProductOfSlot("B2", chips.getName());
        vendingMachineService.loadProduct("A2");
        vendingMachineService.loadProduct("C1");
        vendingMachineService.loadProduct("B2");
        vendingMachineService.loadProduct("B2");
        //===============================================//
        vendingMachineService.setCurrentVendingMachine(null);
        vendingMachineService.operateOnVendingMachine(sodasVendingMachine.getID());
        vendingMachineService.getCurrentVendingMachine().setLoggedUser(admin);
        vendingMachineService.setProductOfSlot("A1", mask.getName());
        vendingMachineService.setProductOfSlot("A3", magnet.getName());
        vendingMachineService.loadProduct("A1");
        vendingMachineService.loadProduct("A1");
        vendingMachineService.loadProduct("A3");
        //=============================================//
        vendingMachineService.setCurrentVendingMachine(null);
        vendingMachineService.operateOnVendingMachine(sodasVendingMachine.getID());
        vendingMachineService.getCurrentVendingMachine().setLoggedUser(u1);
        vendingMachineService.payMoney(MoneyType.ONE_EURO);
        vendingMachineService.payMoney(MoneyType.ONE_EURO);
        vendingMachineService.payMoney(MoneyType.TWENTY_CENTS);
        vendingMachineService.payMoney(MoneyType.FIFTY_CENTS);
        Exception e1 = assertThrows(VendingMachineException.class, () -> vendingMachineService.buyProduct("A3", false));
        assertEquals(e1.getMessage(), "No products in that slot!");
        vendingMachineService.buyProduct("B2", false);
        vendingMachineService.buyProduct("A1", false);
        Exception e2 = assertThrows(VendingMachineException.class, () -> vendingMachineService.buyProduct("B2", true));
        assertEquals(e2.getMessage(), "No products in that slot!");
        vendingMachineService.finaliseTransaction();
        assertEquals(1, u1.getUserWallet().get(7).getAmount());
        //========================================================================================//
        vendingMachineService.setCurrentVendingMachine(null);
        vendingMachineService.operateOnVendingMachine(utilitiesVendingMachine.getID());
        vendingMachineService.getCurrentVendingMachine().setLoggedUser(u2);
        vendingMachineService.payMoney(MoneyType.TWENTY_EUROS);
        vendingMachineService.buyProduct("A3", false);
        vendingMachineService.buyProduct("A1", false);
        Exception e3 = assertThrows(VendingMachineException.class, () -> vendingMachineService.buyProduct("A2", false));
        Exception e4 = assertThrows(VendingMachineException.class, () -> vendingMachineService.buyProduct("A3", false));
        assertEquals(e3.getMessage(), "No products in that slot!");
        assertEquals(e4.getMessage(), "No products in that slot!");
        vendingMachineService.finaliseTransaction();
        assertEquals(1, u2.getUserWallet().get(4).getAmount());
        assertEquals(1, u2.getUserWallet().get(5).getAmount());
        assertEquals(1, u2.getUserWallet().get(7).getAmount());
        vendingMachineService.setCurrentVendingMachine(null);
        vendingMachineService.operateOnVendingMachine(snacksVendingMachine.getID());
        vendingMachineService.getCurrentVendingMachine().setLoggedUser(u2);
        vendingMachineService.payMoney(MoneyType.TWO_EUROS);
        vendingMachineService.payMoney(MoneyType.ONE_EURO);
        Exception e5 = assertThrows(VendingMachineException.class, () -> vendingMachineService.buyProduct("C3", false));
        assertEquals(e5.getMessage(), "No products in that slot!");
        vendingMachineService.buyProduct("A3", false);
        vendingMachineService.buyProduct("A3", false);
        vendingMachineService.buyProduct("A3", false);
        vendingMachineService.buyProduct("C1", true);
        assertEquals(1, u2.getUserWallet().get(6).getAmount());
        assertEquals(2, u2.getUserWallet().get(7).getAmount());
        assertEquals(1, u2.getUserWallet().get(9).getAmount());
        vendingMachineService.setCurrentVendingMachine(null);
        vendingMachineService.operateOnVendingMachine(sodasVendingMachine.getID());
        vendingMachineService.getCurrentVendingMachine().setLoggedUser(u2);
        vendingMachineService.payMoney(MoneyType.FIFTY_CENTS);
        vendingMachineService.payMoney(MoneyType.TWENTY_CENTS);
        vendingMachineService.payMoney(MoneyType.TWENTY_CENTS);
        vendingMachineService.payMoney(MoneyType.FIVE_CENTS);
        Exception e6 = assertThrows(VendingMachineException.class, () -> vendingMachineService.buyProduct("A1", false));
        Exception e7 = assertThrows(VendingMachineException.class, () -> vendingMachineService.buyProduct("C3", true));
        assertEquals(e6.getMessage(), "Must insert more money!");
        assertEquals(e7.getMessage(), "Must insert more money!");
        vendingMachineService.finaliseTransaction();
        assertEquals(1, u2.getUserWallet().get(6).getAmount());
        assertEquals(2, u2.getUserWallet().get(7).getAmount());
        assertEquals(1, u2.getUserWallet().get(9).getAmount());
        //===============================================================================================================//
        vendingMachineService.setCurrentVendingMachine(null);
        vendingMachineService.operateOnVendingMachine(sodasVendingMachine.getID());
        vendingMachineService.getCurrentVendingMachine().setLoggedUser(u3);
        vendingMachineService.payMoney(MoneyType.ONE_EURO);
        vendingMachineService.payMoney(MoneyType.FIVE_CENTS);
        vendingMachineService.payMoney(MoneyType.FIVE_CENTS);
        vendingMachineService.payMoney(MoneyType.FIVE_CENTS);
        vendingMachineService.payMoney(MoneyType.FIVE_CENTS);
        vendingMachineService.payMoney(MoneyType.FIVE_CENTS);
        vendingMachineService.buyProduct("C3", true);
        assertEquals(u3.getUserWallet().get(5).getAmount(), 0);
        assertEquals(u3.getUserWallet().get(4).getAmount(), 1);
        assertEquals(u3.getUserWallet().get(9).getAmount(), 1);
        vendingMachineService.setCurrentVendingMachine(null);
        vendingMachineService.operateOnVendingMachine(snacksVendingMachine.getID());
        vendingMachineService.getCurrentVendingMachine().setLoggedUser(u3);
        vendingMachineService.payMoney(MoneyType.TWO_EUROS);
        vendingMachineService.buyProduct("A2", false);
        vendingMachineService.buyProduct("B2", true);
        assertEquals(1, u3.getUserWallet().get(6).getAmount());
        assertEquals(1, u3.getUserWallet().get(8).getAmount());
        vendingMachineService.setCurrentVendingMachine(null);
        vendingMachineService.operateOnVendingMachine(utilitiesVendingMachine.getID());
        vendingMachineService.getCurrentVendingMachine().setLoggedUser(u3);
        vendingMachineService.payMoney(MoneyType.FIFTY_CENTS);
        Exception e8 = assertThrows(VendingMachineException.class, () -> vendingMachineService.buyProduct("A4", false));
        assertEquals(e8.getMessage(), "Must insert more money!");
        vendingMachineService.payMoney(MoneyType.TEN_CENTS);
        vendingMachineService.buyProduct("A4", true);
        assertEquals(u3.getUserWallet().get(9).getAmount(), 2);
        //==================================================================//
//        admin.setUserWallet(Utils.buildGenericWallet(new ArrayList<MoneyDTO>()));
//        snacksVendingMachine.login(admin);
//        utilitiesVendingMachine.login(admin);
//        sodasVendingMachine.login(admin);
//        System.out.println(snacksVendingMachine.getMoneyInInventory());
//        System.out.println(sodasVendingMachine.getMoneyInInventory());
//        System.out.println(utilitiesVendingMachine.getMoneyInInventory());
//        sodasVendingMachine.getStatus();
//        snacksVendingMachine.getStatus();
//        utilitiesVendingMachine.getStatus();
//        sodasVendingMachine.takeProfits();
//        snacksVendingMachine.takeProfits();
//        utilitiesVendingMachine.takeProfits();
//        //====================================================//
//        snacksVendingMachine.setMoneyInInventory(cents);
//        sodasVendingMachine.setMoneyInInventory(cents);
//        utilitiesVendingMachine.setMoneyInInventory(cents);
//        for (int i = 0; i < 5; i++) {
//            sodasVendingMachine.loadProduct(secondCoke);
//            sodasVendingMachine.loadProduct(secondFanta);
//            sodasVendingMachine.loadProduct(thirdSprite);
//            sodasVendingMachine.loadProduct(secondSprite);
//            //============================================//
//            snacksVendingMachine.loadProduct(peanuts);
//            snacksVendingMachine.loadProduct(secondChips);
//            snacksVendingMachine.loadProduct(secondSkittles);
//            snacksVendingMachine.loadProduct(thirdSkittles);
//            //==============================================//
//            utilitiesVendingMachine.loadProduct(cigar);
//            utilitiesVendingMachine.loadProduct(magnet);
//            if (i != 4) {
//                sodasVendingMachine.loadProduct(coke);
//                sodasVendingMachine.loadProduct(fanta);
//                //=======================================//
//                snacksVendingMachine.loadProduct(chips);
//                //========================================//
//                utilitiesVendingMachine.loadProduct(mask);
//            }
//        }
//        sodasVendingMachine.loadProduct(sprite);
//        sodasVendingMachine.loadProduct(sprite);
//        //======================================//
//        snacksVendingMachine.loadProduct(skittles);
//        snacksVendingMachine.loadProduct(skittles);
//        snacksVendingMachine.loadProduct(skittles);
//        //=============================================//
//        utilitiesVendingMachine.loadProduct(gum);
//        //=============================================//
//        sodasVendingMachine.getStatus();
//        snacksVendingMachine.getStatus();
//        utilitiesVendingMachine.getStatus();
    }

}
