package com.example.GymProject.config;

import com.example.GymProject.security.CustomAuthFilter;
import com.example.GymProject.security.jwt.JwtAuthenticationEntryPoint;
import com.example.GymProject.security.jwt.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager,
                                                   CustomAuthFilter customAuthFilter) throws Exception {
        http
                .csrf().disable()
                .cors(httpSecurityCorsConfigurer -> {
                    UrlBasedCorsConfigurationSource source =
                            new UrlBasedCorsConfigurationSource();
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowCredentials(true);
                    config.addAllowedOrigin("http://localhost**");
                    config.setAllowedHeaders(List.of("Content-Type", "Authorization"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
                    source.registerCorsConfiguration("/**", config);
                })
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers("/api/trainees/register", "/api/trainers/register",
                                "/api/login", "/api/changePassword", "/swagger-ui/**", "/v3/api-docs/**" , "/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().flush();
                })
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}
