package pl.punktozaur.application.dto;


import pl.punktozaur.domain.NominalValue;

import java.util.UUID;

public record CouponDto(UUID id, UUID loyaltyAccountId, NominalValue nominalValue, boolean isActive) {
}
