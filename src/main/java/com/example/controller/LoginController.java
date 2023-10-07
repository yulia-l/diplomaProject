package com.example.controller;

import com.example.dto.LoginRequest;
import com.example.dto.LoginResponse;
import com.example.handler.LoginHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class LoginController {

    private final LoginHandler loginHandler;

    public LoginController(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

    // Метод для авторизации пользователя
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        // Обрабатываем запрос на авторизацию с помощью LoginHandler
        String response = loginHandler.handleLogin(loginRequest.getLogin(), loginRequest.getPassword());
        // Создаем новый объект LoginResponse с полученным токеном
        LoginResponse loginResponse = new LoginResponse(response);
        // Возвращаем ответ
        return ResponseEntity.ok(loginResponse);
    }

    // Метод для выхода из системы
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("auth-token") String token) {
        // Обрабатываем запрос на выход из системы с помощью LoginHandler
        loginHandler.handleLogout(token);
        return ResponseEntity.ok().build();
    }
}
