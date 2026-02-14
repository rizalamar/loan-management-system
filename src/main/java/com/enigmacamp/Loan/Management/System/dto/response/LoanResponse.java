package com.enigmacamp.Loan.Management.System.dto.response;

import com.enigmacamp.Loan.Management.System.entities.Loan;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record LoanResponse(
        UUID id,
        UUID customerId,
        String customerName,
        String customerEmail,
        BigDecimal amount,
        Integer tenorMonths,
        BigDecimal interestRate,
        Loan.LoanStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
