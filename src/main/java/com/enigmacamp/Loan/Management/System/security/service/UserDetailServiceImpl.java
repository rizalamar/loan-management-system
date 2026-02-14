package com.enigmacamp.Loan.Management.System.security.service;

import com.enigmacamp.Loan.Management.System.entities.User;
import com.enigmacamp.Loan.Management.System.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // dipanggil saat login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find user dari database
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found " + username)
                );
        // Convert entity User ke Spring Security User Details
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // password sudah encode
                // Role user (Customer atau Admin)
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority(user.getRole().name())
                ))
                .build();
    }
}
