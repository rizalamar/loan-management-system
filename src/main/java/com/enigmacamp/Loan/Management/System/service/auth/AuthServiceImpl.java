package com.enigmacamp.Loan.Management.System.service.auth;

import com.enigmacamp.Loan.Management.System.dto.request.LoginRequest;
import com.enigmacamp.Loan.Management.System.dto.request.RegisterRequest;
import com.enigmacamp.Loan.Management.System.dto.response.AuthResponse;
import com.enigmacamp.Loan.Management.System.entities.CustomerProfile;
import com.enigmacamp.Loan.Management.System.entities.User;
import com.enigmacamp.Loan.Management.System.exception.DuplicateResourceException;
import com.enigmacamp.Loan.Management.System.exception.ResourceNotFoundException;
import com.enigmacamp.Loan.Management.System.repository.CustomerProfileRepository;
import com.enigmacamp.Loan.Management.System.repository.UserRepository;
import com.enigmacamp.Loan.Management.System.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;


    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // 1. username validation
        if(userRepository.existsByUsername(request.username())){
            throw new DuplicateResourceException("Username already exists");
        }

        // 2. Email validation
        if(customerProfileRepository.existsByEmail(request.email())){
            throw new DuplicateResourceException("Email already exists");
        }

        // 3. Create User entity
        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(User.Role.ROLE_CUSTOMER)
                .build();
        // 4. save the user
        User savedUser = userRepository.save(user);

        // 5. Create Customer entity
        CustomerProfile customer = CustomerProfile.builder()
                .user(savedUser)
                .name(request.name())
                .email(request.email())
                .monthlyIncome(request.monthlyIncome())
                .build();

        // 6. save the customer
        customerProfileRepository.save(customer);

        // 7. Generate token
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
        String token = jwtUtil.generateToken(userDetails);

        // 8. Return response
        return new AuthResponse(
                token,
                savedUser.getUsername(),
                savedUser.getRole().name()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // 1. Authentication user (Spring Security cek username & password)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        // 2. Kalau sampai sini, berarti login berhasil
        // Load user details
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found")
                );
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getRole().name()
        );
    }
}
