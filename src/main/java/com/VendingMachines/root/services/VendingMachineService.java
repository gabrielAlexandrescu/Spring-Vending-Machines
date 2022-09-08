package com.VendingMachines.root.services;

import com.VendingMachines.root.commons.Utils;
import com.VendingMachines.root.entities.Slot;
import com.VendingMachines.root.entities.VendingMachine;
import com.VendingMachines.root.enums.MoneyType;
import com.VendingMachines.root.exceptions.ProductException;
import com.VendingMachines.root.exceptions.UserException;
import com.VendingMachines.root.exceptions.VendingMachineException;
import com.VendingMachines.root.model.MoneyDTO;
import com.VendingMachines.root.model.VendingMachineDTO;
import com.VendingMachines.root.repositories.SlotsRepository;
import com.VendingMachines.root.repositories.VendingMachinesRepository;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class VendingMachineService {
    private final VendingMachinesRepository vendingMachinesRepository;
    private final UserService userService;
    private final ProductService productService;
    private final SlotsRepository slotsRepository;
    @Getter
    @Setter
    private VendingMachine currentVendingMachine;
    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public VendingMachineService(VendingMachinesRepository vendingMachinesRepository, UserService userService, ProductService productService, SlotsRepository slotsRepository) {
        this.vendingMachinesRepository = vendingMachinesRepository;
        this.userService = userService;
        this.productService = productService;
        this.slotsRepository = slotsRepository;
        this.currentVendingMachine = null;
    }


    public VendingMachine findByID(UUID ID) {
        VendingMachine vm = vendingMachinesRepository.findById(ID).orElseThrow(() -> new IllegalStateException("The machine with this ID doesn't exist!"));
        Hibernate.initialize(vm.getSlots());
        Hibernate.initialize(vm.getVmWallet());
        return vm;
    }

    public VendingMachine findByName(String name) {
        List<VendingMachine> vendingMachines = vendingMachinesRepository.findAll();
        for (VendingMachine vendingMachine : vendingMachines) {
            if (Objects.equals(vendingMachine.getName(), name)) {
                Hibernate.initialize(vendingMachine.getSlots());
                Hibernate.initialize(vendingMachine.getVmWallet());
                return vendingMachine;
            }
        }
        return null;
    }

    private VendingMachine vendingMachineDTOToEntity(VendingMachineDTO vendingMachineDTO) {
        return new VendingMachine(vendingMachineDTO.getLimitOfProductPerSlot(), vendingMachineDTO.getSlots(), vendingMachineDTO.getLimitOfMoneyPerValue(),
                vendingMachineDTO.getProductType(), vendingMachineDTO.getName(), vendingMachineDTO.getAddress());
    }

    private VendingMachineDTO vendingMachineToDTO(VendingMachine vendingMachine) {
        VendingMachineDTO vendingMachineDTO = new VendingMachineDTO(vendingMachine.getName(), vendingMachine.getAddress(), vendingMachine.getLimitOfProductPerSlot(), vendingMachine.getLimitOfMoneyPerValue(), vendingMachine.getProductType(), vendingMachine.getSlots());
        vendingMachineDTO.setID(vendingMachine.getID());
        return vendingMachineDTO;
    }

    public VendingMachine findByAddress(String address) {
        List<VendingMachine> vendingMachines = vendingMachinesRepository.findAll();
        for (VendingMachine vendingMachine : vendingMachines) {
            if (Objects.equals(vendingMachine.getAddress(), address)) {
                Hibernate.initialize(vendingMachine.getSlots());
                Hibernate.initialize(vendingMachine.getVmWallet());
                return vendingMachine;
            }
        }
        return null;
    }

    public List<VendingMachine> getAllVendingMachines() {
        return vendingMachinesRepository.findAll();
    }

    public void add(VendingMachineDTO vendingMachineDTO) {
        VendingMachine vendingMachine = vendingMachineDTOToEntity(vendingMachineDTO);
        vendingMachinesRepository.save(vendingMachine);
    }

    public void delete(UUID ID) {
        vendingMachinesRepository.deleteById(ID);
    }

    public void update(UUID ID, VendingMachineDTO updatedVendingMachine) throws VendingMachineException {
        VendingMachine toBeUpdatedTo = vendingMachineDTOToEntity(updatedVendingMachine);
        VendingMachine vendingMachine = findByID(ID);
        if (!Objects.equals(vendingMachine.getLoggedUser().getRole(), "admin")) {
            throw new VendingMachineException("Only admins can do that!");
        }
        vendingMachine.setName(toBeUpdatedTo.getName());
        vendingMachine.setAddress(toBeUpdatedTo.getAddress());
        vendingMachine.setLimitOfMoneyPerValue(toBeUpdatedTo.getLimitOfMoneyPerValue());
        vendingMachine.setSlots(toBeUpdatedTo.getSlots());
        vendingMachine.setProductType(toBeUpdatedTo.getProductType());
        vendingMachinesRepository.save(vendingMachine);
    }

    public void operateOnVendingMachine(UUID ID) {
        currentVendingMachine = findByID(ID);
    }

    private boolean checkIfChangePossible(int money) {
        ListIterator<MoneyDTO> listIterator = currentVendingMachine.getVmWallet().listIterator(currentVendingMachine.getVmWallet().size());
        getCurrentVendingMachine().setChange(new ArrayList<>());
        Utils.buildGenericWallet(getCurrentVendingMachine().getChange());
        while (listIterator.hasPrevious()) {
            MoneyDTO obj = listIterator.previous();
            int maxCoin = obj.getMoneyType().getValue();
            while (money >= maxCoin && obj.getAmount() > 0) {
                MoneyDTO changeType = currentVendingMachine.getChange().get(currentVendingMachine.getVmWallet().size() - listIterator.previousIndex() - 1);
                changeType.setAmount(changeType.getAmount() + 1);
                currentVendingMachine.getChange().set(currentVendingMachine.getVmWallet().size() - listIterator.previousIndex() - 1, changeType);
                money -= maxCoin;
                obj.setAmount(obj.getAmount() - 1);
            }
        }
        return money == 0;
    }

    public boolean giveChange(int money) throws VendingMachineException {
        if (money == 0) {
            logger.log(Level.INFO, "No change needed!\n");
            return true;
        } else {
            if (checkIfChangePossible(money)) {
                ListIterator<MoneyDTO> listIterator = currentVendingMachine.getChange().listIterator(currentVendingMachine.getChange().size());
                while (listIterator.hasPrevious()) {
                    userService.addMoneyToWallet(currentVendingMachine.getLoggedUser().getID(), listIterator.previous().getMoneyType(), 1);
                    while (listIterator.previous().getAmount() > 0) {
                        listIterator.previous().setAmount(listIterator.previous().getAmount() - 1);
                        if (listIterator.previous().getMoneyType().getValue() < 100) {
                            logger.log(Level.INFO, "A coin of " + listIterator.previous().getMoneyType().getValue() + " euro-money has been returned!\n");
                        } else if (listIterator.previous().getMoneyType().getValue() == 100) {
                            logger.log(Level.INFO, "A coin of " + listIterator.previous().getMoneyType().getValue() / 100 + " euro has been returned!\n");
                        } else if (listIterator.previous().getMoneyType().getValue() == 200) {
                            logger.log(Level.INFO, "A coin of " + listIterator.previous().getMoneyType().getValue() / 100 + " euros has been returned!\n");
                        } else {
                            logger.log(Level.INFO, "A bill of " + listIterator.previous().getMoneyType().getValue() / 100 + " euros has been returned!\n");
                        }
                    }
                }
                logger.log(Level.INFO, "Change was given\n");
                return true;
            } else {
                ListIterator<MoneyDTO> listIterator = currentVendingMachine.getVmWallet().listIterator(currentVendingMachine.getVmWallet().size());
                while (listIterator.hasPrevious()) {
                    int amount = currentVendingMachine.getChange().get(currentVendingMachine.getVmWallet().size() - listIterator.previousIndex() - 1).getAmount();
                    listIterator.previous().setAmount(listIterator.previous().getAmount() + amount);
                    MoneyDTO changeTypeZero = listIterator.previous();
                    changeTypeZero.setAmount(0);
                    currentVendingMachine.getChange().set(currentVendingMachine.getVmWallet().size() - listIterator.previousIndex() - 1, changeTypeZero);
                }
                logger.log(Level.SEVERE, "Error", new VendingMachineException("Not enough money in inventory for giving change"));
                throw new VendingMachineException("Not enough money in inventory for giving change!");
            }
        }
    }

    private void checkIfProductsInCode(String code) throws VendingMachineException {
        for (Slot slot : currentVendingMachine.getSlots()) {
            if (slot.getAmount() == 0 && Objects.equals(slot.getCode(), code)) {
                logger.log(Level.SEVERE, "No products in that slot!");
                throw new VendingMachineException("No products in that slot!");
            }
        }
    }

    private Slot checkIfCodeValid(String code) throws VendingMachineException {
        char min = '[', max = 'A';
        int min_number = Integer.MAX_VALUE, max_number = Integer.MIN_VALUE;
        boolean found = false;
        for (Slot slot : currentVendingMachine.getSlots()) {
            if (slot.getCode().charAt(0) < min) {
                min = slot.getCode().charAt(0);
            }
            if (slot.getCode().charAt(0) > max) {
                max = slot.getCode().charAt(0);
            }
            if (Integer.parseInt(slot.getCode().substring(1)) < min_number) {
                min_number = Integer.parseInt(slot.getCode().substring(1));
            }
            if (Integer.parseInt(slot.getCode().substring(1)) > max_number) {
                max_number = Integer.parseInt(slot.getCode().substring(1));
            }
            if (Objects.equals(slot.getCode(), code)) {
                found = true;
            }
        }
        if (!(code.charAt(0) >= min && code.charAt(0) <= max)) {
            logger.log(Level.SEVERE, "User input an invalid row character! ");
            throw new VendingMachineException("Invalid code!");
        }
        if (!(Integer.parseInt(code.substring(1)) >= min_number && Integer.parseInt(code.substring(1)) <= max_number)) {
            logger.log(Level.SEVERE, "User input an invalid column character! ");
            throw new VendingMachineException("Invalid code!");
        }
        if (!found) {
            logger.log(Level.SEVERE, "User input an invalid column character! ");
            throw new VendingMachineException("Invalid code!");
        }
        for (Slot slot : currentVendingMachine.getSlots()) {
            if (Objects.equals(slot.getCode(), code))
                return slot;
        }
        return null;
    }

    public boolean buyProduct(String code, boolean last) throws VendingMachineException {
        Slot slot = checkIfCodeValid(code);
        if (slot == null) {
            throw new VendingMachineException("The code is not valid!");
        }
        checkIfProductsInCode(code);
        if (currentVendingMachine.getCurrentMoneyAddedByUser() >= slot.getPrice()) {
            slot.setAmount(slot.getAmount() - 1);
            slotsRepository.save(slot);
            if (!last) {
                currentVendingMachine.setCurrentMoneyAddedByUser(currentVendingMachine.getCurrentMoneyAddedByUser() - slot.getPrice());
            } else {
                currentVendingMachine.setCurrentMoneyAddedByUser(0);
                addMoneyToInventory();
                giveChange((currentVendingMachine.getCurrentMoneyAddedByUser() - slot.getPrice()));
            }
            logger.log(Level.INFO, "Item " + slot.getProduct().getName() + " has been bought by user " + currentVendingMachine.getLoggedUser().getUsername() + "\n");
            userService.addTransaction(currentVendingMachine.getLoggedUser().getID(), slot.getProduct());
            if (last) {
                currentVendingMachine.setLoggedUser(null);
            }
            vendingMachinesRepository.save(currentVendingMachine);
            return true;
        } else {
            logger.log(Level.WARNING, "The user hasn't inserted enough money!\n");
            throw new VendingMachineException("Must insert more money!");
        }
    }

    private void addMoneyToInventory() {
        for (int i = 0; i < currentVendingMachine.getVmWallet().size(); i++) {
            currentVendingMachine.getVmWallet().get(i).setAmount(currentVendingMachine.getVmWallet().get(i).getAmount() + currentVendingMachine.getUserInsertedMoney().get(i).getAmount());
            currentVendingMachine.getUserInsertedMoney().get(i).setAmount(0);
        }
    }

    public void loadProduct(String code) throws VendingMachineException {
        if (!Objects.equals(currentVendingMachine.getLoggedUser().getRole(), "admin")) {
            throw new VendingMachineException("Only admins can do that!");
        } else {
            Slot slot = checkIfCodeValid(code);
            if (slot == null) {
                throw new VendingMachineException("The code is not valid!");
            }
            if (slot.getAmount() == currentVendingMachine.getLimitOfProductPerSlot()) {
                throw new VendingMachineException("Too many products in that slot!");
            }
            slot.setAmount(slot.getAmount()+1);
            slotsRepository.save(slot);
            vendingMachinesRepository.save(currentVendingMachine);
        }
    }

    public void unloadProduct(String code) throws VendingMachineException {
        if (!Objects.equals(currentVendingMachine.getLoggedUser().getRole(), "admin")) {
            throw new VendingMachineException("Only admins can do that!");
        } else {
            Slot slot = checkIfCodeValid(code);
            if (slot == null) {
                throw new VendingMachineException("The code is not valid!");
            }
            if (slot.getAmount() == 0) {
                throw new VendingMachineException("No products in that slot!");
            }
            slot.setAmount(slot.getAmount() - 1);
            slotsRepository.save(slot);
            vendingMachinesRepository.save(currentVendingMachine);
        }
    }

    public void payMoney(MoneyType moneyType) throws VendingMachineException, UserException {
        if (currentVendingMachine.getLoggedUser() == null) {
            throw new VendingMachineException("There is no logged user to do this operation!");
        }
        boolean valid = false;
        if(getCurrentVendingMachine().getUserInsertedMoney() == null)
        {
            getCurrentVendingMachine().setUserInsertedMoney(new ArrayList<>());
            Utils.buildGenericWallet(getCurrentVendingMachine().getUserInsertedMoney());
        }
        for (MoneyDTO obj : getCurrentVendingMachine().getUserInsertedMoney()) {
            if (obj.getMoneyType() == moneyType) {
                if (obj.getAmount() < currentVendingMachine.getLimitOfMoneyPerValue()) {
                    obj.setAmount(obj.getAmount() + 1);
                    valid = true;
                    userService.removeMoneyFromWallet(currentVendingMachine.getLoggedUser().getID(), obj.getMoneyType(), 1);
                    getCurrentVendingMachine().setCurrentMoneyAddedByUser(getCurrentVendingMachine().getCurrentMoneyAddedByUser()+obj.getMoneyType().getValue());
                    vendingMachinesRepository.save(currentVendingMachine);
                } else {
                    throw new VendingMachineException("There is too much money of that type!");
                }
            }
        }
        if (!valid) {
            throw new VendingMachineException("The money type is not valid!");
        }
    }

    public void loadMoney(List<MoneyDTO> wallet) throws VendingMachineException {
        if (!Objects.equals(currentVendingMachine.getLoggedUser().getRole(), "admin")) {
            throw new VendingMachineException("Only admins can do that!");
        } else {
            for (int i = 0; i < wallet.size(); i++) {
                currentVendingMachine.getVmWallet().get(i).setAmount(
                        currentVendingMachine.getVmWallet().get(i).getAmount() + wallet.get(i).getAmount());
            }
        }
    }

    public void unloadMoney() throws VendingMachineException {
        if (!Objects.equals(currentVendingMachine.getLoggedUser().getRole(), "admin")) {
            throw new VendingMachineException("Only admins can do that!");
        } else {
            for (MoneyDTO obj : currentVendingMachine.getVmWallet()) {
                userService.addMoneyToWallet(currentVendingMachine.getLoggedUser().getID(), obj.getMoneyType(), obj.getAmount());
                obj.setAmount(0);
            }
        }
    }

    public void finaliseTransaction() throws VendingMachineException {
        addMoneyToInventory();
        giveChange(currentVendingMachine.getCurrentMoneyAddedByUser());
        logger.log(Level.INFO, "The transaction has been canceled!\n");
    }

    public void setProductOfSlot(String code, String productName) throws ProductException, UserException {
        if (!Objects.equals(getCurrentVendingMachine().getLoggedUser().getRole(), "admin")) {
            throw new UserException("Only admins can do that!");
        }
        for (Slot slot : getCurrentVendingMachine().getSlots()) {
            if (Objects.equals(slot.getCode(), code)) {
                if (productService.findByName(productName) == null) {
                    throw new ProductException("The product doesn't exist!");
                } else {
                    slot.setProduct(productService.findByName(productName));
                }
            }
        }
    }
}
