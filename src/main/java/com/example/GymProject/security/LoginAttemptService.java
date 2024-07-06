package com.example.GymProject.security;

import com.example.GymProject.model.Token;
import com.example.GymProject.model.User;
import com.example.GymProject.repository.TokenRepository;
import com.example.GymProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class LoginAttemptService {
    private static final int MAX_ATTEMPTS = 3;
    private static final int BLOCK_DURATION_MINUTES = 5;
    private final ConcurrentHashMap<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final UserRepository userRepository;

    public void loginFailed(String username) {
        int attempts = attemptsCache.getOrDefault(username, 0);
        attempts++;
        attemptsCache.put(username, attempts);
        if (attempts >= MAX_ATTEMPTS) {
            blockUser(username);
        }
    }

    public void loginSucceeded(String username) {
        attemptsCache.remove(username);
    }

    public boolean isBlocked(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        LocalDateTime lockTime = user.getLockTime();
        return lockTime != null && lockTime.plusMinutes(BLOCK_DURATION_MINUTES).isAfter(LocalDateTime.now());
    }

    private void blockUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        user.setLockTime(LocalDateTime.now());
        userRepository.save(user);
    }
}