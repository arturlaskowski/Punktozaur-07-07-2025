package pl.punktozaur.application.exception;

import pl.punktozaur.domain.CouponId;

public class CouponNotFoundException extends RuntimeException {

    public CouponNotFoundException(CouponId couponId) {
        super("Coupon not found with ID: " + couponId.id());
    }
}