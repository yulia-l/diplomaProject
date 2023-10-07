package com.example.service;

import com.example.entity.User;
import com.example.exception.BadCredentialsException;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class LoginService {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(TokenService tokenService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;}

    public String login(String login, String password) {
        // Ищем пользователя по логину
        User user = userRepository.findByLogin(login);
        if (user == null) {
            // Если пользователь не найден, регистрируем нового пользователя
            register(login, password);
            // Получаем идентификатор нового пользователя
            Long userId = getUserIdByLogin(login);
            // Генерируем токен и возвращаем
            return tokenService.generateToken(userId);
        } else {
            // Если пользователь найден, проверяем пароль
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException();
            }
            // Генерируем токен и возвращаем
            return tokenService.generateToken(user.getId());
        }
    }

    public void logout(String token) {
        // Удаляем токен
        tokenService.removeToken(token);
    }

    private String getPasswordHashFromDatabase(String login) {
        // Ищем пользователя по логину
        User user = userRepository.findByLogin(login);
        // Возвращаем хеш пароля пользователя
        return user.getPassword();
    }

    private Long getUserIdByLogin(String login) {
        // Ищем пользователя по логину
        User user = userRepository.findByLogin(login);
        // Возвращаем идентификатор пользователя
        return user.getId();
    }

    public void register(String login, String password) {
        // Кодируем пароль с использованием PasswordEncoder
        String encodedPassword = passwordEncoder.encode(password);
        // Создаем нового пользователя
        User user = new User();
        user.setLogin(login);
        user.setPassword(encodedPassword);
        // Сохраняем пользователя в базе данных
        userRepository.save(user);
    }
}
