package pl.punktozaur.acceptance.loyaltyaccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.punktozaur.common.LoyaltyAccountId;
import pl.punktozaur.common.LoyaltyPoints;
import pl.punktozaur.loyalty.CreateLoyaltyAccountDto;
import pl.punktozaur.loyalty.LoyaltyAccountService;

import java.util.UUID;

@Component
class FixtureLoyaltyAccountAcceptance {

    @Autowired
    private LoyaltyAccountService loyaltyAccountService;

    UUID createLoyaltyAccountWithPoints(int points) {
        var createLoyaltyAccountDto = new CreateLoyaltyAccountDto(UUID.randomUUID());
        var accountId = loyaltyAccountService.addAccount(createLoyaltyAccountDto);
        loyaltyAccountService.addPoints(accountId, new LoyaltyPoints(points));
        return accountId.id();
    }

    UUID createLoyaltyAccount(UUID customerId, int points) {
        var createLoyaltyAccountDto = new CreateLoyaltyAccountDto(customerId);
        var accountId = loyaltyAccountService.addAccount(createLoyaltyAccountDto);
        loyaltyAccountService.addPoints(accountId, new LoyaltyPoints(points));
        return accountId.id();
    }

    int getLoyaltyAccountPoints(UUID id) {
        return loyaltyAccountService.getAccount(new LoyaltyAccountId(id)).points();
    }
}
