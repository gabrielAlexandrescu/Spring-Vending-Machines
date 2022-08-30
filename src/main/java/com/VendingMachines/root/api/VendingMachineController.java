package com.VendingMachines.root.api;

import com.VendingMachines.root.entities.Products;
import com.VendingMachines.root.entities.VendingMachine;
import com.VendingMachines.root.model.MachineDto;
import com.VendingMachines.root.service.VendingMachineService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@ApiModel
@RequestMapping(path = "/api/vending_machines")
public class VendingMachineController {
    @Autowired
    private VendingMachineService vendingMachineService;
    ////// For tests

    @GetMapping(path = "/home")
    public String home(){
        return "Home Page";
    }
    @GetMapping(path = "/dashboard")
    public String dashboard(){
        return "Dashboard Page";
    }

////////////////////////////////
    @ApiOperation("Create Vending Machine")
    @PostMapping
    public void createMachine(@RequestBody MachineDto machine){
       vendingMachineService.addMachine(machine);
    }

    @ApiOperation("Read Vending Machine Types")
    @GetMapping
    public List<VendingMachine> readMachines(){
        return vendingMachineService.readMachines();
    }

    @ApiOperation("Read Vending Machine By Id")
    @GetMapping(path="/id={id}")
    public Optional<VendingMachine> readMachinesById(@PathVariable Long id){
        return vendingMachineService.readMachineById(id);
    }

    @ApiOperation("Read Products")
    @GetMapping(path = "/products")
    public List<Products> readProducts(){
        return vendingMachineService.readProduct();
    }

}
