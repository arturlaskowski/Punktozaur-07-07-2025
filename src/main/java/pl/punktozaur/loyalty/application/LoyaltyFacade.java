package pl.punktozaur.loyalty.application;

import pl.punktozaur.common.domain.LoyaltyAccountId;
import pl.punktozaur.common.domain.LoyaltyPoints;

public interface LoyaltyFacade {
    void subtractPoints(LoyaltyAccountId loyaltyAccountId, LoyaltyPoints requiredPoints);
}
