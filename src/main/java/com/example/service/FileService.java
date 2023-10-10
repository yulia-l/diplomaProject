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

    @Transactional
    public void uploadFile(String authToken, String filename, MultipartFile file) {
        logger.debug("Uploading file: {}", filename);
        // Получаем информацию о пользователе по токену аутентификации
        User user = getUserByAuthToken(authToken);
        Long userId = user.getId();

        // Сохраняем информацию о файле в базе данных
        try {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFilename(filename);
            fileEntity.setSize(file.getSize());
            fileEntity.setUserId(userId);
            fileEntity.setContentType(file.getContentType());
            // Читаем содержимое файла в массив байтов
            byte[] fileBytes = file.getBytes();
            // Сохраняем массив байтов в базе данных
            fileEntity.setData(fileBytes);
            fileRepository.save(fileEntity);
        } catch (Exception e) {
            logger.error("Error saving file: {}", filename, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка сохранения файла");
        }

        logger.info("File uploaded successfully: {}", filename);
    }

    @Transactional
    public void deleteFile(String authToken, String filename) {
        logger.debug("Deleting file: {}", filename);
        // Получаем информацию о пользователе по токену аутентификации
        getUserByAuthToken(authToken);

        // Проверяем, существует ли файл
        FileEntity file = fileRepository.findByFilename(filename);
        if (file == null) {
            logger.error("File not found: {}", filename);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Файл не найден");
        }

        // Удаляем информацию о файле из базы данных
        fileRepository.deleteByFilename(filename);
        logger.info("File deleted successfully: {}", filename);
    }

    public byte[] downloadFile(String authToken, String filename) {
        logger.debug("Downloading file: {}", filename);
        // Получаем информацию о пользователе по токену аутентификации
        getUserByAuthToken(authToken);

        // Получаем информацию о файле из базы данных
        FileEntity file = fileRepository.findByFilename(filename);
        // Получаем бинарные данные файла
        byte[] data = file.getData();

        logger.info("File downloaded successfully: {}", filename);
        return data;
    }

    @Transactional
    public void editFileName(String authToken, String filename, String newFilename) {
        logger.debug("Editing file name from {} to {}", filename, newFilename);
        // Получаем информацию о пользователе по токену аутентификации
        getUserByAuthToken(authToken);

        // Проверяем, существует ли файл
        FileEntity file = fileRepository.findByFilename(filename);
        if (file == null) {
            logger.error("File not found: {}", filename);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Файл не найден");
        }

        // Изменяем имя файла в объекте FileEntity
        file.setFilename(newFilename);

        // Сохраняем изменения в базе данных
        try {
            fileRepository.save(file);
        } catch (Exception e) {
            logger.error("Error editing file name from {} to {}", filename, newFilename, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка изменения имени файла");
        }

        logger.info("File name edited successfully from {} to {}", filename, newFilename);
    }

    private User getUserByAuthToken(String authToken) {
        User user = tokenService.getUserByAuthToken(authToken);
        if (user == null) {
            logger.error("Unauthorized access attempt with token: {}", authToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return user;
    }
}
