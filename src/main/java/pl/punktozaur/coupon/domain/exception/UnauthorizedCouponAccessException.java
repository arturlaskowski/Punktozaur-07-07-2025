package pl.punktozaur.coupon.domain.exception;

import pl.punktozaur.common.LoyaltyAccountId;
import pl.punktozaur.coupon.domain.CouponId;

public class UnauthorizedCouponAccessException extends RuntimeException {

    public UnauthorizedCouponAccessException(CouponId couponId, LoyaltyAccountId loyaltyAccountId) {
        super("This coupon with ID: " + couponId.id() + " does not belong to the loyalty accountId with ID: " + loyaltyAccountId.id() + ".");
    }
}