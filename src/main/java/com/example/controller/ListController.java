package com.example.controller;

import com.example.service.ListService;
import com.example.service.TokenService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/list")
public class ListController {

    private final ListService listService;
    private final TokenService tokenService;

    public ListController(ListService listService, TokenService tokenService) {
        this.listService = listService;
        this.tokenService = tokenService;
    }

    @GetMapping
    public List<String> getFiles(@RequestHeader("auth-token") String authToken) {
        // Проверяем токен аутентификации
        if (!tokenService.isValid(authToken)) {
            throw new RuntimeException("Недействительный токен аутентификации");
        }

        // Получаем список файлов пользователя
        return listService.getFiles(authToken);
    }
}
