package com.example.handler;

import com.example.entity.FileEntity;
import com.example.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileHandler {
    private final FileService fileService;

    public FileHandler(FileService fileService) {
        this.fileService = fileService;
    }

    public void handleUploadFile(String authToken, String filename, MultipartFile file) {
        // Загружаем файл с помощью FileService
        fileService.uploadFile(authToken, filename, file);

    }

    public void handleDeleteFile(String authToken, String filename) {
        // Удаляем файл с помощью FileService
        fileService.deleteFile(authToken, filename);

    }

    public FileEntity handleDownloadFile(String authToken, String filename) {
        // Скачиваем файл с помощью FileService
        FileEntity file = fileService.downloadFile(authToken, filename);

        return file;
    }

    public void handleEditFileName(String authToken, String filename, String newFilename) {
        // Изменяем имя файла с помощью FileService
        fileService.editFileName(authToken, filename, newFilename);

    }
}
