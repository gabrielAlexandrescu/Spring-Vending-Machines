package com.VendingMachines.root.repositories;

import com.VendingMachines.root.entities.VendingMachine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VendingMachinesRepository extends JpaRepository<VendingMachine, UUID> {
}
