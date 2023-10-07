package com.example.service;

import com.example.entity.FileEntity;
import com.example.entity.User;
import com.example.repository.FileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FileService {
    private final TokenService tokenService;
    private final FileRepository fileRepository;
    private final Logger logger = LoggerFactory.getLogger(FileService.class);

    public FileService(TokenService tokenService, FileRepository fileRepository) {
        this.tokenService = tokenService;
        this.fileRepository = fileRepository;
    }

    public void uploadFile(String authToken, String filename, MultipartFile file) {
        logger.info("Uploading file: {}", filename);
        // Получаем информацию о пользователе по токену аутентификации
        User user = tokenService.getUserByAuthToken(authToken);
        if (user == null) {
            logger.error("Unauthorized access attempt with token: {}", authToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Long userId = user.getId();

        // Сохраняем информацию о файле в базе данных
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFilename(filename);
        fileEntity.setSize(file.getSize());
        fileEntity.setUserId(userId);
        fileRepository.save(fileEntity);

        logger.info("File uploaded successfully: {}", filename);
    }

    @Transactional
    public void deleteFile(String authToken, String filename) {
        logger.info("Deleting file: {}", filename);
        // Получаем информацию о пользователе по токену аутентификации
        User user = tokenService.getUserByAuthToken(authToken);
        if (user == null) {
            logger.error("Unauthorized access attempt with token: {}", authToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // Удаляем информацию о файле из базы данных
        fileRepository.deleteByFilename(filename);
        logger.info("File deleted successfully: {}", filename);
    }

    public FileEntity downloadFile(String authToken, String filename) {
        logger.info("Downloading file: {}", filename);
        // Получаем информацию о пользователе по токену аутентификации
        User user = tokenService.getUserByAuthToken(authToken);
        if (user == null) {
            logger.error("Unauthorized access attempt with token: {}", authToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // Получаем информацию о файле из базы данных
        FileEntity file = fileRepository.findByFilename(filename);

        logger.info("File downloaded successfully: {}", filename);
        return file;
    }

    @Transactional
    public void editFileName(String authToken, String filename, String newFilename) {
        logger.info("Editing file name from {} to {}", filename, newFilename);
        // Получаем информацию о пользователе по токену аутентификации
        User user = tokenService.getUserByAuthToken(authToken);
        if (user == null) {
            logger.error("Unauthorized access attempt with token: {}", authToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        // Проверяем, существует ли файл
        FileEntity file = fileRepository.findByFilename(filename);
        if (file == null) {
            logger.error("File not found: {}", filename);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Файл не найден");
        }

        // Изменяем имя файла в объекте FileEntity
        file.setFilename(newFilename);

        // Сохраняем изменения в базе данных
        fileRepository.save(file);

        logger.info("File name edited successfully from {} to {}", filename, newFilename);
    }
}
