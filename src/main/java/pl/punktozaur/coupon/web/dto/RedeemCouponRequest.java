package pl.punktozaur.coupon.web.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RedeemCouponRequest(@NotNull UUID loyaltyAccountId) {
}
