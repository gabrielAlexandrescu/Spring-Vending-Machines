package com.VendingMachines.root.repositories;

import com.VendingMachines.root.entities.VendingMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface VendingMachinesRepository extends JpaRepository<VendingMachine, UUID> {
}
