package com.VendingMachines.root.service;

import com.VendingMachines.root.entities.Money;
import com.VendingMachines.root.entities.Products;
import com.VendingMachines.root.entities.Slots;
import com.VendingMachines.root.entities.VendingMachine;
import com.VendingMachines.root.enums.MoneyType;
import com.VendingMachines.root.enums.ProductType;
import com.VendingMachines.root.enums.SlotType;
import com.VendingMachines.root.model.MachineDto;
import com.VendingMachines.root.repo.MoneyRepository;
import com.VendingMachines.root.repo.ProductsRepository;
import com.VendingMachines.root.repo.SlotsRepository;
import com.VendingMachines.root.repo.VendingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendingMachineService {
    @Autowired
    VendingMachineRepository vendingMachineRepository;
    @Autowired
    ProductsRepository productsRepository;
    @Autowired
    SlotsRepository slotsRepository;
    @Autowired
    MoneyRepository moneyRepository;

    private Products product;
    private Slots slot;
    private Money money;
    private VendingMachine vendingMachine;


    public void addMachine(MachineDto machine) {
        vendingMachine = new VendingMachine(machine.getMachineName());
        for(MoneyType moneyType : machine.getMoneyTypes()){
            money = new Money(moneyType.getCode());
            money.setMoneyStock(machine.getMoneyStock());
            vendingMachine.getMoney().add(money);
        }
        for(SlotType slotType : machine.getSlotTypes()){
            slot = new Slots(slotType.getCode());
            slot.setStock(machine.getProductStock());
            vendingMachine.getSlots().add(slot);
        }
        for(ProductType productType : machine.getProductTypes()){
            product = new Products(productType.getCode());
            vendingMachine.getProducts().add(product);
        }

        vendingMachineRepository.save(vendingMachine);

        //Optional<VendingMachine> machine2 =  vendingMachineRepository.findById(1l);
    }
    public List<VendingMachine> readMachines(){
        return vendingMachineRepository.findAll();
    }
    public Optional<VendingMachine> readMachineById(Long id){
        return vendingMachineRepository.findById(id);
    }
    public List<Products> readProduct(){
        Optional<VendingMachine> machine = vendingMachineRepository.findById(1l);

        VendingMachine x = machine.get();
        return x.getProducts();
    }
}
