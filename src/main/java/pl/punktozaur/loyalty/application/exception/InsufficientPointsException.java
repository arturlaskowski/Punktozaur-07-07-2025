package pl.punktozaur.loyalty.application.exception;


import pl.punktozaur.common.domain.LoyaltyAccountId;

public class InsufficientPointsException extends RuntimeException {

    public InsufficientPointsException(LoyaltyAccountId accountId) {
        super("Loyalty account doesn't have enough points. Id: " + accountId.id());
    }
}