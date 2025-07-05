package pl.punktozaur.domain.exception;

import pl.punktozaur.domain.CouponId;
import pl.punktozaur.domain.LoyaltyAccountId;

public class UnauthorizedCouponAccessException extends RuntimeException {

    public UnauthorizedCouponAccessException(CouponId couponId, LoyaltyAccountId loyaltyAccountId) {
        super("This coupon with ID: " + couponId.id() + " does not belong to the loyalty accountId with ID: " + loyaltyAccountId.id() + ".");
    }
}