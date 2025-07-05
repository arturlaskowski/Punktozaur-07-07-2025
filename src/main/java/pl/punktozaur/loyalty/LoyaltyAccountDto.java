package pl.punktozaur.loyalty;

import java.util.UUID;

public record LoyaltyAccountDto(UUID id, UUID customerId, int points) {
}
