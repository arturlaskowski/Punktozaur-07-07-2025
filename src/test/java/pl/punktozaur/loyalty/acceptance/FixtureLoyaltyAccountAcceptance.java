package pl.punktozaur.loyalty.acceptance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.punktozaur.common.domain.LoyaltyAccountId;
import pl.punktozaur.common.domain.LoyaltyPoints;
import pl.punktozaur.loyalty.application.LoyaltyAccountService;
import pl.punktozaur.loyalty.application.dto.CreateLoyaltyAccountDto;

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
