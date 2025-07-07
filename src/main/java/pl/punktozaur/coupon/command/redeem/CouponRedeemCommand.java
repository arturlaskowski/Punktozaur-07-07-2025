package pl.punktozaur.coupon.command.redeem;

import jakarta.validation.constraints.NotNull;
import pl.punktozaur.common.command.Command;
import pl.punktozaur.common.domain.LoyaltyAccountId;
import pl.punktozaur.coupon.domain.CouponId;

public record CouponRedeemCommand(
        @NotNull CouponId couponId,
        @NotNull LoyaltyAccountId loyaltyAccountId) implements Command {
}
