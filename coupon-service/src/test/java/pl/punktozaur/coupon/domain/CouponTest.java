package pl.punktozaur.coupon.domain;

import org.junit.jupiter.api.Test;
import pl.punktozaur.coupon.domain.exception.CouponNotActiveException;
import pl.punktozaur.coupon.domain.exception.UnauthorizedCouponAccessException;
import pl.punktozaur.domain.LoyaltyAccountId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CouponTest {

    private final LoyaltyAccountId loyaltyAccountId = LoyaltyAccountId.newOne();

    @Test
    void shouldCreateCoupon() {
        var nominalValue = NominalValue.TWENTY;
        var coupon = new Coupon(loyaltyAccountId, nominalValue);

        assertThat(coupon.getId()).isNotNull();
        assertThat(coupon.getLoyaltyAccountId()).isEqualTo(loyaltyAccountId);
        assertThat(coupon.getNominalValue()).isEqualTo(nominalValue);
        assertThat(coupon.getStatus()).isEqualTo(CouponStatus.PENDING);
        assertThat(coupon.isActive()).isFalse(); // PENDING coupons are not active
    }

    @Test
    void shouldRedeemCoupon() {
        var coupon = new Coupon(loyaltyAccountId, NominalValue.TWENTY);
        coupon.active();

        coupon.redeem(loyaltyAccountId);

        assertThat(coupon.getLoyaltyAccountId()).isEqualTo(loyaltyAccountId);
        assertThat(coupon.getNominalValue()).isEqualTo(NominalValue.TWENTY);
        assertThat(coupon.getStatus()).isEqualTo(CouponStatus.REDEEMED);
        assertThat(coupon.isActive()).isFalse();
    }

    @Test
    void shouldThrowUnauthorizedCouponAccessExceptionWhenRedeemedByNotOwner() {
        var coupon = new Coupon(loyaltyAccountId, NominalValue.TWENTY);
        coupon.active(); // Need to confirm first before redeeming
        var notOwnerId = LoyaltyAccountId.newOne();

        assertThrows(UnauthorizedCouponAccessException.class,
                () -> coupon.redeem(notOwnerId));
    }

    @Test
    void shouldThrowCouponNotActiveExceptionWhenRedeemingAnInactiveCoupon() {
        var coupon = new Coupon(loyaltyAccountId, NominalValue.TWENTY);
        coupon.active(); // Confirm first
        coupon.redeem(loyaltyAccountId); // Then redeem to make it inactive

        assertThrows(CouponNotActiveException.class,
                () -> coupon.redeem(loyaltyAccountId));
    }
}