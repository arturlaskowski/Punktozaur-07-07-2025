package pl.punktozaur.customer.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.punktozaur.customer.application.exception.CustomerAlreadyExistsException;
import pl.punktozaur.customer.application.exception.CustomerNotFoundException;
import pl.punktozaur.web.ApiErrorResponse;

@RestControllerAdvice
@Slf4j
public class CustomerExceptionHandler {


    @ExceptionHandler(value = CustomerNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(CustomerNotFoundException ex) {
        var errorResponse = new ApiErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = CustomerAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleException(CustomerAlreadyExistsException ex) {
        var errorResponse = new ApiErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
