package com.nexttech.coursemanagement.services;

import com.nexttech.coursemanagement.DTOs.UserLoginDTO;
import com.nexttech.coursemanagement.models.User;
import com.nexttech.coursemanagement.repositories.UserRepo;
import com.nexttech.coursemanagement.util.ApiError;
import com.nexttech.coursemanagement.util.CustomRestExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public void register(User user) {
        User existingUser = userRepo.findByUserEmail(user.getUserEmail());
        if(existingUser==null) {
            userRepo.save(user);
        } else {
            System.out.println("User is already in DB.");
        }
    }

    public boolean login(UserLoginDTO userLoginDTO) {
        //TODO: handle exceptions - wrong key, etc
        //TODO: interpret exceptions in Controller
        User loginUser = userRepo.findByUserEmail(userLoginDTO.getUserEmail());
        if (loginUser == null) {
//            System.out.println(loginUser + "exception when email is wrong");
            System.out.println("User not found - please register or check email");
            return false;
        }
        if (userLoginDTO.getUserPassword().equals(loginUser.getUserPassword())) {
            System.out.println("User login successful");
            return true;
        } else {
            System.out.println("Please check password.");
            return false;
        }
    }

    public List<User> findUsers(Optional<String> role) {
        if(role.isPresent()) {
            return userRepo.findUsersByRole(role.get());
        } else {
            List<User> userList = new ArrayList<>();
            userRepo.findAll().forEach(user -> userList.add(user));
            return userList;
        }
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent()) {
            return user.get();
        } else {
//            https://howtodoinjava.com/spring5/webmvc/controller-getmapping-postmapping/
//            throw new RecordNotFoundException();
            throw new RuntimeException();
//            return new ResponseEntity<String>("User not found", HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteUser(Long id) throws Exception {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            //TODO debug : use wrong ID
            userRepo.deleteById(id);
        } else {
            //TODO : find the correct way to throw Not found => this throws 500...not OK
//          throw new ResponseEntityExceptionHandler("Id not found");
//          Response response = givenAuth().delete(URL_PREFIX + "/api/xx");
//          ApiError error = response.as(ApiError.class);
            ApiError errorResponse = new ApiError(HttpStatus.NOT_FOUND, "id not found", "Please provide a valid id.");
//          throw errorResponse
            //create new exception handle class
        }
    }

}
