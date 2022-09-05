package com.VendingMachines.root.services;

import com.VendingMachines.root.entities.Product;
import com.VendingMachines.root.entities.Slot;
import com.VendingMachines.root.entities.VendingMachine;
import com.VendingMachines.root.exceptions.VendingMachineException;
import com.VendingMachines.root.model.MoneyDTO;
import com.VendingMachines.root.model.VendingMachineDTO;
import com.VendingMachines.root.repositories.VendingMachinesRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class VendingMachineService {
    private final VendingMachinesRepository vendingMachinesRepository;
    private final UserService userService;
    @Getter
    private VendingMachine currentVendingMachine;
    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public VendingMachineService(VendingMachinesRepository vendingMachinesRepository, UserService userService) {
        this.vendingMachinesRepository = vendingMachinesRepository;
        this.userService = userService;
        this.currentVendingMachine = null;
    }
    public VendingMachine findByID(UUID ID){
        return vendingMachinesRepository.findById(ID).orElseThrow(()->new IllegalStateException("The user with this ID doesn't exist!"));
    }
    public VendingMachine findByName(String name){
        List<VendingMachine> vendingMachines = vendingMachinesRepository.findAll();
        for(VendingMachine vendingMachine:vendingMachines){
            if(Objects.equals(vendingMachine.getName(), name))
                return vendingMachine;
        }
        return null;
    }
    private VendingMachine VendingMachineDTOToEntity(VendingMachineDTO vendingMachineDTO){
        return new VendingMachine(vendingMachineDTO.getLimitOfProductPerSlot(),vendingMachineDTO.getSlots(),vendingMachineDTO.getLimitOfMoneyPerValue(),
                vendingMachineDTO.getProductType(),vendingMachineDTO.getName(), vendingMachineDTO.getAdress());
    }
    public VendingMachine findByAdress(String adress){
        List<VendingMachine> vendingMachines = vendingMachinesRepository.findAll();
        for(VendingMachine vendingMachine:vendingMachines){
            if(Objects.equals(vendingMachine.getName(), adress))
                return vendingMachine;
        }
        return null;
    }
    public List<VendingMachine> getAllVendingMachines(){
        return vendingMachinesRepository.findAll();
    }
    public void add(VendingMachineDTO vendingMachineDTO){
        VendingMachine vendingMachine = VendingMachineDTOToEntity(vendingMachineDTO);
        List<Slot> slots = vendingMachine.getSlots();
        for(Slot slot:slots){
            slot.setVendingMachine(vendingMachine);
        }
        vendingMachine.setSlots(slots);
        vendingMachinesRepository.save(vendingMachine);
    }
    public void delete(UUID ID){
        vendingMachinesRepository.deleteById(ID);
    }
    public void update(UUID ID,VendingMachineDTO updatedVendingMachine) throws VendingMachineException {
        VendingMachine toBeUpdatedTo = VendingMachineDTOToEntity(updatedVendingMachine);
        VendingMachine vendingMachine = findByID(ID);
        if(!Objects.equals(vendingMachine.getLoggedUser().getRole(), "admin"))
        {
            throw new VendingMachineException("Only admins can do that!");
        }
        vendingMachine.setName(toBeUpdatedTo.getName());
        vendingMachine.setAdress(toBeUpdatedTo.getAdress());
        vendingMachine.setLimitOfMoneyPerValue(toBeUpdatedTo.getLimitOfMoneyPerValue());
        vendingMachine.setSlots(toBeUpdatedTo.getSlots());
        vendingMachine.setProductType(toBeUpdatedTo.getProductType());
        vendingMachinesRepository.save(vendingMachine);
    }
    public void operateOnVendingMachine(UUID ID){
        currentVendingMachine = findByID(ID);
    }
    private boolean checkIfChangePossible(int money) {
        ListIterator<MoneyDTO> listIterator = currentVendingMachine.getVm_wallet().listIterator(currentVendingMachine.getVm_wallet().size());
        while (listIterator.hasPrevious()) {
            MoneyDTO obj = listIterator.previous();
            int maxCoin = obj.getMoneyType().getValue();
            while (money >= maxCoin && obj.getAmount() > 0) {
                MoneyDTO changeType = currentVendingMachine.getChange().get( currentVendingMachine.getVm_wallet().size() - listIterator.previousIndex() - 1);
                changeType.setAmount(changeType.getAmount() + 1);
                currentVendingMachine.getChange().set( currentVendingMachine.getVm_wallet().size() - listIterator.previousIndex() - 1, changeType);
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
                    userService.addCoinsToWallet( currentVendingMachine.getLoggedUser().getID(),listIterator.previous().getMoneyType(), 1);
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
                ListIterator<MoneyDTO> listIterator = currentVendingMachine.getVm_wallet().listIterator(currentVendingMachine.getVm_wallet().size());
                while (listIterator.hasPrevious()) {
                    int amount = currentVendingMachine.getChange().get(currentVendingMachine.getVm_wallet().size() - listIterator.previousIndex() - 1).getAmount();
                    listIterator.previous().setAmount(listIterator.previous().getAmount() + amount);
                    MoneyDTO changeTypeZero = listIterator.previous();
                    changeTypeZero.setAmount(0);
                    currentVendingMachine.getChange().set(currentVendingMachine.getVm_wallet().size() - listIterator.previousIndex() - 1, changeTypeZero);
                }
                logger.log(Level.SEVERE, "Error", new VendingMachineException("Not enough money in inventory for giving change"));
                throw new VendingMachineException("Not enough money in inventory for giving change!");
            }
        }
    }

    private void checkIfProductsInCode(String code) throws VendingMachineException {
        for (Slot slot : currentVendingMachine.getSlots()){
            if (slot.getAmount() == 0) {
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
                min_number = Integer.parseInt(slot.getCode().substring(1));
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
        for (Slot slot : currentVendingMachine.getSlots()){
            if(Objects.equals(slot.getCode(), code))
                return slot;
        }
        return null;
    }

    public boolean buyProduct(String code, boolean last) throws VendingMachineException {
        Slot slot = checkIfCodeValid(code);
        if(slot == null){
            throw new VendingMachineException("The code is not valid!");
        }
        checkIfProductsInCode(code);
        if (currentVendingMachine.getCurrentMoneyAddedByUser() >= slot.getPrice()) {
            slot.setAmount(slot.getAmount() - 1);
            if (!last) {
                currentVendingMachine.setCurrentMoneyAddedByUser(currentVendingMachine.getCurrentMoneyAddedByUser() - slot.getPrice());
            } else {
                currentVendingMachine.setCurrentMoneyAddedByUser(0);
                addMoneyToInventory();
                giveChange((currentVendingMachine.getCurrentMoneyAddedByUser()  - slot.getPrice()));
            }
            logger.log(Level.INFO, "Item " + slot.getProduct().getName() + " has been bought by user " + currentVendingMachine.getLoggedUser().getUsername() + "\n");
            userService.addTransaction(currentVendingMachine.getLoggedUser().getID(),slot.getProduct());
            if (last) {
                currentVendingMachine.setLoggedUser(null);
            }
            return true;
        } else {
            logger.log(Level.WARNING, "The user hasn't inserted enough money!\n");
            throw new VendingMachineException("Must insert more money!");
        }
    }

    private void addMoneyToInventory() {
        for (int i = 0; i < currentVendingMachine.getVm_wallet().size(); i++) {
            currentVendingMachine.getVm_wallet().get(i).setAmount(currentVendingMachine.getVm_wallet().get(i).getAmount() + currentVendingMachine.getUserInsertedMoney().get(i).getAmount());
            currentVendingMachine.getUserInsertedMoney().get(i).setAmount(0);
        }
    }
    public void loadProduct(String code) throws VendingMachineException {
        if(!Objects.equals(currentVendingMachine.getLoggedUser().getRole(), "admin")){
            throw new VendingMachineException("Only admins can do that!");
        }
        else{
            Slot slot = checkIfCodeValid(code);
            if(slot == null){
                throw new VendingMachineException("The code is not valid!");
            }
            if(slot.getAmount() == currentVendingMachine.getLimitOfProductPerSlot())
            {
                throw new VendingMachineException("Too many products in that slot!");
            }
            slot.setAmount(slot.getAmount()+1);
        }
    }


}
