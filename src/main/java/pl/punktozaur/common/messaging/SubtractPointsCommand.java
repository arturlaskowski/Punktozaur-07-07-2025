package pl.punktozaur.common.messaging;

import java.util.UUID;

public record SubtractPointsCommand(
        UUID loyaltyAccountId,
        int pointsToSubtract
) {
}
