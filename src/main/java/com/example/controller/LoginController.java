package com.example.controller;

import com.example.dto.LoginRequest;
import com.example.dto.LoginResponse;
import com.example.handler.LoginHandler;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final LoginHandler loginHandler;

    public LoginController(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

    // Метод для авторизации пользователя
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.debug("Received login request for user: {}", loginRequest.getLogin());
        // Обрабатываем запрос на авторизацию с помощью LoginHandler
        String response = loginHandler.handleLogin(loginRequest.getLogin(), loginRequest.getPassword());
        // Создаем новый объект LoginResponse с полученным токеном
        LoginResponse loginResponse = new LoginResponse(response);
        logger.info("User logged in successfully: {}", loginRequest.getLogin());
        // Возвращаем ответ
        return ResponseEntity.ok(loginResponse);
    }
    // Метод для выхода из системы
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("auth-token") String token) {
        // Обрабатываем запрос на выход из системы с помощью LoginHandler
        loginHandler.handleLogout(token);
        logger.info("Logging out user with token: {}", token);
        return ResponseEntity.ok().build();
    }
}
