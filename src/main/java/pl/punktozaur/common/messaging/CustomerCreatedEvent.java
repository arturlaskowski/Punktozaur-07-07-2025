package pl.punktozaur.common.messaging;

import java.time.Instant;
import java.util.UUID;

public record CustomerCreatedEvent(
        UUID customerId,
        String email,
        Instant occurredAt
) {
    public CustomerCreatedEvent(UUID customerId, String email) {
        this(customerId, email, Instant.now());
    }
}