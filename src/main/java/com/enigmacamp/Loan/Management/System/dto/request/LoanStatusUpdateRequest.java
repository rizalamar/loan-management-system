package com.enigmacamp.Loan.Management.System.dto.request;

import com.enigmacamp.Loan.Management.System.entities.Loan;
import jakarta.validation.constraints.NotNull;

public record LoanStatusUpdateRequest(
        @NotNull(message = "Status is required")
        Loan.LoanStatus status
) {
}
