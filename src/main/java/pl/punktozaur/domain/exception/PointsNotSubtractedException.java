package pl.punktozaur.domain.exception;


import pl.punktozaur.domain.LoyaltyAccountId;

public class PointsNotSubtractedException extends RuntimeException {

    public PointsNotSubtractedException(LoyaltyAccountId loyaltyAccountId) {
        super("Subtracting points failed for loyalty account: " + loyaltyAccountId.id());
    }
}
