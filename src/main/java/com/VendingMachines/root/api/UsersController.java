package com.VendingMachines.root.api;

import com.VendingMachines.root.entities.User;
import com.VendingMachines.root.model.UserDTO;
import com.VendingMachines.root.services.UserService;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
    public List<User> getUsers() {
        return userService.getAllUsers();
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
    public User getUserByID(UUID ID){
        return userService.findByID(ID);
    }
    @GetMapping(path = "/getByUsername/")
    public User getUserByUsername(@RequestParam String username){
        return userService.findByUsername(username);
    }
}
