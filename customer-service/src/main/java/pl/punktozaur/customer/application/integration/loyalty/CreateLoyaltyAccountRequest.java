package pl.punktozaur.customer.application.integration.loyalty;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

record CreateLoyaltyAccountRequest(
        @NotNull UUID customerId) {
}
