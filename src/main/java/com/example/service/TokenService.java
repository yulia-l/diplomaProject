package com.example.service;

import com.example.entity.Token;
import com.example.entity.User;
import com.example.repository.TokenRepository;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TokenService {
    private final Logger logger = LoggerFactory.getLogger(TokenService.class);
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public TokenService(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public String generateToken(Long userId) {
        // Генерируем новый токен
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        String token = base64Encoder.encodeToString(randomBytes);
        // Сохраняем токен в базе данных
        tokenRepository.save(new Token(token, userId));
        logger.info("Token generated successfully for user: {}", userId);
        return token;
    }

    public void removeToken(String token) {
        logger.debug("Removing token: {}", token);
        // Удаляем токен из базы данных
        tokenRepository.deleteByToken(token);
        logger.info("Token removed successfully");
    }

    public User getUserByAuthToken(String authToken) {
        logger.debug("Getting user by auth token: {}", authToken);
        // Проверяем, начинается ли токен с "Bearer "
        if (authToken.startsWith("Bearer ")) {
            // Удаляем "Bearer " из токена
            String authTokenWithoutBearer = authToken.substring(7);
            // Проверяем, существует ли токен в базе данных
            if (tokenRepository.existsByToken(authTokenWithoutBearer)) {
                // Получаем информацию о токене из базы данных
                Token token = tokenRepository.findByToken(authTokenWithoutBearer);
                // Возвращаем информацию о пользователе, связанную с токеном
                User user = userRepository.findById(token.getUserId()).orElse(null);
                logger.info("User retrieved successfully by auth token: {}", user != null ? user.getId() : null);
                return user;
            }
        }
        // Токен недействителен, возвращаем null
        logger.error("Invalid auth token: {}", authToken);
        return null;
    }
}

