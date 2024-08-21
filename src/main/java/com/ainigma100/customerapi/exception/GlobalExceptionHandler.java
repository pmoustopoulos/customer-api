package com.ainigma100.customerapi.exception;

import com.ainigma100.customerapi.dto.APIResponse;
import com.ainigma100.customerapi.dto.ErrorDTO;
import com.ainigma100.customerapi.enums.Status;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({RuntimeException.class, NullPointerException.class})
    public ResponseEntity<Object> handleRuntimeExceptions(RuntimeException exception) {

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "An internal server error occurred")));

        log.error("RuntimeException or NullPointerException occurred {}", exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundExceptions(ResourceNotFoundException exception) {

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "The requested resource was not found")));

        log.error("ResourceNotFoundException occurred {}", exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({ResourceAlreadyExistException.class, DataAccessException.class})
    public ResponseEntity<Object> handleOtherExceptions(Exception exception) {

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "An error occurred while processing your request")));

        log.error("ResourceAlreadyExistException or DataAccessException occurred {}", exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "The requested URL does not support this method")));

        log.error("HttpRequestMethodNotSupportedException occurred {}", exception.getMessage());

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }


    @ExceptionHandler({MethodArgumentNotValidException.class, MissingServletRequestParameterException.class, MissingPathVariableException.class})
    public ResponseEntity<Object> handleValidationExceptions(Exception exception) {

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());

        List<ErrorDTO> errors = new ArrayList<>();
        if (exception instanceof MethodArgumentNotValidException ex) {

            ex.getBindingResult().getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.add(new ErrorDTO(fieldName, errorMessage));
            });

        } else if (exception instanceof MissingServletRequestParameterException ex) {

            String parameterName = ex.getParameterName();
            errors.add(new ErrorDTO("", "Required parameter is missing: " + parameterName));

        } else if (exception instanceof MissingPathVariableException ex) {

            String variableName = ex.getVariableName();
            errors.add(new ErrorDTO("", "Missing path variable: " + variableName));
        }

        log.error("Validation errors: {}", errors);

        response.setErrors(errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<APIResponse<ErrorDTO>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(Collections.singletonList(new ErrorDTO("", "Malformed JSON request")));

        log.error("Malformed JSON request: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIResponse<ErrorDTO>> handleConstraintViolationException(ConstraintViolationException ex) {

        List<ErrorDTO> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(new ErrorDTO(violation.getPropertyPath().toString(), violation.getMessage()));
        }

        APIResponse<ErrorDTO> response = new APIResponse<>();
        response.setStatus(Status.FAILED.getValue());
        response.setErrors(errors);

        log.error("Constraint violation errors: {}", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }



}

