package com.nexttech.coursemanagement.security;

import com.nexttech.coursemanagement.models.AppUserPrincipal;
import com.nexttech.coursemanagement.models.User;
import com.nexttech.coursemanagement.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SecurityUserDetailsServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByUserName(username);
        return user
                .map(entity -> new AppUserPrincipal(entity) {
                })
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
