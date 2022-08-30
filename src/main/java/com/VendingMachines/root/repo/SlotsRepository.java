package com.VendingMachines.root.repo;

import com.VendingMachines.root.entities.Slots;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotsRepository extends JpaRepository<Slots,Long> {
}
