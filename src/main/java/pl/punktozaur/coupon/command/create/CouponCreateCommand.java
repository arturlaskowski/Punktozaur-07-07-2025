package pl.punktozaur.coupon.command.create;

import jakarta.validation.constraints.NotNull;
import pl.punktozaur.common.command.Command;
import pl.punktozaur.common.domain.LoyaltyAccountId;
import pl.punktozaur.coupon.domain.CouponId;
import pl.punktozaur.coupon.domain.NominalValue;

public record CouponCreateCommand(
        @NotNull CouponId couponId,
        @NotNull LoyaltyAccountId loyaltyAccountId,
        @NotNull NominalValue nominalValue) implements Command {
}
