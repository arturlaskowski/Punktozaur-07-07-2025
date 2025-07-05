package pl.punktozaur.domain.exception;


import pl.punktozaur.domain.LoyaltyAccountId;

public class InsufficientPointsException extends RuntimeException {

    public InsufficientPointsException(LoyaltyAccountId accountId) {
        super("Loyalty account doesn't have enough points. Id: " + accountId.id());
    }
}