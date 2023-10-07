package com.example.service;

import com.example.entity.FileEntity;
import com.example.repository.FileRepository;
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
    private final FileRepository fileRepository;

    public ListService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<FileEntity> getFiles(Integer limit) {
        // Получаем список файлов из базы данных с помощью FileRepository
        return fileRepository.findTopNById(limit);
    }
}
