package com.enigmacamp.Loan.Management.System.service.customer_profile;

import com.enigmacamp.Loan.Management.System.dto.request.CustomerProfileUpdateRequest;
import com.enigmacamp.Loan.Management.System.dto.response.CustomerProfileResponse;
import com.enigmacamp.Loan.Management.System.entities.CustomerProfile;
import com.enigmacamp.Loan.Management.System.entities.User;
import com.enigmacamp.Loan.Management.System.repository.CustomerProfileRepository;
import com.enigmacamp.Loan.Management.System.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerProfileServiceImpl implements CustomerProfileService {

    private final UserRepository userRepository;
    private final CustomerProfileRepository customerProfileRepository;

    @Override
    public CustomerProfileResponse getMyProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new RuntimeException("User not found")
                );

        CustomerProfile profile = customerProfileRepository.findByUser(user)
                .orElseThrow(
                        () -> new RuntimeException("Customer profile not found")
                );

        return mapToResponse(profile);
    }

    @Override
    public CustomerProfileResponse updateMyProfile(String username, CustomerProfileUpdateRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new RuntimeException("User not found")
                );

        CustomerProfile profile = customerProfileRepository.findByUser(user)
                .orElseThrow(
                        () -> new RuntimeException("Customer profile not found")
                );
        // Validate email if changed
        if(!profile.getEmail().equals(request.email())){
            if(customerProfileRepository.existsByEmail(request.email())){
                throw new RuntimeException("Email already exists");
            }
        }

        profile.setName(request.name());
        profile.setEmail(request.email());
        profile.setMonthlyIncome(request.monthlyIncome());

        CustomerProfile updated = customerProfileRepository.save(profile);

        return mapToResponse(updated);
    }

    // =============== Admin ================

    @Override
    public List<CustomerProfileResponse> getAllProfiles() {
        return customerProfileRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerProfileResponse getProfileById(UUID id) {
        CustomerProfile profile = customerProfileRepository.findByUserId(id)
                .orElseThrow(
                        () -> new RuntimeException("Customer profile not found")
                );
        return mapToResponse(profile);
    }

    private CustomerProfileResponse mapToResponse(CustomerProfile profile){
        return new CustomerProfileResponse(
                profile.getId(),
                profile.getUser().getId(),
                profile.getUser().getUsername(),
                profile.getName(),
                profile.getEmail(),
                profile.getMonthlyIncome(),
                profile.getKtpPath(),
                profile.getSalarySlipPath(),
                profile.getCreateAt(),
                profile.getUpdateAt()
        );
    }
}
