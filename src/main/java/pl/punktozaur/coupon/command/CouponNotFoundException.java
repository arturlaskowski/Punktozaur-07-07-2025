package pl.punktozaur.coupon.command;

import pl.punktozaur.coupon.domain.CouponId;

public class CouponNotFoundException extends RuntimeException {

    public CouponNotFoundException(CouponId couponId) {
        super("Coupon not found with ID: " + couponId.id());
    }
}