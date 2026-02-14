package com.enigmacamp.Loan.Management.System.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record LoanRequest(
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "1000000.0", message = "Minimum loan amount is 1.000.000")
        @DecimalMax(value = "1000000000.0", message = "Maximum load amount is 1.000.000.000")
        BigDecimal amount,

        @NotNull(message = "Tenor is required")
        @Min(value = 6, message = "Minimum loan tenor is 6 months")
        @Max(value = 360, message = "Maximum load tenor is 360 months")
        Integer tenorMonth,

        @NotNull(message = "Interest rate is required")
        @DecimalMin(value = "0.1", message = "Minimum interest rate is 0.1%")
        @DecimalMax(value = "100.0", message = "Maximum interest rate is 100%")
        BigDecimal interestRate
) {
}
