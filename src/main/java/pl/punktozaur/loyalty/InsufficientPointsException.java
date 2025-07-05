package pl.punktozaur.loyalty;


import pl.punktozaur.common.LoyaltyAccountId;

public class InsufficientPointsException extends RuntimeException {

    public InsufficientPointsException(LoyaltyAccountId accountId) {
        super("Loyalty account doesn't have enough points. Id: " + accountId.id());
    }
}