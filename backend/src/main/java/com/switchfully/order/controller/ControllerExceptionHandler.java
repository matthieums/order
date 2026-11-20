package com.switchfully.order.controller;

import com.switchfully.order.exception.EntityNotFoundException;
import com.switchfully.order.exception.EntityNotValidException;
import com.switchfully.order.exception.NotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice(basePackages = "com.switchfully.order.controller")
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @Order(value = 10)
    public Error IllegalArgumentExceptionHandler(IllegalArgumentException exception, HttpServletRequest request) {
        return new Error(exception, BAD_REQUEST, request);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @Order(value = 10)
    public Error IllegalStateExceptionHandler(IllegalStateException exception, HttpServletRequest request) {
        return new Error(exception, BAD_REQUEST, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @Order(value = 10)
    public Error EntityNotFoundExceptionHandler(EntityNotFoundException exception, HttpServletRequest request) {
        return new Error(exception, BAD_REQUEST, request);
    }

    @ExceptionHandler(EntityNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @Order(value = 10)
    public Error badRequestHandler(EntityNotValidException exception, HttpServletRequest request) {
        return new Error(exception, BAD_REQUEST, request);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    @ResponseStatus(FORBIDDEN)
    @ResponseBody
    @Order(value = 10)
    public Error badRequestHandler(NotAuthorizedException exception, HttpServletRequest request) {
        return new Error(exception, FORBIDDEN, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ResponseBody
    @Order(value = 0)
    public Error internalErrorHandler(Exception exception, HttpServletRequest request) {
        return new Error(exception, INTERNAL_SERVER_ERROR, request);
    }

    public static class Error {

        private static final Logger LOGGER = LoggerFactory.getLogger(Error.class);

        private String uniqueErrorId;
        private String message;
        private int httpStatus;

        private Error() {
        }

        private Error(Exception exception, HttpStatus httpStatus, HttpServletRequest request) {
            uniqueErrorId = UUID.randomUUID().toString();
            this.message = getMessage(exception);
            this.httpStatus = httpStatus.value();
            logError(exception, request);
        }

        private String getMessage(Exception exception) {
            if (exception.getMessage() != null) {
                return exception.getMessage();
            } return "Something went wrong, please review your request and try again. If the error persists, " +
                    "please contact us at somethingwentwrong@mywebsite.com";
        }

        private void logError(Exception e, HttpServletRequest request) {
            LOGGER.error("REST call threw exception [" + uniqueErrorId + "] , request=" + fullURL(request), e);
        }

        private String fullURL(HttpServletRequest request) {
            return request.getMethod() + " " + request.getRequestURL() +
                    ((request.getQueryString() != null) ? "?" + request.getQueryString() : "");
        }

        public String getUniqueErrorId() {
            return uniqueErrorId;
        }

        public String getMessage() {
            return message;
        }

        public int getHttpStatus() {
            return httpStatus;
        }
    }

}
