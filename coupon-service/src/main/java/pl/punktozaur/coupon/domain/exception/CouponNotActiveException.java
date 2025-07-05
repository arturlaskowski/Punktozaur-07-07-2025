package pl.punktozaur.coupon.domain.exception;

import pl.punktozaur.coupon.domain.CouponId;

public class CouponNotActiveException extends RuntimeException {

    public CouponNotActiveException(CouponId couponId) {
        super("Coupon with ID: " + couponId.id() + " is not active.");
    }
}