package com.nexttech.coursemanagement.models;

import com.nexttech.coursemanagement.security.SecurityConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class AppUserPrincipal implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // convert db roles to spring authorities
        List<String> roleArray = new ArrayList<>();
        roleArray.add(user.getUserRole());
        return roleArray
                .stream()
                .map(roleEntoty -> new SimpleGrantedAuthority(SecurityConfiguration.ROLE_PREFIX + roleEntoty))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getUserPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO should come from db
        //  return userEntity.getStatus() != UserStatus.CREDENTIALS_EXPIRED;
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO should come from db
        //    return userEntity.getStatus() != UserStatus.LOCKED;
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO should come from db
        //    UserStatus.CREDENTIALS_EXPIRED != userEntity.getStatus();
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO should come from db
        //    UserStatus.ACTIVE == userEntity.getStatus();
        return true;
    }
}
