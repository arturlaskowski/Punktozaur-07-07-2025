package pl.punktozaur.coupon.web.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateCouponRequest(
        @NotNull UUID loyaltyAccountId,
        @NotNull NominalValueApi nominalValue) {
}
