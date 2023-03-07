package com.hbsmoura.videorentalshop.config.exceptionhandling;

import com.hbsmoura.videorentalshop.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler({
                    BookingNotFoundException.class,
                    ClientNotFoundException.class,
                    EmployeeNotFoundException.class,
                    MovieNotFoundException.class,
                    UserNotFoundException.class
    })
    public ResponseEntity<Object> handleResourceNotFound(Exception ex, WebRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                HttpStatus.NOT_FOUND, ex.getMessage(), ""
        );

        return handleExceptionInternal(
                ex, apiErrorResponse,
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @ExceptionHandler({
            BookingCannotBeFinalizedException.class,
            BookingCannotBeUpdatedFromRequestedException.class,
            NoSuchGenreException.class
    })
    public ResponseEntity<Object> handleBrokenRule(Exception ex, WebRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST, ex.getMessage(), ""
        );

        return handleExceptionInternal(
                ex, apiErrorResponse,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    @ExceptionHandler({
            AuthenticationException.class,
            FailedLoginException.class,
            InvalidTokenException.class,
            PasswordNotMachException.class
    })
    public ResponseEntity<Object> handleUnauthorizedRequest(Exception ex, WebRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                HttpStatus.UNAUTHORIZED, ex.getMessage(), ""
        );

        return handleExceptionInternal(
                ex, apiErrorResponse,
                new HttpHeaders(),
                HttpStatus.UNAUTHORIZED,
                request
        );
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                HttpStatus.FORBIDDEN, ex.getMessage(), ""
        );

        return handleExceptionInternal(
                ex, apiErrorResponse,
                new HttpHeaders(),
                HttpStatus.FORBIDDEN,
                request
        );
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleRemainingExceptions(Exception ex, WebRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                "Unmapped error! Please notify the support"
        );

        return handleExceptionInternal(
                ex, apiErrorResponse,
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> fieldErrors = new ArrayList<>();
        for (FieldError f : ex.getFieldErrors()) {
            fieldErrors.add(f.getField() + ": " + f.getDefaultMessage());
        }

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation error. Check 'errors' field for details.",
                fieldErrors
        );

        return this.handleExceptionInternal(
                ex, apiErrorResponse,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }
}
