package com.VendingMachines.root.services;

import com.VendingMachines.root.entities.User;
import com.VendingMachines.root.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    public User findByID(Long ID){
        return usersRepository.findById(ID).orElseThrow(()->new IllegalStateException("The user with this ID doesn't exist!"));
    }
    public User findByUsername(String username){
        List<User> users = usersRepository.findAll();
        for(User user:users){
            if(Objects.equals(user.getUsername(), username))
                return user;
        }
        return null;
    }
    public void register(String username,String password){
        usersRepository.save(new User(username,password,false));
    }
    public void delete(Long ID){
        usersRepository.deleteById(ID);
    }
    public void update(Long ID,User updatedUser){
        User oldUser = findByID(ID);
        updatedUser.setID(ID);
        usersRepository.save(updatedUser);
    }
    public List<User> getAllUsers(){
        return usersRepository.findAll();
    }

}
