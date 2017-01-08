package com.bigos.awp.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bigos on 04.12.16.
 */

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String EXCEPTION_CAUSE = "ExceptionCause";

    public RestResponseEntityExceptionHandler() {
        super();
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleBadRequest(final ConstraintViolationException ex, final WebRequest request) {
        final String bodyOfResponse = "ConstraintViolationException: " + ex.getMessage() + ex.getCause().getCause().getMessage();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(EXCEPTION_CAUSE, bodyOfResponse);
        Map<String, String> map = new HashMap<>();
        map.put(EXCEPTION_CAUSE, bodyOfResponse);
        return handleExceptionInternal(ex, map, responseHeaders, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleBadRequest(final DataIntegrityViolationException ex, final WebRequest request) {
        final String bodyOfResponse = "DataIntegrityViolationException: " + ex.getMessage();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(EXCEPTION_CAUSE, bodyOfResponse);
        Map<String, String> map = new HashMap<>();
        map.put(EXCEPTION_CAUSE, bodyOfResponse);
        return handleExceptionInternal(ex, map, responseHeaders, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        String bodyOfResponse = "";
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            bodyOfResponse +=  "Field " + fieldError.getField() + ": " + fieldError.getDefaultMessage() + ".";
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(EXCEPTION_CAUSE, bodyOfResponse);
        Map<String, String> map = new HashMap<>();
        map.put(EXCEPTION_CAUSE, bodyOfResponse);
        return handleExceptionInternal(ex, map, responseHeaders, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ InvalidDataAccessApiUsageException.class, DataAccessException.class })
    protected ResponseEntity<Object> handleConflict(final RuntimeException ex, final WebRequest request) {
        final String bodyOfResponse = "Conflict in database. "  + ex.getMessage();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(EXCEPTION_CAUSE, bodyOfResponse);
        Map<String, String> map = new HashMap<>();
        map.put(EXCEPTION_CAUSE, bodyOfResponse);
        return handleExceptionInternal(ex, map, responseHeaders, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        final String bodyOfResponse = "Internal server error. " + ex.getMessage();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(EXCEPTION_CAUSE, bodyOfResponse);
        Map<String, String> map = new HashMap<>();
        map.put(EXCEPTION_CAUSE, bodyOfResponse);
        return handleExceptionInternal(ex, map, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    //my exception handlers

    @ExceptionHandler({EntityNotFoundException.class, javax.persistence.EntityNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(final RuntimeException ex, final WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        if (ex instanceof EntityNotFoundException) {
            bodyOfResponse = "User with id: " + ((EntityNotFoundException)ex).getUserId() + " not found. ";
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(EXCEPTION_CAUSE, bodyOfResponse);
        Map<String, String> map = new HashMap<>();
        map.put(EXCEPTION_CAUSE, bodyOfResponse);
        return handleExceptionInternal(ex, map, responseHeaders, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({NotUniqueNickname.class})
    public ResponseEntity<Object> handleNotUniqueNickName(final RuntimeException ex, final WebRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        String bodyOfResponse = "User with given nickname: " + ((NotUniqueNickname)ex).getNickName() +
                " already exists.";
        Map<String, String> map = new HashMap<>();
        map.put(EXCEPTION_CAUSE, bodyOfResponse);
        return handleExceptionInternal(ex, map , responseHeaders, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({UnauthorizedChangeTaskStatus.class, IncorrectUserPosition.class})
    public ResponseEntity<Object> handleNotAuthorizedChangeTaskStatus(final RuntimeException ex, final WebRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        Map<String, String> map = new HashMap<>();
        map.put(EXCEPTION_CAUSE, ex.getMessage());
        return handleExceptionInternal(ex, map , responseHeaders, HttpStatus.FORBIDDEN, request);
    }
}
