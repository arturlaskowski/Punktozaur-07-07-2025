package pl.punktozaur.loyalty;

import pl.punktozaur.common.LoyaltyAccountId;

public class LoyaltyAccountNotFoundException extends RuntimeException {

    public LoyaltyAccountNotFoundException(LoyaltyAccountId loyaltyAccountId) {
        super("Could not find loyalty account with id: " + loyaltyAccountId.id());
    }
}
