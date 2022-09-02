package com.VendingMachines.root.repositories;

import com.VendingMachines.root.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User,Long> {
}
