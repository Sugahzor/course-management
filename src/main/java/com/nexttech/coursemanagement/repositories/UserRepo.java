package com.nexttech.coursemanagement.repositories;

import com.nexttech.coursemanagement.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {
    User findByUserEmail(String userEmail);

    @Query("SELECT user from User user WHERE user.userRole = :userRole")
    List<User> findUsersByRole(@Param("userRole") final String role);

}
