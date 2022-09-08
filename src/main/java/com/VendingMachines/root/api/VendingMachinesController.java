package com.VendingMachines.root.api;

import com.VendingMachines.root.commons.Utils;
import com.VendingMachines.root.entities.Product;
import com.VendingMachines.root.entities.Slot;
import com.VendingMachines.root.entities.VendingMachine;
import com.VendingMachines.root.enums.MoneyType;
import com.VendingMachines.root.exceptions.ProductException;
import com.VendingMachines.root.exceptions.UserException;
import com.VendingMachines.root.exceptions.VendingMachineException;
import com.VendingMachines.root.model.VendingMachineDTO;
import com.VendingMachines.root.repositories.SlotsRepository;
import com.VendingMachines.root.services.ProductService;
import com.VendingMachines.root.services.UserService;
import com.VendingMachines.root.services.VendingMachineService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@EnableSwagger2
@RestController
@RequestMapping(path = "/api/vending_machines/")
public class VendingMachinesController {
    private final VendingMachineService vendingMachineService;
    private final UserService userService;
    private final SlotsRepository slotsRepository;
    private final ProductService productService;
    private boolean currentlyOperating = false;
    private List<String> userCodeList;
    private List<Slot> slotsForMachine;

    public VendingMachinesController(VendingMachineService vendingMachineService, UserService userService, SlotsRepository slotsRepository, ProductService productService) {
        this.vendingMachineService = vendingMachineService;
        this.userService = userService;
        this.slotsRepository = slotsRepository;
        this.productService = productService;
        this.userCodeList = null;
    }

    @GetMapping
    public List<VendingMachineDTO> getVendingMachines() {
        List<VendingMachineDTO> vendingMachineDTOS = new ArrayList<>();
        for(VendingMachine vm:vendingMachineService.getAllVendingMachines()){
            vendingMachineDTOS.add(Utils.convertVendingMachineToDTO(vm));
        }
        return vendingMachineDTOS;
    }

    @PostMapping
    public void addVendingMachine(@RequestBody VendingMachineDTO vendingMachineDTO) throws VendingMachineException {
        if (slotsForMachine == null) {
            throw new VendingMachineException("Cannot instantiate vending machine with no slots!");
        }
        vendingMachineDTO.setSlots(slotsForMachine);
        vendingMachineService.add(vendingMachineDTO);

    }

    @PutMapping
    public void updateVendingMachine(@RequestParam UUID Id, @RequestBody VendingMachineDTO vendingMachineDTO) throws VendingMachineException {
        vendingMachineService.update(Id, vendingMachineDTO);
    }

    @DeleteMapping
    public void deleteVendingMachine(@RequestParam UUID Id) {
        vendingMachineService.delete(Id);
        slotsRepository.deleteAll(slotsForMachine);
        slotsForMachine = null;
    }

    @GetMapping(path = "/getByID/")
    public VendingMachineDTO getVendingMachineByID(UUID ID) {
        return Utils.convertVendingMachineToDTO(vendingMachineService.findByID(ID));
    }

    @GetMapping(path = "/getByUsername/")
    public VendingMachineDTO getVendingMachineByName(@RequestParam String name) {
        return Utils.convertVendingMachineToDTO(vendingMachineService.findByName(name));
    }

    @GetMapping(path = "/getByAddress")
    public VendingMachineDTO getVendingMachineByAddress(@RequestParam String address) {
        return Utils.convertVendingMachineToDTO(vendingMachineService.findByAddress(address));
    }

    @GetMapping(path = "/operateOnMachine")
    public String selectVendingMachine(@RequestParam String name) {
        try {
            if (currentlyOperating) {
                throw new VendingMachineException("Cannot operate on two machines at the same time!");
            }
            UUID ID = vendingMachineService.findByName(name).getID();
            vendingMachineService.operateOnVendingMachine(ID);
            currentlyOperating = true;
            return "You are currently operating on the machine " + vendingMachineService.findByName(name);
        } catch (Exception e) {
            return "An exception has occurred: " + e.getMessage();
        }
    }

    @GetMapping(path = "/logOnMachine")
    public String logOnMachine(@RequestParam String username, @RequestParam String password) throws UserException {
        if (userService.logIn(username, password)) {
            vendingMachineService.getCurrentVendingMachine().setLoggedUser(userService.findByUsername(username));
            return "Log in successful!";
        } else {
            return "Log in failed";
        }
    }

    @PostMapping(path = "/loadProduct")
    public void loadProduct(@RequestBody Product product, @RequestParam String code) throws VendingMachineException {
        try {
            assert currentlyOperating;
        } catch (AssertionError e) {
            throw new VendingMachineException("You are not operating any vending machines currently!");
        }
        Product anotherProduct =  productService.findByName(product.getName());
        if (anotherProduct!= null && anotherProduct.getName().equals(product.getName())) {
            vendingMachineService.loadProduct(code);
        } else {
            throw new VendingMachineException("The product cannot be added because it does not exist!");
        }
    }

    @PostMapping(path = "/createInitialSlotsForMachine")
    public void createSlots(@RequestParam String firstSlotCode, @RequestParam String lastSlotCode) throws VendingMachineException {
        if (firstSlotCode.charAt(0) > lastSlotCode.charAt(0)) {
            throw new VendingMachineException("The first slot letter has to be lower/equal to the last one");
        } else if (Integer.parseInt(firstSlotCode.substring(1)) > Integer.parseInt(lastSlotCode.substring(1))) {
            throw new VendingMachineException("The first slot number cannot be higher than the last one's!");
        } else {
            slotsForMachine = Utils.buildSlotList(firstSlotCode,lastSlotCode);
            slotsRepository.saveAll(slotsForMachine);
        }

    }

    @PostMapping(path = "/configureSlots")
    public void configureSlot(@RequestParam String code, @RequestParam int price, @RequestParam String productName) throws VendingMachineException, ProductException {
        boolean found = false;
        if (code.charAt(0) < slotsForMachine.get(0).getCode().charAt(0)
                || Integer.parseInt(code.substring(1)) < Integer.parseInt(slotsForMachine.get(0).getCode().substring(1))
                || code.charAt(0) > slotsForMachine.get(slotsForMachine.size() - 1).getCode().charAt(0)
                || Integer.parseInt(code.substring(1)) > Integer.parseInt(slotsForMachine.get(slotsForMachine.size() - 1).getCode().substring(1))) {
            throw new VendingMachineException("The code is not valid!");
        } else {

            Product product = productService.findByName(productName);
            if (product == null) {
                throw new ProductException("The product doesn't exist!");
            }
            for (Slot slot : slotsForMachine) {
                if (Objects.equals(slot.getCode(), code)) {
                    slot.setProduct(product);
                    slot.setPrice(price);
                    slotsRepository.save(slot);
                    found = true;
                }
            }
            if (!found) {
                throw new VendingMachineException("The code couldn't be found!");
            }
        }
    }

    @GetMapping(path = "/logOut")
    public String logOut() throws UserException {
        if (vendingMachineService.getCurrentVendingMachine().getLoggedUser() == null) {
            throw new UserException("There is no logged user to log out!");
        } else {
            vendingMachineService.getCurrentVendingMachine().setLoggedUser(null);
            return "Log out successful!";
        }
    }

    @GetMapping(path = "/stopOperating")
    public String stopOperating() throws VendingMachineException {
        if (vendingMachineService.getCurrentVendingMachine() == null) {
            throw new VendingMachineException("There is no vending machine currently being operated!");
        } else {
            vendingMachineService.setCurrentVendingMachine(null);
            currentlyOperating = false;
            return "Successfully exited out of the machine!";
        }
    }

    @GetMapping(path = "/getSlots")
    public List<Slot> getSlotsForMachine() {
        return slotsForMachine;
    }
    @GetMapping(path="/payMoney")
    public String payMoney(MoneyType moneyType) throws VendingMachineException, UserException {
        vendingMachineService.payMoney(moneyType);
        return "Transaction successful!";
    }
    @GetMapping(path = "/buyProducts")
    public String buyProducts() {
        if (userCodeList == null) {
            return "The list of products has to exist!";
        }
        if(userCodeList.size() == 0)
        {
            return "The list cannot be empty!";
        }
        for (int i = 0; i < userCodeList.size(); i++) {
            try {
                String code = userCodeList.get(i);
                vendingMachineService.buyProduct(code, i != userCodeList.size() - 1);
            } catch (VendingMachineException e) {
                if (Objects.equals(e.getMessage(), "Must insert more money!")) {
                    String code = userCodeList.get(i);
                    userCodeList = null;
                    return "You didn't have enough money for product with code " + code;
                }
            }
        }
        return null;
    }

    @PostMapping(path = "/insertProductIntoShoppingList/")
    public void addProductToShoppingList(@RequestParam String code) {
        if (userCodeList == null) {
            userCodeList = new ArrayList<>();
        }
        userCodeList.add(code);
    }

    @GetMapping(path = "/getShoppingList")
    public List<String> getShoppingList() {
        if (userCodeList == null)
            return null;
        else return userCodeList;
    }

    @DeleteMapping(path = "/deleteACodeFromShoppingList")
    public void deleteCodeFromShoppingList(String code) throws VendingMachineException {
        if (!userCodeList.contains(code)) {
            throw new VendingMachineException("You haven't inserted that code!");
        } else userCodeList.remove(code);
    }


}
