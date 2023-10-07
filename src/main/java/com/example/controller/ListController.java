package com.example.controller;

import com.example.dto.FileDTO;
import com.example.entity.FileEntity;
import com.example.service.FileService;
import com.example.service.ListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/list")
public class ListController {

    private final ListService listService;
    private FileService fileService;

    public ListController(ListService listService) {
        this.listService = listService;
        this.fileService = fileService;
    }

    @GetMapping
    public ResponseEntity<List<FileDTO>> getFiles(@RequestHeader("auth-token") String token,
                                                  @RequestParam(required = false) Integer limit) {
    // Проверяем токен аутентификации
        if (!fileService.checkAuthToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();}

        // Получаем список файлов с помощью FileService
        List<FileEntity> files = listService.getFiles(limit);
        // Преобразуем список объектов типа FileEntity в список объектов типа FileDTO
        List<FileDTO> fileDTOs = files.stream()
                .map(file -> new FileDTO(file.getFilename(), file.getSize()))
                .collect(Collectors.toList());
        // Возвращаем ответ со списком файлов
        return ResponseEntity.ok(fileDTOs);
    }
}
