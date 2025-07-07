package pl.punktozaur.loyalty.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.punktozaur.common.domain.LoyaltyAccountId;
import pl.punktozaur.common.domain.LoyaltyPoints;
import pl.punktozaur.common.messaging.SubtractPointsCommand;
import pl.punktozaur.loyalty.application.LoyaltyAccountService;

@Component
@RequiredArgsConstructor
class SubtractPointsCommandListener {

    private final LoyaltyAccountService loyaltyAccountService;

    @EventListener
    public void handle(SubtractPointsCommand command) {
        var loyaltyAccountId = new LoyaltyAccountId(command.loyaltyAccountId());
        var loyaltyPoints = new LoyaltyPoints(command.pointsToSubtract());
        loyaltyAccountService.subtractPoints(loyaltyAccountId, loyaltyPoints);
    }
}
