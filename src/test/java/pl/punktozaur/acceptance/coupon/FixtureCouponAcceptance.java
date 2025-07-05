package pl.punktozaur.acceptance.coupon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.punktozaur.common.LoyaltyAccountId;
import pl.punktozaur.common.LoyaltyPoints;
import pl.punktozaur.loyalty.CreateLoyaltyAccountDto;
import pl.punktozaur.loyalty.LoyaltyAccountService;

import java.util.UUID;

@Component
class FixtureCouponAcceptance {

    @Autowired
    private LoyaltyAccountService loyaltyAccountService;

    UUID createLoyaltyAccountWithPoints(LoyaltyPoints points) {
        var createLoyaltyAccountDto = new CreateLoyaltyAccountDto(UUID.randomUUID());
        var accountId = loyaltyAccountService.addAccount(createLoyaltyAccountDto);
        loyaltyAccountService.addPoints(accountId, points);
        return accountId.id();
    }

    UUID createLoyaltyAccountWithPoints() {
        return createLoyaltyAccountWithPoints(new LoyaltyPoints(10000));
    }

    int getLoyaltyAccountPoints(UUID id) {
        return loyaltyAccountService.getAccount(new LoyaltyAccountId(id)).points();
    }
}
