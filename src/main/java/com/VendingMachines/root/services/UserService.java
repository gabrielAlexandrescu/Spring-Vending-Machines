package com.VendingMachines.root.services;

import com.VendingMachines.root.entities.Product;
import com.VendingMachines.root.entities.Transaction;
import com.VendingMachines.root.entities.User;
import com.VendingMachines.root.enums.MoneyType;
import com.VendingMachines.root.exceptions.UserException;
import com.VendingMachines.root.model.MoneyDTO;
import com.VendingMachines.root.model.UserDTO;
import com.VendingMachines.root.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {
    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public User findByID(UUID ID) {
        return usersRepository.findById(ID).orElseThrow(() -> new IllegalStateException("The user with this ID doesn't exist!"));
    }

    public User findByUsername(String username) {
        List<User> users = usersRepository.findAll();
        for (User user : users) {
            if (Objects.equals(user.getUsername(), username))
                return user;
        }
        return null;
    }

    private User userDTOToEntity(UserDTO userDTO) {
        boolean isAdmin = Objects.equals(userDTO.getRole(), "admin");
        return new User(userDTO.getUsername(), userDTO.getPassword(), isAdmin);
    }

    private UserDTO userToDTO(User user) {
        boolean isAdmin = Objects.equals(user.getRole(), "admin");
        UserDTO userDTO = new UserDTO(user.getUsername(), user.getPassword(),isAdmin);
        userDTO.setID(user.getID());
        userDTO.setUserWallet(user.getUserWallet());
        return userDTO;
    }

    public void register(UserDTO userDTO) {
        User user = userDTOToEntity(userDTO);
        usersRepository.save(user);
    }

    public void addAdmin(UserDTO userDTO) {
        User user = userDTOToEntity(userDTO);
        user.setRole("admin");
        usersRepository.save(user);
    }

    public void delete(UUID ID) {
        usersRepository.deleteById(ID);
    }

    public void update(UUID ID, User updatedUser) {
        User oldUser = findByID(ID);
        oldUser.setPassword(updatedUser.getPassword());
        oldUser.setUsername(updatedUser.getUsername());
        oldUser.setRole(updatedUser.getRole());
        oldUser.setUserWallet(updatedUser.getUserWallet());
        oldUser.setTransactions(updatedUser.getTransactions());
        usersRepository.save(oldUser);
    }

    public List<User> getAllUsers() {
        return usersRepository.findAll();
    }

    public List<User> getAllAdmins() {
        List<User> users = usersRepository.findAll();
        List<User> admins = new ArrayList<>();
        for (User user : users) {
            if (Objects.equals(user.getRole(), "admin")) {
                admins.add(user);
            }
        }
        return admins;
    }

    public void addMoneyToWallet(UUID ID, MoneyType moneyType, int value) {
        User user = findByID(ID);
        List<MoneyDTO> userWallet = user.getUserWallet();
        for (MoneyDTO obj : userWallet) {
            if (obj.getMoneyType() == moneyType)
                obj.setAmount(obj.getAmount() + value);
        }
        user.setUserWallet(userWallet);
        user.setID(ID);
        usersRepository.save(user);
    }

    public void removeMoneyFromWallet(UUID ID, MoneyType moneyType, int value) throws UserException {
        User user = findByID(ID);
        List<MoneyDTO> userWallet = user.getUserWallet();
        for (MoneyDTO obj : userWallet) {
            if (obj.getMoneyType() == moneyType) {
                if (obj.getAmount() < value) {
                    throw new UserException("The user doesn't have enough money for this operation!");
                } else {
                    obj.setAmount(obj.getAmount()-value);
                }
            }
        }
        user.setUserWallet(userWallet);
        usersRepository.save(user);
    }

    public void addTransaction(UUID ID, Product product) {
        User user = findByID(ID);
        Transaction transaction = new Transaction(ID, LocalDateTime.now(), product);
        user.getTransactions().add(transaction);
        usersRepository.save(user);
    }

    public boolean logIn(String username, String password) throws UserException {
        User user = findByUsername(username);
        if (Objects.equals(user.getPassword(), password)) {
            return true;
        }
        throw new UserException("Log in failed");
    }


}
