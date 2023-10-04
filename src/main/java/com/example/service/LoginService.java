package com.example.service;

import com.example.entity.User;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public LoginService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public String login(String login, String password) {
        // Проверяем учетные данные пользователя
        User user = userRepository.findByLogin(login);
        if (user == null) {
            // Создаем нового пользователя
            user = new User();
            user.setLogin(login);
            user.setPassword(password);
            userRepository.save(user);
        } else if (!password.equals(user.getPassword())) {
            throw new RuntimeException("Неверные учетные данные");
        }

        // Создаем новый токен аутентификации
        return tokenService.createToken(user.getId());
    }

    public void logout(String authToken) {
        // Удаляем токен аутентификации
        tokenService.deleteToken(authToken);
    }
}
