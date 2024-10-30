package com.nesreading.service;

import jakarta.servlet.ServletContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {
    private final ServletContext servletContext;

    public UploadService(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public String handleSaveUploadFile(MultipartFile file, String targetFolder) {
        // Don't Upload File If empty
        if (file.isEmpty()) {
            return "";
        }

        // Root path to resources/static/images
//        String rootPath = this.servletContext.getRealPath("/resources/static/images");
        String rootPath = new File("src/main/resources/static/images").getAbsolutePath();

        // Check if target folder exists within images
        if (!isValidTargetFolder(targetFolder)) {
            throw new IllegalArgumentException("Invalid target folder: " + targetFolder);
        }

        String fileName = "";
        try {
            byte[] bytes = file.getBytes();

            // Create directory if it doesn't exist
            File dir = new File(rootPath + File.separator + targetFolder);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Generate unique filename
            fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

            // Save the file to the server
            File serverFile = new File(dir.getAbsolutePath() + File.separator + fileName);
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
                stream.write(bytes);
            }

            System.out.println("File saved to: " + serverFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }

        return fileName;
    }

    // Helper method to validate the target folder
    private boolean isValidTargetFolder(String targetFolder) {
        List<String> validFolders = Arrays.asList("avatar", "default", "logo", "other", "book");
        return validFolders.contains(targetFolder);
    }

}
