package pl.punktozaur.coupon.application.dto;


import pl.punktozaur.coupon.domain.CouponStatus;
import pl.punktozaur.coupon.domain.NominalValue;

import java.util.UUID;

public record CouponDto(UUID id, UUID loyaltyAccountId, NominalValue nominalValue, CouponStatus status, String failureMessage) {
}
