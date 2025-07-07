package pl.punktozaur.customer.acceptance;


import pl.punktozaur.common.domain.LoyaltyAccountId;
import pl.punktozaur.common.domain.LoyaltyPoints;
import pl.punktozaur.loyalty.application.LoyaltyFacade;
import pl.punktozaur.loyalty.application.dto.CreateLoyaltyAccountDto;

class StubLoyaltyFacade implements LoyaltyFacade {

    @Override
    public LoyaltyAccountId addAccount(CreateLoyaltyAccountDto createDto) {
        return LoyaltyAccountId.newOne();
    }

    @Override
    public void subtractPoints(LoyaltyAccountId loyaltyAccountId, LoyaltyPoints requiredPoints) {
        //Not use in customer module.
    }
}