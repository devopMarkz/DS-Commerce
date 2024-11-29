package com.devsuperior.dscommerce.controllers.handlers;

import com.devsuperior.dscommerce.dto.CustomErrorDTO;
import com.devsuperior.dscommerce.dto.FieldMessage;
import com.devsuperior.dscommerce.dto.ValidationError;
import com.devsuperior.dscommerce.services.exceptions.DatabaseException;
import com.devsuperior.dscommerce.services.exceptions.IllegalParamTypeException;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorDTO> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        CustomErrorDTO errorDTO = new CustomErrorDTO(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(errorDTO);
    }

    @ExceptionHandler(IllegalParamTypeException.class)
    public ResponseEntity<CustomErrorDTO> illegalParamType(IllegalParamTypeException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomErrorDTO errorDTO = new CustomErrorDTO(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(errorDTO);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<CustomErrorDTO> database(DatabaseException e, HttpServletRequest request){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        CustomErrorDTO customErrorDTO = new CustomErrorDTO(Instant.now(), httpStatus.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(httpStatus).body(customErrorDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorDTO> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError errorDTO = new ValidationError(Instant.now(), status.value(), "Erro de entrada de dados", request.getRequestURI());
        for(FieldError f : e.getFieldErrors()) {
            String fieldError = f.getField();
            String message = f.getDefaultMessage();
            errorDTO.addFieldMessage(new FieldMessage(fieldError, message));
        }
        return ResponseEntity.status(status).body(errorDTO);
    }

}
