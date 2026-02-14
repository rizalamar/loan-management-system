package com.enigmacamp.Loan.Management.System.controller;

import com.enigmacamp.Loan.Management.System.dto.response.CommonResponse;
import com.enigmacamp.Loan.Management.System.dto.response.CustomerProfileResponse;
import com.enigmacamp.Loan.Management.System.service.customer_profile.CustomerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final CustomerProfileService customerProfileService;

    GetMapping("/customers")
    public ResponseEntity<CommonResponse<List<CustomerProfileResponse>>> getAllProfiles(){
        List<CustomerProfileResponse> responses = customerProfileService.getAllProfiles();
        return ResponseEntity.ok(
                CommonResponse.<List<CustomerProfileResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("All customers retrieved successfully")
                        .data(responses)
                        .build()
        );
    }
    
    @GetMapping("/customers/{id}")
    public ResponseEntity<CommonResponse<CustomerProfileResponse>> getCustomerById(
            @PathVariable UUID id
            ) {
        CustomerProfileResponse response = customerProfileService.getProfileById(id);
        return ResponseEntity.ok(
                CommonResponse.<CustomerProfileResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Customer retrieved successfully")
                        .data(response)
                        .build()
        );
    }

}
