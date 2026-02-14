package com.enigmacamp.Loan.Management.System.service.auth;

import com.enigmacamp.Loan.Management.System.dto.request.LoginRequest;
import com.enigmacamp.Loan.Management.System.dto.request.RegisterRequest;
import com.enigmacamp.Loan.Management.System.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
