package com.VendingMachines.root.repositories;

import com.VendingMachines.root.entities.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SlotsRepository extends JpaRepository<Slot, UUID> {
}
