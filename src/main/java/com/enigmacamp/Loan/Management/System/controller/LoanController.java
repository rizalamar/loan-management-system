package com.enigmacamp.Loan.Management.System.controller;

import com.enigmacamp.Loan.Management.System.dto.request.LoanRequest;
import com.enigmacamp.Loan.Management.System.dto.request.LoanStatusUpdateRequest;
import com.enigmacamp.Loan.Management.System.dto.response.CommonResponse;
import com.enigmacamp.Loan.Management.System.dto.response.LoanResponse;
import com.enigmacamp.Loan.Management.System.entities.Loan;
import com.enigmacamp.Loan.Management.System.service.loan.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    // ========== CUSTOMER ENDPOINTS ==========
    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<CommonResponse<LoanResponse>> applyLoan(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody LoanRequest request
            ) {
        LoanResponse response = loanService.applyLoan(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                CommonResponse.<LoanResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Loan application submitted successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("my-loans")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CommonResponse<List<LoanResponse>>> getMyLoans(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<LoanResponse> responses = loanService.getMyLoans(userDetails.getUsername());
        return ResponseEntity.ok(
                CommonResponse.<List<LoanResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Loan retrieved successfully")
                        .data(responses)
                        .build()
        );
    }

    @GetMapping("/my-loans/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CommonResponse<LoanResponse>> getMyLoanById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID id
            ) {
        LoanResponse response = loanService.getMyLoanById(userDetails.getUsername(), id);
        return ResponseEntity.ok(
                CommonResponse.<LoanResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Loan retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    // ========== ADMIN ENDPOINTS ==========

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<List<LoanResponse>>> getAllLoans(){
        List<LoanResponse> responses = loanService.getAllLoans();
        return ResponseEntity.ok(
                CommonResponse.<List<LoanResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("All loans retrieved successfully")
                        .data(responses)
                        .build()
        );
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<List<LoanResponse>>> getLoansByStatus(
            @PathVariable Loan.LoanStatus status
            ) {
        List<LoanResponse> responses = loanService.getLoansByStatus(status);
        return ResponseEntity.ok(
                CommonResponse.<List<LoanResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Loans filtered by status successfully")
                        .data(responses)
                        .build()
        );
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<LoanResponse>> updateLoanStatus(
            @PathVariable UUID id,
            @Valid @RequestBody LoanStatusUpdateRequest request
            ) {
        LoanResponse response = loanService.updateLoanStatus(id, request);
        return ResponseEntity.ok(
                CommonResponse.<LoanResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Loan Status updated successfully")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<Void>> deleteLoan(@PathVariable UUID id){
        loanService.deleteLoan(id);
        return ResponseEntity.ok(
                CommonResponse.<Void>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Loan deleted successfully")
                        .data(null)
                        .build()
        );
    }

}
