package pl.punktozaur.customer.application.integration.loyalty;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateLoyaltyAccountRequest(
        @NotNull UUID customerId) {
}
