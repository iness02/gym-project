package com.example.GymProject.exception;

import org.hibernate.HibernateException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
public class GlobalExceptionHandlerTest {
    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    private Logger logger = mock(Logger.class);

    public GlobalExceptionHandlerTest() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandler.setLogger(logger);
    }

    @Test
    public void handleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        globalExceptionHandler.handleResourceNotFoundException(ex);
        verify(logger).error("Resource not found: " + ex.getMessage(), ex);
    }

    @Test
    public void handleInvalidCredentialsException() {
        InvalidCredentialsException ex = new InvalidCredentialsException("Invalid credentials");
        globalExceptionHandler.handleInvalidCredentialsException(ex);
        verify(logger).error("Invalid credentials: " + ex.getMessage(), ex);
    }

    @Test
    public void handleHibernateException() {
        HibernateException ex = new HibernateException("Hibernate exception");
        globalExceptionHandler.handleHibernateException(ex);
        verify(logger).error("Hibernate exception: " + ex.getMessage(), ex);
    }

    @Test
    public void handleUserAlreadyRegisteredException() {
        UserAlreadyRegisteredException ex = new UserAlreadyRegisteredException("User already registered");
        globalExceptionHandler.handleUserAlreadyRegistered(ex);
        verify(logger).error("User has already registered: " + ex.getMessage(), ex);
    }

    @Test
    public void handleException() {
        Exception ex = new Exception("Generic exception");
        globalExceptionHandler.handleException(ex);
        verify(logger).error("Exception occurs:" + ex.getMessage(), ex);
    }

    @Test
    public void handleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Illegal argument");
        globalExceptionHandler.handleIllegalArgumentException(ex);
        verify(logger).error("Illegal arguments: " + ex.getMessage(), ex);
    }
}
