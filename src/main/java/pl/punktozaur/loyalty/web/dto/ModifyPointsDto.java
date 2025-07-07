package pl.punktozaur.loyalty.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ModifyPointsDto(@NotNull @Min(1) int points) {
}
