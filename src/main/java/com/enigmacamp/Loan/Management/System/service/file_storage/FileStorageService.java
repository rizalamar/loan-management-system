package com.enigmacamp.Loan.Management.System.service.file_storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile file,  String username, String fileType);
    Resource loadFileAsResource(String fileName);
    void deleteFile(String fileName);
}
