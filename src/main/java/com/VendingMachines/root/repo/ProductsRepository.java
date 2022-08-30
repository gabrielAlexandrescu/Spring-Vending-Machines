package com.VendingMachines.root.repo;

import com.VendingMachines.root.entities.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Products,Long> {

}
