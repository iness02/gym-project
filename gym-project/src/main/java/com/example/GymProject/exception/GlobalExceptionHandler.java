package com.example.GymProject.exception;

import lombok.Setter;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Setter
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.error("Resource not found: " + ex.getMessage(), ex);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleInvalidCredentialsException(InvalidCredentialsException ex) {
        logger.error("Invalid credentials: " + ex.getMessage(), ex);
    }

    @ExceptionHandler(HibernateException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "An error occurred in the Hibernate layer")
    public void handleHibernateException(HibernateException ex) {
        logger.error("Hibernate exception: " + ex.getMessage(), ex);
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleUserAlreadyRegistered(UserAlreadyRegisteredException ex) {
        logger.error("User has already registered: " + ex.getMessage(), ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleException(Exception ex) {
        logger.error("Exception occurs:" + ex.getMessage(), ex);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Illegal arguments: " + ex.getMessage(), ex);
    }
}
