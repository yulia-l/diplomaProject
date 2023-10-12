package com.example.service;

import com.example.dto.FileDTO;
import com.example.entity.FileEntity;
import com.example.entity.User;
import com.example.repository.FileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ListService {
    private final Logger logger = LoggerFactory.getLogger(ListService.class);
    private final FileRepository fileRepository;
    private final TokenService tokenService;

    public ListService(FileRepository fileRepository, TokenService tokenService) {
        this.fileRepository = fileRepository;
        this.tokenService = tokenService;
    }

    public List<FileDTO> getFiles(String authToken, Integer limit) {
        logger.debug("Getting files with limit: {}", limit);
        // Получаем информацию о пользователе по токену аутентификации
        User user = tokenService.getUserByAuthToken(authToken);
        if (user == null) {
            logger.error("Unauthorized access attempt with token: {}", authToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        // Получаем список файлов, принадлежащих пользователю, из базы данных
        List<FileEntity> files = fileRepository.findAllByUserId(user.getId());
        // Ограничиваем количество возвращаемых файлов
        if (limit != null && limit < files.size()) {
            files = files.subList(0, limit);
        }
        // Преобразуем список объектов типа FileEntity в список объектов типа FileDTO
        List<FileDTO> result = files.stream()
                .map(file -> new FileDTO(file.getFilename(), file.getSize() == null ? 0 : file.getSize()))
                .collect(Collectors.toList());

        logger.debug("Files retrieved successfully: {}", result.size());
        return result;
    }
}
