package pl.punktozaur.acceptance.coupon;

import pl.punktozaur.common.LoyaltyAccountId;
import pl.punktozaur.common.LoyaltyPoints;
import pl.punktozaur.loyalty.LoyaltyFacade;

class StubLoyaltyFacade implements LoyaltyFacade {

    private boolean shouldThrow;
    
    StubLoyaltyFacade(boolean shouldThrow) {
        this.shouldThrow = shouldThrow;
    }

    @Override
    public void subtractPoints(LoyaltyAccountId loyaltyAccountId, LoyaltyPoints requiredPoints) {
        if (shouldThrow) {
            throw new RuntimeException("Stub exception for testing purposes");
        }
    }
}
