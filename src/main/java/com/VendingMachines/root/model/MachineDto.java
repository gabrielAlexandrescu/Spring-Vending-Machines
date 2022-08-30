package com.VendingMachines.root.model;

import com.VendingMachines.root.enums.MoneyType;
import com.VendingMachines.root.enums.ProductType;
import com.VendingMachines.root.enums.SlotType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MachineDto {
    private String machineName;
    private int moneyStock;
    private int productStock;
    private List<MoneyType> moneyTypes;
    private List<ProductType> productTypes;
    private List<SlotType> slotTypes;

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public int getMoneyStock() {
        return moneyStock;
    }

    public void setMoneyStock(int moneyStock) {
        this.moneyStock = moneyStock;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public List<MoneyType> getMoneyTypes() {
        return moneyTypes;
    }

    public void setMoneyTypes(List<MoneyType> moneyTypes) {
        this.moneyTypes = moneyTypes;
    }

    public List<ProductType> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(List<ProductType> productTypes) {
        this.productTypes = productTypes;
    }

    public List<SlotType> getSlotTypes() {
        return slotTypes;
    }

    public void setSlotTypes(List<SlotType> slotTypes) {
        this.slotTypes = slotTypes;
    }
}
