package com.VendingMachines.root.api;

import com.VendingMachines.root.commons.Utils;
import com.VendingMachines.root.entities.Product;
import com.VendingMachines.root.entities.Slot;
import com.VendingMachines.root.entities.VendingMachine;
import com.VendingMachines.root.exceptions.ProductException;
import com.VendingMachines.root.exceptions.UserException;
import com.VendingMachines.root.exceptions.VendingMachineException;
import com.VendingMachines.root.model.VendingMachineDTO;
import com.VendingMachines.root.repositories.SlotsRepository;
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
    private final SlotsRepository slotsRepository;
    private final ProductService productService;
    private boolean currentlyOperating = false;
    private List<Slot> slotsForMachine;

    public VendingMachinesController(VendingMachineService vendingMachineService, UserService userService, SlotsRepository slotsRepository, ProductService productService) {
        this.vendingMachineService = vendingMachineService;
        this.userService = userService;
        this.slotsRepository = slotsRepository;
        this.productService = productService;
    }

    @GetMapping
    public List<VendingMachine> getVendingMachines() {
        return vendingMachineService.getAllVendingMachines();
    }
    @PostMapping
    public void addVendingMachine(@RequestBody VendingMachineDTO vendingMachineDTO) throws VendingMachineException {
        if(slotsForMachine == null){
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
        return vendingMachineService.findByID(ID);
    }

    @GetMapping(path = "/getByUsername/")
    public VendingMachineDTO getVendingMachineByName(@RequestParam String name) {
        return vendingMachineService.findByName(name);
    }

    @GetMapping(path = "/getByAddress")
    public VendingMachineDTO getVendingMachineByAddress(@RequestParam String address) {
        return vendingMachineService.findByAddress(address);
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
        try {
            assert currentlyOperating;
        } catch (AssertionError e) {
            throw new VendingMachineException("You are not operating any vending machines currently!");
        }
        if (Utils.convertProductDTOToEntity(productService.findByName(product.getName())).equals(product)) {
            vendingMachineService.loadProduct(code);
        } else {
            throw new VendingMachineException("The product cannot be added because it does not exist!");
        }
    }

    @PostMapping(path = "/createIntialSlotsForMachine")
    public void createSlots(@RequestParam String firstSlotCode, @RequestParam String lastSlotCode) throws VendingMachineException {
        if (firstSlotCode.charAt(0) > lastSlotCode.charAt(0)) {
            throw new VendingMachineException("The first slot letter has to be lower/equal to the last one");
        } else if (Integer.parseInt(firstSlotCode.substring(1)) > Integer.parseInt(lastSlotCode.substring(1))) {
            throw new VendingMachineException("The first slot number cannot be higher than the last one's!");
        } else {
            slotsForMachine = new ArrayList<>();
            for (char row = firstSlotCode.charAt(0); row <= lastSlotCode.charAt(0); row++) {
                for (int i = 1; i <= Integer.parseInt(lastSlotCode.substring(1)); i++) {
                    String newCode = row + String.valueOf(i);
                    Slot slot = new Slot(newCode, 0);
                    slotsRepository.save(slot);
                    slotsForMachine.add(slot);
                }
            }
        }
        //TODO verificat daca sloturile sunt valide in service

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

            Product product = Utils.convertProductDTOToEntity(productService.findByName(productName));
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


}
