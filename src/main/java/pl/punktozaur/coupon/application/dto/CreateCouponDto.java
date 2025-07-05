package pl.punktozaur.coupon.application.dto;

import jakarta.validation.constraints.NotNull;
import pl.punktozaur.coupon.domain.NominalValue;

import java.util.UUID;

public record CreateCouponDto(
        @NotNull UUID loyaltyAccountId,
        @NotNull NominalValue nominalValue) {
}
