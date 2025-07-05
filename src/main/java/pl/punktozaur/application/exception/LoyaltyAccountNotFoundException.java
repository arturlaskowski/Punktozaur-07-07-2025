package pl.punktozaur.application.exception;

import pl.punktozaur.domain.LoyaltyAccountId;

public class LoyaltyAccountNotFoundException extends RuntimeException {

    public LoyaltyAccountNotFoundException(LoyaltyAccountId loyaltyAccountId) {
        super("Could not find loyalty account with id: " + loyaltyAccountId.id());
    }
}
