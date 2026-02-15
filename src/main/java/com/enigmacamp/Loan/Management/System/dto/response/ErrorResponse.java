package com.enigmacamp.Loan.Management.System.dto.response;

import lombok.*;
import java.util.Map;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private Integer statusCode;
    private String message;
    private String error;
    private LocalDateTime timeStamp;
    private String path;
    private Map<String, String> validationErrors;
}
