package com.enigmacamp.Loan.Management.System.config;

import jakarta.annotation.PostConstruct;
import org.apache.tomcat.util.http.fileupload.UploadContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

@Configuration
public class FileStorageConfig {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostConstruct
    public void init(){
        try{
            // Create upload directory if not exists
            Path uploadPath = Paths.get(uploadDir);
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
                System.out.println("Upload directory created:" + uploadDir );
            }
        } catch (IOException e){
            throw new RuntimeException("Could not create upload directory");
        }
    }

    public String getUploadDir(){
        return uploadDir;
    }
}
