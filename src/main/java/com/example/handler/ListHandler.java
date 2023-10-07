package com.example.handler;

import com.example.dto.FileDTO;
import com.example.service.ListService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ListHandler {
    private final ListService listService;

    public ListHandler(ListService listService) {
        this.listService = listService;
    }

    public List<FileDTO> handleGetFiles(String authToken, Integer limit) {
        // Получаем список файлов с помощью ListService
        List<FileDTO> fileDTOs = listService.getFiles(authToken, limit);
        // Сортируем список файлов по размеру
        fileDTOs.sort(Comparator.comparing(FileDTO::getSize));
        return fileDTOs;
    }
}
