package pl.punktozaur.loyalty;

import pl.punktozaur.common.LoyaltyAccountId;
import pl.punktozaur.common.LoyaltyPoints;

public interface LoyaltyFacade {
    void subtractPoints(LoyaltyAccountId loyaltyAccountId, LoyaltyPoints requiredPoints);
}
