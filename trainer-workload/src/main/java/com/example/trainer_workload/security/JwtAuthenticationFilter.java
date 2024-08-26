package com.example.trainer_workload.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;


    private final JwtToPrincipalConverter jwtToPrincipalConverter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        extractTokenFromRequest(request)
                .map(token -> jwtDecoder.decode(token))
                .map(jwt -> jwtToPrincipalConverter.convert(jwt))
                .map(UserPrincipalAuthenticationToken::new)
                .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));  // Set authentication

        filterChain.doFilter(request, response);
    }

    //sa ashxatum e normal
    private Optional<String> extractTokenFromRequest(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(!StringUtils.hasText(token) || !token.startsWith("Bearer ")){
            return Optional.empty();
        }

        return Optional.of(token.substring(7));


    }
}