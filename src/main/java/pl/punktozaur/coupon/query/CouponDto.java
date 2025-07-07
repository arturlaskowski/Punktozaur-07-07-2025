package pl.punktozaur.coupon.query;


import pl.punktozaur.coupon.domain.NominalValue;

import java.util.UUID;

public record CouponDto(UUID id, UUID loyaltyAccountId, NominalValue nominalValue, boolean isActive) {
}
