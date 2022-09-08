package com.VendingMachines.root.repositories;

import com.VendingMachines.root.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ProductsRepository extends JpaRepository<Product, UUID> {
}
