package com.example.service;

import com.example.entity.FileEntity;
import com.example.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
    private final TokenService tokenService;
    private final FileRepository fileRepository;

    public FileService(TokenService tokenService, FileRepository fileRepository) {
        this.tokenService = tokenService;
        this.fileRepository = fileRepository;
    }

    public void uploadFile(String authToken, String filename, MultipartFile file) {
        // Проверяем токен аутентификации
        checkAuthToken(authToken);

        // Сохраняем информацию о файле в базе данных
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename(filename);
        fileEntity.setSize(file.getSize());
        fileRepository.save(fileEntity);
    }

    public void deleteFile(String authToken, String filename) {
        // Проверяем токен аутентификации
        checkAuthToken(authToken);

        // Удаляем информацию о файле из базы данных
        fileRepository.deleteByFilename(filename);
    }

    public FileEntity downloadFile(String authToken, String filename) {
        // Проверяем токен аутентификации
        checkAuthToken(authToken);

        // Получаем информацию о файле из базы данных
        return fileRepository.findByFilename(filename);
    }

    public void editFileName(String authToken, String filename, String newFilename) {
        // Проверяем токен аутентификации
        checkAuthToken(authToken);

        // Изменяем имя файла в базе данных
        fileRepository.updateFilename(filename, newFilename);
    }

    private void checkAuthToken(String authToken) {
        if (!tokenService.isValid(authToken)) {
            throw new RuntimeException("Недействительный токен аутентификации");
        }
    }
}
