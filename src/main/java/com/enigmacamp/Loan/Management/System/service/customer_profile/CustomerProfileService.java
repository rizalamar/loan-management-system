package com.enigmacamp.Loan.Management.System.service.customer_profile;

import com.enigmacamp.Loan.Management.System.dto.request.CustomerProfileUpdateRequest;
import com.enigmacamp.Loan.Management.System.dto.response.CustomerProfileResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerProfileService {
    CustomerProfileResponse getMyProfile(String username);
    CustomerProfileResponse updateMyProfile(String username, CustomerProfileUpdateRequest request);
    List<CustomerProfileResponse> getAllProfiles(); // Admin only
    CustomerProfileResponse getProfileById(UUID id); // Admin only
}
