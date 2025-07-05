package pl.punktozaur.domain.exception;

import pl.punktozaur.domain.CouponId;

public class CouponNotActiveException extends RuntimeException {

    public CouponNotActiveException(CouponId couponId) {
        super("Coupon with ID: " + couponId.id() + " is not active.");
    }
}