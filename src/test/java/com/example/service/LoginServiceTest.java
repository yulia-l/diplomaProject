package com.example.service;

import com.example.entity.User;
import com.example.exception.BadCredentialsException;
import com.example.exception.UserNotFoundException;
import com.example.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {
    @Mock
    private TokenService tokenService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private LoginService loginService;

    @Test
    public void testLoginSuccess() {
        String login = "testLoginSuccess";
        String password = "password";
        User user = new User();
        user.setId(1L);
        user.setLogin(login);
        user.setPassword(password);

        when(userRepository.findByLogin(login)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(tokenService.generateToken(user.getId())).thenReturn("token");

        String result = loginService.login(login, password);

        verify(userRepository).findByLogin(login);
        verify(passwordEncoder).matches(password, user.getPassword());
        verify(tokenService).generateToken(user.getId());

        assertEquals("token", result);
    }

    @Test(expected = UserNotFoundException.class)
    public void testLoginUserNotFound() {
        String login = "testLoginUserNotFound";
        String password = "password";

        when(userRepository.findByLogin(login)).thenReturn(null);

        loginService.login(login, password);
    }

    @Test(expected = BadCredentialsException.class)
    public void testLoginInvalidCredentials() {
        String login = "testLoginInvalidCredentials";
        String password = "password";
        User user = new User();
        user.setId(1L);
        user.setLogin(login);
        user.setPassword(password);

        when(userRepository.findByLogin(login)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        loginService.login(login, password);
    }
}
