package com.kyler.lynk.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {
    @Value("${application.file.uploads.mediaoutput.path}")
    private String fileUploadPath;

    public FileService() {
    }


    public String saveFile(
            @NonNull MultipartFile sourceFile,
            @NonNull String userId) {
        final String fileUploadSubPath = "users" + File.separator + userId;
        return uploadFile(sourceFile, fileUploadSubPath);
    }

    private String uploadFile(
            @NonNull MultipartFile sourceFile,
            @NonNull String fileUploadSubPath) {
        final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;

        File targetFolder = new File(finalUploadPath);
        if (!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdirs();
            if (!folderCreated) {
                System.out.println("Failed to create folder " + targetFolder);
                return null;
            }
        }

        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath = finalUploadPath + File.separator + System.currentTimeMillis() + "." + fileExtension;
        Path targetPath = Paths.get(targetFilePath);

        try {
            Files.write(targetPath, sourceFile.getBytes());
            System.out.println("File uploaded to " + targetPath);
            return targetFilePath;
        } catch (IOException e) {
            System.out.println("File was not saved. " + e);
        }

        return null;

    }

    private String getFileExtension(String originalFileName) {
        if (originalFileName == null || originalFileName.isEmpty()) {
            return "";
        }

        int lastDotIndex = originalFileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }

        return originalFileName.substring(lastDotIndex + 1).toLowerCase();
    }
}
