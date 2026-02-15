package com.enigmacamp.Loan.Management.System.controller;

import com.enigmacamp.Loan.Management.System.dto.response.CommonResponse;
import com.enigmacamp.Loan.Management.System.dto.response.CustomerProfileResponse;
import com.enigmacamp.Loan.Management.System.service.customer_profile.CustomerProfileService;
import com.enigmacamp.Loan.Management.System.service.file_storage.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;
    private final CustomerProfileService customerProfileService;

    // ========== CUSTOMER ENDPOINTS - UPLOAD ==========

    @PostMapping("/upload/ktp")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CommonResponse<CustomerProfileResponse>> uploadKtp(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("file") MultipartFile file
            ) {
        CustomerProfileResponse response = customerProfileService.uploadKtp(
                userDetails.getUsername(),
                file
        );

        return ResponseEntity.ok(
                CommonResponse.<CustomerProfileResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Ktp successfully uploaded")
                        .data(response)
                        .build()
        );
    }

    @PostMapping("/upload/salary-slip")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CommonResponse<CustomerProfileResponse>> uploadSalarySlip(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("file") MultipartFile file
    ) {
        CustomerProfileResponse response = customerProfileService.uploadSalarySlip(
                userDetails.getUsername(),
                file
        );

        return ResponseEntity.ok(
                CommonResponse.<CustomerProfileResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Salary Slip successfully uploaded")
                        .data(response)
                        .build()
        );
    }

    // ========== DOWNLOAD/VIEW ENDPOINTS ==========

    @GetMapping("/download/{fileName:.+}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String fileName,
            HttpServletRequest request
    ) {
        // 1. Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // 2. Determine file's content type
        String contentType = null;
        try{
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            // Fallback to default content type
        }

        // 3. Fallback to default content type if type could not be determined
        if(contentType == null){
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/view/{fileName:.+}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<Resource> viewFile(
            @PathVariable String fileName,
            HttpServletRequest request
    ) {
        // 1. Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // 2. Detemine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            // Fallback to default content type
        }

        // 3. Fallback to default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
