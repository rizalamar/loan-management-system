package com.enigmacamp.Loan.Management.System.service.file_storage;

import com.enigmacamp.Loan.Management.System.config.FileStorageConfig;
import com.enigmacamp.Loan.Management.System.exception.BadRequestException;
import com.enigmacamp.Loan.Management.System.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService{
    private final FileStorageConfig fileStorageConfig;

    // Upload file ke server
    @Override
    public String storeFile(MultipartFile file, String username, String fileType) {
        // 1. Validate file
        if(file.isEmpty()){
            throw new BadRequestException("File is empty");
        }

        // Validate file extension
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFilename);

        if(!isValidFileExtension(fileExtension)){
            throw new BadRequestException("Invalid file type. Only PDF, JPG, JPEG, PNG allowed");
        }

        // 3. Validate file size (max 2 MB)
        long maxFileSize = 2 * 1024 * 1024;
        if(file.getSize() > maxFileSize){
            throw new BadRequestException("File size exceeds maximum limit of 2MB");
        }

        try{
            // 4. Generate unique filename
            // Format: username_fileType_UUID.extension
            // Example: john_ktp_a9sf931.pdf
            String fileName = username + "_" + fileType + "_" + UUID.randomUUID() + fileExtension;

            // 5. Resolved file path
            Path uploadPath = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();
            Path targetLocation = uploadPath.resolve(fileName);

            // 6. Copy filename to target location (replace if exists)
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 7. Return filename (not full path, fo security)
            return fileName;
        } catch (IOException e){
            throw new RuntimeException("Could not store file. Please try again!", e);
        }
    }

    // Download/view file
    @Override
    public Resource loadFileAsResource(String fileName) {
        try{
            // 1. Resolve file path
            Path uploadPath = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();
            Path filePath = uploadPath.resolve(fileName).normalize();

            // 2. Create resource
            Resource resource = new UrlResource(filePath.toUri());

            // 3. Check if file exists
            if(resource.exists()){
                return resource;
            } else {
                throw new ResourceNotFoundException("File not found");
            }

        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("File not found" + fileName);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try{
            // 1. Resolved file path
            Path uploadFile = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();
            Path filePath = uploadFile.resolve(fileName).normalize();

            // 2. Delete file
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file: " + fileName + e);
        }
    }

    // Helper: get file extension
    private String getFileExtension(String filename){
        if(filename == null || filename.isEmpty()){
            return "";
        }

        int lastDotIndex = filename.lastIndexOf('.');
        if(lastDotIndex == -1){
            return "";
        }

        return filename.substring(lastDotIndex);
    }

    // Helper: Validate file extension
    private boolean isValidFileExtension(String extension){
        String[] allowedExtension = {".pdf", ".jpg", ".jpeg", ".png"};

        String lowerExtension = extension.toLowerCase();

        for(String allowed : allowedExtension){
            if(lowerExtension.equals(allowed)){
                return true;
            }
        }
            return false;
    }

}
