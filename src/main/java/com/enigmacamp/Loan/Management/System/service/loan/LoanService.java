package com.enigmacamp.Loan.Management.System.service.loan;

import com.enigmacamp.Loan.Management.System.dto.request.LoanRequest;
import com.enigmacamp.Loan.Management.System.dto.request.LoanStatusUpdateRequest;
import com.enigmacamp.Loan.Management.System.dto.response.LoanResponse;
import com.enigmacamp.Loan.Management.System.entities.Loan;

import java.util.List;
import java.util.UUID;

public interface LoanService {
    // customer operations
    LoanResponse applyLoan(String username, LoanRequest request);
    List<LoanResponse> getMyLoans(String username);
    LoanResponse getMyLoanById(String username, UUID loanId);

    // Admin operations
    List<LoanResponse> getAllLoans();
    List<LoanResponse> getLoanByStatus(Loan.LoanStatus status);
    LoanResponse updateLoanStatus(UUID loanId, LoanStatusUpdateRequest request);
    void deleteLoan(UUID loanId);
}
