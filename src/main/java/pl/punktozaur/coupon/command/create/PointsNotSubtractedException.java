package pl.punktozaur.coupon.command.create;

import pl.punktozaur.common.domain.LoyaltyAccountId;

public class PointsNotSubtractedException extends RuntimeException {

    public PointsNotSubtractedException(LoyaltyAccountId loyaltyAccountId) {
        super("Subtracting points failed for loyalty account: " + loyaltyAccountId.id());
    }
}
