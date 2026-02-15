package com.enigmacamp.Loan.Management.System.controller;

import com.enigmacamp.Loan.Management.System.dto.request.CustomerProfileUpdateRequest;
import com.enigmacamp.Loan.Management.System.dto.response.CommonResponse;
import com.enigmacamp.Loan.Management.System.dto.response.CustomerProfileResponse;
import com.enigmacamp.Loan.Management.System.service.customer_profile.CustomerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerProfileService customerProfileService;

    @GetMapping("/profile")
    public ResponseEntity<CommonResponse<CustomerProfileResponse>> getMyProfile(
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        CustomerProfileResponse response = customerProfileService.getMyProfile(userDetails.getUsername());
        return ResponseEntity.ok(
                CommonResponse.<CustomerProfileResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Profile retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<CommonResponse<CustomerProfileResponse>> updateMyProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CustomerProfileUpdateRequest request
            ) {
        CustomerProfileResponse response = customerProfileService.updateMyProfile(
                userDetails.getUsername(),
                request
        );

        return ResponseEntity.ok(
                CommonResponse.<CustomerProfileResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Profile updated successfully")
                        .data(response)
                        .build()
        );
    }
}
