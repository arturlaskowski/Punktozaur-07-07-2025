package pl.punktozaur.coupon.application.exception;

import pl.punktozaur.common.LoyaltyAccountId;

public class PointsNotSubtractedException extends RuntimeException {

    public PointsNotSubtractedException(LoyaltyAccountId loyaltyAccountId) {
        super("Subtracting points failed for loyalty account: " + loyaltyAccountId.id());
    }
}
