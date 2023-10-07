package com.example.controller;

import com.example.entity.FileEntity;
import com.example.handler.FileHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {
    private final FileHandler fileHandler;
    private final Logger logger = LoggerFactory.getLogger(FileController.class);
    public FileController(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }
    @PostMapping
    public void uploadFile(@RequestHeader("auth-token") String authToken,
                           @RequestParam("filename") String filename,
                           @RequestPart("file") MultipartFile file) {
        logger.debug("Received upload file request: {}", filename);
        fileHandler.handleUploadFile(authToken, filename, file);
        logger.info("File uploaded successfully: {}", filename);

    }

    @DeleteMapping
    public void deleteFile(@RequestHeader("auth-token") String authToken,
                           @RequestParam("filename") String filename) {
        logger.debug("Received delete file request: {}", filename);
        fileHandler.handleDeleteFile(authToken, filename);
        logger.info("File deleted successfully: {}", filename);
    }

    @GetMapping
    public FileEntity downloadFile(@RequestHeader("auth-token") String authToken,
                                   @RequestParam("filename") String filename) {
        logger.debug("Received download file request: {}", filename);
        FileEntity file = fileHandler.handleDownloadFile(authToken, filename);
        logger.info("File downloaded successfully: {}", filename);
        return file;
    }

    @PutMapping
    public void editFileName(@RequestHeader("auth-token") String authToken,
                             @RequestParam("filename") String filename,
                             @RequestBody Map<String, String> requestBody) {
        String newFilename = requestBody.get("filename");
        logger.debug("Received edit file name request: {} -> {}", filename, newFilename);
        fileHandler.handleEditFileName(authToken, filename, newFilename);
        logger.info("File name edited successfully: {} -> {}", filename, newFilename);
    }
}
