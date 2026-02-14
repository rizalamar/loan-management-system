package com.enigmacamp.Loan.Management.System.dto.response;

import com.enigmacamp.Loan.Management.System.entities.User;

public record AuthResponse(
        String token,
        String username,
        User role
) {
}
