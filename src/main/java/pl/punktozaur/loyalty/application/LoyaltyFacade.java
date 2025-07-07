package pl.punktozaur.loyalty.application;

import pl.punktozaur.common.domain.LoyaltyAccountId;
import pl.punktozaur.common.domain.LoyaltyPoints;
import pl.punktozaur.loyalty.application.dto.CreateLoyaltyAccountDto;

public interface LoyaltyFacade {

    LoyaltyAccountId addAccount(CreateLoyaltyAccountDto createDto);

    void subtractPoints(LoyaltyAccountId loyaltyAccountId, LoyaltyPoints requiredPoints);
}
