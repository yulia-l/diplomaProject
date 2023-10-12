package com.example.handler;

import com.example.exception.BadCredentialsException;
import com.example.exception.UserNotFoundException;
import com.example.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        } catch (UserNotFoundException e) {
            // Если пользователь не найден, регистрируем нового пользователя
            loginService.register(login, password);
            // Повторная попытка авторизации
            return loginService.login(login, password);
        } catch (BadCredentialsException e) {
            // В случае неверных учетных данных выбрасываем исключение
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверные учетные данные");
        }
    }

    public void handleLogout(String token) {
        // Вызываем метод сервиса для удаления токена
        loginService.logout(token);
    }
}
