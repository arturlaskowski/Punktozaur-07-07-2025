package pl.punktozaur.loyalty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ModifyPointsRequest(@NotNull @Min(1) int points) {
}
