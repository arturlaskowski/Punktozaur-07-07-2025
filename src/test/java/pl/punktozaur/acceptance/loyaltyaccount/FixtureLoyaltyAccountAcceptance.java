package pl.punktozaur.acceptance.loyaltyaccount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.punktozaur.application.LoyaltyAccountService;
import pl.punktozaur.application.dto.CreateLoyaltyAccountDto;
import pl.punktozaur.domain.LoyaltyAccountId;
import pl.punktozaur.domain.LoyaltyPoints;

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
