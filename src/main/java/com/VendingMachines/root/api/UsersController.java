package com.VendingMachines.root.api;

import com.VendingMachines.root.commons.Utils;
import com.VendingMachines.root.entities.User;
import com.VendingMachines.root.enums.MoneyType;
import com.VendingMachines.root.exceptions.UserException;
import com.VendingMachines.root.model.UserDTO;
import com.VendingMachines.root.services.UserService;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EnableSwagger2
@RestController
@RequestMapping(path = "/api/users/")
public class UsersController {
    public final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getUsers() {
        List<UserDTO> users = new ArrayList<>();
        for(User user: userService.getAllUsers()){
            users.add(Utils.convertUserToDTO(user));
        }
        return users;
    }


    @PostMapping
    public void addUser(@RequestBody UserDTO userDTO) {
        userService.register(userDTO);
    }
    @PostMapping(path = "/addAdmin")
    public void addAdmin(@RequestBody UserDTO userDTO){
        userService.addAdmin(userDTO);
    }

    @PutMapping
    public void updateUser(@RequestParam UUID Id, @RequestBody User user) {
        userService.update(Id,user);
    }

    @DeleteMapping
    public void deleteUser(@RequestParam UUID Id) {
        userService.delete(Id);
    }
    @GetMapping(path="/getByID/")
    public UserDTO getUserByID(UUID ID){
        return Utils.convertUserToDTO(userService.findByID(ID));
    }
    @GetMapping(path = "/getByUsername/")
    public User getUserByUsername(@RequestParam String username){
        return userService.findByUsername(username);
    }

    @GetMapping(path = "/getAllAdmins/")
    public List<UserDTO> getAllAdmins(){
        List<UserDTO> admins = new ArrayList<>();
        for(User admin: userService.getAllAdmins()){
            admins.add(Utils.convertUserToDTO(admin));
        }
        return admins;
    }
    @PostMapping(path = "/addMoneyToUserWallet")
    public void addMoneyToWallet(@RequestParam UUID ID,@RequestParam MoneyType moneyType){
        userService.addMoneyToWallet(ID,moneyType,1);
    }
    @PostMapping(path = "/removeMoneyFromWallet")
    public void removeMoneyFromWallet(@RequestParam UUID ID,@RequestParam MoneyType moneyType) throws UserException {
        userService.removeMoneyFromWallet(ID,moneyType,1);
    }
}
