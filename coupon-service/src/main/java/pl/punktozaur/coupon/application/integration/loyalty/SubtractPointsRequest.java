package pl.punktozaur.coupon.application.integration.loyalty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

record SubtractPointsRequest(@NotNull @Min(1) int points) {
}
