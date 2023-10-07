package com.example.controller;

import com.example.entity.FileEntity;
import com.example.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
@RestController
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public void uploadFile(@RequestHeader("auth-token") String authToken,
                           @RequestParam("filename") String filename,
                           @RequestPart("file") MultipartFile file) {
        fileService.uploadFile(authToken, filename, file);
    }

    @DeleteMapping
    public void deleteFile(@RequestHeader("auth-token") String authToken,
                           @RequestParam("filename") String filename) {
        fileService.deleteFile(authToken, filename);
    }

    @GetMapping
    public FileEntity downloadFile(@RequestHeader("auth-token") String authToken,
                                   @RequestParam("filename") String filename) {
        return fileService.downloadFile(authToken, filename);
    }

    @PutMapping
    public void editFileName(@RequestHeader("auth-token") String authToken,
                             @RequestParam("filename") String filename,
                             @RequestBody Map<String, String> requestBody) {
        String newFilename = requestBody.get("filename");
        fileService.editFileName(authToken, filename, newFilename);
    }
}
