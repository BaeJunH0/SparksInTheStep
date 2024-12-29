package com.sparksInTheStep.webBoard.global.errorHandling;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ProblemDetail> handleCustomException(
            HttpServletRequest request,
            CustomException exception
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                exception.getHttpStatus(),
                exception.getMessage()
        );
        problemDetail.setTitle(exception.getErrorCode());
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return new ResponseEntity<>(problemDetail, exception.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> methodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        Map<String, Object> errors = new HashMap<>();
        e.getAllErrors()
                .forEach(
                        field -> errors.put(((FieldError) field).getField(), field.getDefaultMessage()));

        problemDetail.setTitle("V000");
        problemDetail.setProperties(errors);
        return ResponseEntity.badRequest().body(problemDetail);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(
            HttpServletRequest request,
            Exception exception
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage()
        );
        problemDetail.setTitle("E999");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        log.error(exception.getMessage(), exception);

        return new ResponseEntity<>(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}