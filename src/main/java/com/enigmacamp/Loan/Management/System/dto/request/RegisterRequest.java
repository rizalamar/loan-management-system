package com.enigmacamp.Loan.Management.System.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record RegisterRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 6 characters")
        String password,

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be not exceed 100 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotNull(message = "Monthly Income is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Monthly income must be greater than 0")
        BigDecimal monthlyIncome
) {
}
