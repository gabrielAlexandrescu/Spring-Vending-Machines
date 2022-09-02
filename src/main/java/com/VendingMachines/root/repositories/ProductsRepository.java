package com.VendingMachines.root.repositories;

import com.VendingMachines.root.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Product,Long> {
}
