package com.example.controller;

import com.example.dto.FileDTO;
import com.example.service.ListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/list")
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @GetMapping
    public ResponseEntity<List<FileDTO>> getFiles(@RequestHeader("auth-token") String token,
                                                  @RequestParam(required = false) Integer limit) {
        try {
            // Получаем список файлов с помощью ListService
            List<FileDTO> fileDTOs = listService.getFiles(token, limit);
            // Возвращаем ответ со списком файлов
            return ResponseEntity.ok(fileDTOs);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }
}
