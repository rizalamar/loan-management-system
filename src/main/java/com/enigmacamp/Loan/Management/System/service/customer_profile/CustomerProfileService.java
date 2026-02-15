package com.enigmacamp.Loan.Management.System.service.customer_profile;

import com.enigmacamp.Loan.Management.System.dto.request.CustomerProfileUpdateRequest;
import com.enigmacamp.Loan.Management.System.dto.response.CustomerProfileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface CustomerProfileService {
    CustomerProfileResponse getMyProfile(String username);
    CustomerProfileResponse updateMyProfile(String username, CustomerProfileUpdateRequest request);

    // File upload methods
    CustomerProfileResponse uploadKtp(String username, MultipartFile file);
    CustomerProfileResponse uploadSalarySlip(String username, MultipartFile file);

    List<CustomerProfileResponse> getAllProfiles(); // Admin only
    CustomerProfileResponse getProfileById(UUID id); // Admin only
}
