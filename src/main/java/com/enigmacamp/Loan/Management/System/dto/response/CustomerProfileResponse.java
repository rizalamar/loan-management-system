package com.enigmacamp.Loan.Management.System.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerProfileResponse(
        UUID id,
        UUID userId,
        String username,
        String name,
        String email,
        BigDecimal monthlyIncome,
        String ktpPath,
        String salarySlipPath,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
