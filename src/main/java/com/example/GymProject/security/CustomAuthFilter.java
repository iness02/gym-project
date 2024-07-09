package com.example.GymProject.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthFilter extends AbstractAuthenticationProcessingFilter {
    private final LoginAttemptService loginAttemptService;

    public CustomAuthFilter(LoginAttemptService loginAttemptService,
                            @Lazy AuthenticationManager authenticationManager,
                            CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
                            CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        super(new AntPathRequestMatcher("/api/login", "POST"), authenticationManager);
        this.loginAttemptService = loginAttemptService;
        setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        setAuthenticationFailureHandler(customAuthenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("Login attempt for username: {}", username);

        if (loginAttemptService.isBlocked(username)) {
            log.info("User {} is blocked due to too many failed login attempts", username);
            throw new BadCredentialsException("User is blocked");
        }

        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            return getAuthenticationManager().authenticate(authToken);
        } catch (BadCredentialsException e) {
            loginAttemptService.loginFailed(username);
            throw e;
        }
    }
}