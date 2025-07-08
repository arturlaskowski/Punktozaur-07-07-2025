package pl.punktozaur.coupon.application;

import pl.punktozaur.domain.LoyaltyAccountId;
import pl.punktozaur.domain.LoyaltyPoints;

public interface SubtractPointsCommandPublisher {

    void publishSubtractPointsCommand(LoyaltyAccountId loyaltyAccountId, LoyaltyPoints points);
}
