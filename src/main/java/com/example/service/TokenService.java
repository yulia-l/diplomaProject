package com.example.service;

import com.example.entity.Token;
import com.example.repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class TokenService {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String generateToken(Long userId) {
        // Генерируем новый токен
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        String token = base64Encoder.encodeToString(randomBytes);
        // Сохраняем токен в базе данных
        tokenRepository.save(new Token(token, userId));
        return token;
    }

    public boolean isValid(String token) {
        // Проверяем наличие токена в базе данных
        return tokenRepository.existsByToken(token);
    }

    public void removeToken(String token) {
        // Удаляем токен из базы данных
        tokenRepository.deleteByToken(token);
    }

    public Long getUserIdFromAuthToken(String authToken) {
        // Ищем токен в базе данных
        Token token = tokenRepository.findByToken(authToken);
        if (token != null) {
            // Возвращаем идентификатор пользователя, связанный с токеном
            return token.getUserId();
        } else {
            // Токен не найден в базе данных
            return null;
        }
    }
}

