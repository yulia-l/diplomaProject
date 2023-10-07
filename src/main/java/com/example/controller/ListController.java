package com.example.controller;

import com.example.dto.FileDTO;
import com.example.handler.ListHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/list")
public class ListController {
    private final ListHandler listHandler;
    private final Logger logger = LoggerFactory.getLogger(ListController.class);

    public ListController(ListHandler listHandler) {
        this.listHandler = listHandler;
    }

    @GetMapping
    public ResponseEntity<List<FileDTO>> getFiles(@RequestHeader("auth-token") String token,
                                                  @RequestParam(required = false) Integer limit) {
        logger.info("Received get files request with limit: {}", limit);
        try {
            // Получаем список файлов с помощью ListHandler
            List<FileDTO> fileDTOs = listHandler.handleGetFiles(token, limit);
            // Возвращаем ответ со списком файлов
            logger.info("Files retrieved successfully: {}", fileDTOs.size());
            return ResponseEntity.ok(fileDTOs);
        } catch (ResponseStatusException e) {
            logger.error("Error retrieving files", e);
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }
}
