package com.nexttech.coursemanagement.services;

import com.nexttech.coursemanagement.DTOs.UserDTO;
import com.nexttech.coursemanagement.DTOs.UserLoginDTO;
import com.nexttech.coursemanagement.models.User;
import com.nexttech.coursemanagement.repositories.UserRepo;
import com.nexttech.coursemanagement.util.BadRequestException;
import com.nexttech.coursemanagement.util.MyResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public void register(User user) throws BadRequestException{
        try {
            Assert.isNull(userRepo.findByUserEmail(user.getUserEmail()), "User already exists");
            userRepo.save(user);
        }
        catch(IllegalArgumentException exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    public void login(UserLoginDTO userLoginDTO) throws BadRequestException{
        try {
            User loginUser = userRepo.findByUserEmail(userLoginDTO.getUserEmail()); //shouldn't this throw IllegalArgumentException??
            Assert.notNull(loginUser, "Email not found.");
            if (userLoginDTO.getUserPassword().equals(loginUser.getUserPassword())) {
                System.out.println("User login successful");
            } else {
                throw new BadRequestException("Please check password.");
            }
        }
        catch(IllegalArgumentException exception) {
            System.out.println(exception.getMessage() + "IllegalArgumentException service");
            throw new BadRequestException(exception.getLocalizedMessage());
        }
        catch(BadRequestException exception) {
            throw new BadRequestException("Please check password.");
        }
    }

    public List<User> findUsers(Optional<String> role) {
        //TODO: which exceptions can occur here except DB failure?
        if(role.isPresent()) {
            return userRepo.findUsersByRole(role.get());
        } else {
            List<User> userList = new ArrayList<>();
            userRepo.findAll().forEach(user -> userList.add(user));
            return userList;
        }
    }

    public User getUserById(Long id) {
        try {
            Assert.notNull(id, "Id cannot be null");
            return userRepo.findById(id).get();
        }
        catch(IllegalArgumentException exception) {
            System.out.println("IllegalArgumentException caught ok");
            throw exception;
        }
        catch(NoSuchElementException exception) {
            System.out.println("NoSuchElementException catch: " + exception.getMessage());
            throw exception;
        }
    }

    public void deleteUser(Long id) {
        try {
            userRepo.deleteById(id);
        }
        catch(EmptyResultDataAccessException exception){
            throw new MyResourceNotFoundException("User not found");
        }
    }

    public User getUserByUserEmail(final String userEmail) {
        return userRepo.findByUserEmail(userEmail);
    }
}
