package com.VendingMachines.root.repo;

import com.VendingMachines.root.entities.VendingMachine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendingMachineRepository extends JpaRepository<VendingMachine,Long>{
}
