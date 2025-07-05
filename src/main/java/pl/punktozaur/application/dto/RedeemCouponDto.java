package pl.punktozaur.application.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RedeemCouponDto(@NotNull UUID loyaltyAccountId) {
}
