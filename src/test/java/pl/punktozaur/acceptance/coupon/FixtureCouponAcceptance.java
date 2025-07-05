package pl.punktozaur.acceptance.coupon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.punktozaur.application.LoyaltyAccountService;
import pl.punktozaur.application.dto.CreateLoyaltyAccountDto;
import pl.punktozaur.domain.LoyaltyAccountId;
import pl.punktozaur.domain.LoyaltyPoints;

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
