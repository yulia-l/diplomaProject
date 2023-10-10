package com.example.service;

import com.example.entity.Token;
import com.example.repository.TokenRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest {
    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private TokenService tokenService;

    @Test
    public void testGenerateTokenSuccess() {
        Long userId = 1L;

        String result = tokenService.generateToken(userId);

        verify(tokenRepository).save(any(Token.class));

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
