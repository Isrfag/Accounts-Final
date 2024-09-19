package com.microcompany.accountsservice.config;

import com.microcompany.accountsservice.exception.AccountIncorrectBalanceException;
import com.microcompany.accountsservice.exception.AccountNotFoundException;
import com.microcompany.accountsservice.exception.CustomerNotFoundException;
import com.microcompany.accountsservice.exception.GlobalException;
import com.microcompany.accountsservice.model.StatusMessage;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<StatusMessage> handleNotFoundException(GlobalException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StatusMessage(HttpStatus.NOT_FOUND.value(), "GE: Recurso no encontrado"));
    }


    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public StatusMessage handleNewProductException(AccountNotFoundException ex) {
        return new StatusMessage(HttpStatus.NOT_FOUND.value(), "Cuenta no encontrada");
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public StatusMessage handleNewProductException(CustomerNotFoundException ex) {
        return new StatusMessage(HttpStatus.NOT_FOUND.value(), "Cliente no encontrado");
    }

    @ExceptionHandler(AccountIncorrectBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public StatusMessage handleNewProductException(AccountIncorrectBalanceException ex) {
        return new StatusMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public StatusMessage handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new StatusMessage(HttpStatus.PRECONDITION_FAILED.value(), "El argumento introducido no es valido");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    StatusMessage handleConstraintViolationException(ConstraintViolationException e) {
        return new StatusMessage(HttpStatus.PRECONDITION_FAILED.value() ,"Restricción violada ");
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    StatusMessage handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException e) {
        return new StatusMessage(HttpStatus.BAD_REQUEST.value(),"La id del usuario es incorrecta");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    StatusMessage handleIllegalArgumentException(IllegalArgumentException e) {
        return new StatusMessage(HttpStatus.BAD_REQUEST.value(),"La id del usuario es incorrecta");
    }




}
