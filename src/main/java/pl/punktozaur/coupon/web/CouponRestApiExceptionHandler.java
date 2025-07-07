package pl.punktozaur.coupon.web;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.punktozaur.common.web.ApiErrorResponse;
import pl.punktozaur.coupon.application.exception.CouponNotFoundException;
import pl.punktozaur.coupon.application.exception.PointsNotSubtractedException;
import pl.punktozaur.coupon.domain.exception.CouponNotActiveException;
import pl.punktozaur.coupon.domain.exception.UnauthorizedCouponAccessException;

@RestControllerAdvice
public class CouponRestApiExceptionHandler {

    @ExceptionHandler(value = CouponNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleException(CouponNotFoundException ex) {
        var apiErrorResponse = new ApiErrorResponse(ex.getMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = PointsNotSubtractedException.class)
    public ResponseEntity<ApiErrorResponse> handleException(PointsNotSubtractedException ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(ex.getMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CouponNotActiveException.class)
    public ResponseEntity<ApiErrorResponse> handleException(CouponNotActiveException ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(ex.getMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = UnauthorizedCouponAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleException(UnauthorizedCouponAccessException ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(ex.getMessage());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.FORBIDDEN);
    }
}
