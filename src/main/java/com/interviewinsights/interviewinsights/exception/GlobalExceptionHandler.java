package com.interviewinsights.interviewinsights.exception;

import com.interviewinsights.interviewinsights.dto.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handles EmailAlreadyExistsException thrown from the service
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<AuthResponse> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex) { // ex contains the exception object and its message

        // Create response object to send error details to the browser
        AuthResponse response = new AuthResponse();
        response.setSuccess(false);
        response.setMessage(ex.getMessage()); // Get the message from the thrown exception and add it to message of response object

        // Return HTTP 409 Conflict along with the response object to the browser
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    // Handles PasswordMismatchException thrown from the service
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<AuthResponse> handlePasswordMismatch(
            PasswordMismatchException ex) {

        // Create response object to send error details to browser
        AuthResponse response = new AuthResponse();
        response.setSuccess(false);
        response.setMessage(ex.getMessage()); // Get the message from the thrown exception and add it to message of response object

        // Return HTTP 400 Bad request along with response object to the browser
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // Handles RollNumberAlreadyExistsException thrown from the service
    @ExceptionHandler(RollNumberAlreadyExistsException.class)
    public ResponseEntity<AuthResponse> handleRollNumberAlreadyExists(
            RollNumberAlreadyExistsException ex) {

        AuthResponse response = new AuthResponse();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }


    // Handles UserNotFoundException thrown from the service
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<AuthResponse> handleUserNotFound(
            UserNotFoundException ex) {

        AuthResponse response = new AuthResponse();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());

        // Return HTTP 404 Not Found along with the response object to the browser
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // Handles InvalidPasswordException thrown from the service
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<AuthResponse> handleInvalidPassword(
            InvalidPasswordException ex) {

        AuthResponse response = new AuthResponse();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());

        // Return HTTP 401 Unauthorized along with the response object to the browser
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    // Handles BatchNotFoundException thrown from the service
    @ExceptionHandler(BatchNotFoundException.class)
    public ResponseEntity<AuthResponse> handleBatchNotFound(
            BatchNotFoundException ex) {

        AuthResponse response = new AuthResponse();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // Handles IllegalArgumentException thrown from the service
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AuthResponse> handleIllegalArgumentException(
            IllegalArgumentException ex) {

        AuthResponse response = new AuthResponse();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}