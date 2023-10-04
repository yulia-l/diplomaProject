package com.example.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class ListService {

    public List<String> getFiles(String username) {
        // Определяем путь к директории пользователя
        Path userDir = Paths.get("/path/to/upload/dir", username);

        // Получаем список файлов в директории пользователя
        List<String> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(userDir)) {
            for (Path file : stream) {
                files.add(file.getFileName().toString());
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при получении списка файлов", e);
        }

        return files;
    }
}
