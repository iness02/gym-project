package com.example.GymProject.security;

import com.example.GymProject.mapper.EntityMapper;
import com.example.GymProject.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final EntityMapper entityMapper;
    private final LoginAttemptService loginAttemptService;

    @Override
    @SneakyThrows
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        String username = request.getParameter("username");
        loginAttemptService.loginSucceeded(username);

        String jwtToken = jwtTokenProvider.generateToken(authentication);
        response.addHeader("Authorization", "Bearer " + jwtToken);
    }
}