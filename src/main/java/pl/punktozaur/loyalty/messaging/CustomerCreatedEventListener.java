package pl.punktozaur.loyalty.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.punktozaur.common.messaging.CustomerCreatedEvent;
import pl.punktozaur.loyalty.application.LoyaltyAccountService;
import pl.punktozaur.loyalty.application.dto.CreateLoyaltyAccountDto;

@Component
@RequiredArgsConstructor
class CustomerCreatedEventListener {

    private final LoyaltyAccountService loyaltyAccountService;

    @EventListener
    public void handle(CustomerCreatedEvent event) {
        var createLoyaltyAccountDto = new CreateLoyaltyAccountDto(event.customerId());
        loyaltyAccountService.addAccount(createLoyaltyAccountDto);
    }
}
