package com.example.handler;

import com.example.exception.BadCredentialsException;
import com.example.service.LoginService;
import org.springframework.stereotype.Service;

@Service
public class LoginHandler {
    private final LoginService loginService;

    public LoginHandler(LoginService loginService) {
        this.loginService = loginService;
    }

    public String handleLogin(String login, String password) {
        try {
            // Попытка авторизации пользователя с помощью сервиса
            // Возвращаем токен
            return loginService.login(login, password);
        } catch (BadCredentialsException e) {
            // В случае неверных учетных данных возвращаем сообщение об ошибке
            return "Неверные учетные данные";
        }
    }
    public void handleLogout(String token) {
        // Вызываем метод сервиса для удаления токена
        loginService.logout(token);
    }
}
