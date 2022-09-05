package com.VendingMachines.root.api;

import com.VendingMachines.root.entities.Product;
import com.VendingMachines.root.entities.Slot;
import com.VendingMachines.root.entities.VendingMachine;
import com.VendingMachines.root.exceptions.UserException;
import com.VendingMachines.root.exceptions.VendingMachineException;
import com.VendingMachines.root.model.VendingMachineDTO;
import com.VendingMachines.root.services.ProductService;
import com.VendingMachines.root.services.UserService;
import com.VendingMachines.root.services.VendingMachineService;
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
    private final ProductService productService;
    private boolean currentlyOperating = false;
    private List<Slot> slotsForMachine;

    public VendingMachinesController(VendingMachineService vendingMachineService, UserService userService, ProductService productService) {
        this.vendingMachineService = vendingMachineService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping
    public List<VendingMachine> getVendingMachines() {
        return vendingMachineService.getAllVendingMachines();
    }

    @PostMapping
    public void addVendingMachine(@RequestBody VendingMachineDTO vendingMachineDTO) {
        vendingMachineDTO.setSlots(slotsForMachine);
        vendingMachineService.add(vendingMachineDTO);
        slotsForMachine = null;
    }

    @PutMapping
    public void updateVendingMachine(@RequestParam UUID Id, @RequestBody VendingMachineDTO vendingMachineDTO) throws VendingMachineException {
        vendingMachineService.update(Id, vendingMachineDTO);
    }

    @DeleteMapping
    public void deleteVendingMachine(@RequestParam UUID Id) {
        vendingMachineService.delete(Id);
    }

    @GetMapping(path = "/getByID/")
    public VendingMachine getVendingMachineByID(UUID ID) {
        return vendingMachineService.findByID(ID);
    }

    @GetMapping(path = "/getByUsername/")
    public VendingMachine getVendingMachineByName(@RequestParam String name) {
        return vendingMachineService.findByName(name);
    }

    @GetMapping(path = "/getByAdress")
    public VendingMachine getVendingMachineByAdress(@RequestParam String adress) {
        return vendingMachineService.findByAdress(adress);
    }

    @GetMapping(path = "/operateOnMachine")
    public String selectVendingMachine(@RequestParam String name) {
        try {
            UUID ID = vendingMachineService.findByName(name).getID();
            vendingMachineService.operateOnVendingMachine(ID);
            currentlyOperating = true;
            return "You are currently operating on the machine " + vendingMachineService.findByName(name);
        } catch (Exception e) {
            return "An exception has occured: " + e.getMessage();
        }
    }

    @GetMapping(path = "/logOnMachine")
    public String logOnMachine(@RequestParam String username, @RequestParam String password) throws UserException {
        if (userService.logIn(username, password)) {
            return "Log in successful!";
        } else {
            return "Log in failed";
        }
    }

    @PostMapping(path = "/loadProduct")
    public void loadProduct(@RequestBody Product product, @RequestParam String code) throws VendingMachineException {
        assert currentlyOperating;
        if (productService.findByName(product.getName()) == product) {
            vendingMachineService.loadProduct(code);
        } else {
            throw new VendingMachineException("The product cannot be added because it does not exist!");
        }
    }

    @PostMapping(path = "/createIntialSlotsForMachine")
    public void createSlots(@RequestParam String firstSlotCode, @RequestParam String lastSlotCode) throws VendingMachineException {
        slotsForMachine = new ArrayList<>();
        if (firstSlotCode.charAt(0) > lastSlotCode.charAt(0)) {
            throw new VendingMachineException("The first slot letter has to be lower/equal to the last one");
        } else if (Integer.parseInt(firstSlotCode.substring(1)) > Integer.parseInt(lastSlotCode.substring(1))) {
            throw new VendingMachineException("The first slot number cannot be higher than the last one's!");
        } else {
            slotsForMachine = new ArrayList<>();
            for (char row = firstSlotCode.charAt(0); row <= lastSlotCode.charAt(0); row++) {
                for (int i = 1; i <= Integer.parseInt(lastSlotCode.substring(1)); i++) {
                    String newCode = row + String.valueOf(i);
                    slotsForMachine.add(new Slot(newCode, 0));
                }
            }
        }
    }

    @PostMapping(path = "/configureSlots")
    public void configureSlot(@RequestParam String code, @RequestParam int price, @RequestParam String productName) throws VendingMachineException {
        boolean found = false;
        if (code.charAt(0) < slotsForMachine.get(0).getCode().charAt(0)
                || Integer.parseInt(code.substring(1)) < Integer.parseInt(slotsForMachine.get(0).getCode().substring(1))
                || code.charAt(0) > slotsForMachine.get(slotsForMachine.size() - 1).getCode().charAt(0)
                || Integer.parseInt(code.substring(1)) > Integer.parseInt(slotsForMachine.get(slotsForMachine.size() - 1).getCode().substring(1))) {
            throw new VendingMachineException("The code is not valid!");
        } else {
            Product product = productService.findByName(productName);
            for (Slot slot : slotsForMachine) {
                if (Objects.equals(slot.getCode(), code)) {
                    slot.setProduct(product);
                    slot.setPrice(price);
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
        if(vendingMachineService.getCurrentVendingMachine().getLoggedUser() == null){
            throw new UserException("There is no logged user to log out!");
        }
        else {
            vendingMachineService.getCurrentVendingMachine().setLoggedUser(null);
            return "Log out successful!";
        }
    }
    @GetMapping(path="/getSlots")
    public List<Slot> getSlotsForMachine(){
        return slotsForMachine;
    }



}
