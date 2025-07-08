package pl.punktozaur.customer.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.punktozaur.customer.application.exception.CustomerAlreadyExistsException;
import pl.punktozaur.customer.application.exception.CustomerNotFoundException;
import pl.punktozaur.customer.application.exception.LoyaltyAccountCreationException;
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

    @ExceptionHandler(value = LoyaltyAccountCreationException.class)
    public ResponseEntity<ApiErrorResponse> handleException(LoyaltyAccountCreationException ex) {
        var errorResponse = new ApiErrorResponse(ex.getMessage());
        if (ex.getReason() == LoyaltyAccountCreationException.Reason.CLIENT_ERROR) {
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } else if (ex.getReason() == LoyaltyAccountCreationException.Reason.SERVER_ERROR) {
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
