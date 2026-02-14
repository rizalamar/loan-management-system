package com.enigmacamp.Loan.Management.System.controller;

import com.enigmacamp.Loan.Management.System.dto.request.LoginRequest;
import com.enigmacamp.Loan.Management.System.dto.request.RegisterRequest;
import com.enigmacamp.Loan.Management.System.dto.response.AuthResponse;
import com.enigmacamp.Loan.Management.System.dto.response.CommonResponse;
import com.enigmacamp.Loan.Management.System.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CommonResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                CommonResponse.<AuthResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Registration successful")
                        .data(response)
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
      AuthResponse response = authService.login(request);
      return ResponseEntity.ok(
              CommonResponse.<AuthResponse>builder()
                      .statusCode(HttpStatus.OK.value())
                      .message("Login successful")
                      .data(response)
                      .build()
      );
    }
}
