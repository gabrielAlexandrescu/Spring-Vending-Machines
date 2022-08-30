package com.VendingMachines.root.repo;

import com.VendingMachines.root.entities.Money;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneyRepository extends JpaRepository<Money,Long> {
}
