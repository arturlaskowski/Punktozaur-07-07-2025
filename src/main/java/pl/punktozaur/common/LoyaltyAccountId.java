package pl.punktozaur.common;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public record LoyaltyAccountId(UUID loyaltyAccountId) {

    public static LoyaltyAccountId newOne() {
        return new LoyaltyAccountId(UUID.randomUUID());
    }

    public UUID id() {
        return loyaltyAccountId;
    }
}
