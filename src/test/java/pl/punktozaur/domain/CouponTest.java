package pl.punktozaur.domain;

import org.junit.jupiter.api.Test;
import pl.punktozaur.domain.exception.CouponNotActiveException;
import pl.punktozaur.domain.exception.UnauthorizedCouponAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CouponTest {

    private final LoyaltyAccount loyaltyAccount = new LoyaltyAccount(CustomerId.newOne());

    @Test
    void shouldCreateCoupon() {
        var nominalValue = NominalValue.TWENTY;
        var coupon = new Coupon(loyaltyAccount, nominalValue);

        assertThat(coupon.getId()).isNotNull();
        assertThat(coupon.getLoyaltyAccount()).isEqualTo(loyaltyAccount);
        assertThat(coupon.getNominalValue()).isEqualTo(nominalValue);
        assertThat(coupon.isActive()).isTrue();
    }

    @Test
    void shouldRedeemCoupon() {
        var coupon = new Coupon(loyaltyAccount, NominalValue.TWENTY);

        coupon.redeem(loyaltyAccount.getId());

        assertThat(coupon.getLoyaltyAccount()).isEqualTo(loyaltyAccount);
        assertThat(coupon.getNominalValue()).isEqualTo(NominalValue.TWENTY);
        assertThat(coupon.isActive()).isFalse();
    }

    @Test
    void shouldThrowUnauthorizedCouponAccessExceptionWhenRedeemedByNotOwner() {
        var coupon = new Coupon(loyaltyAccount, NominalValue.TWENTY);
        var notOwnerId = LoyaltyAccountId.newOne();

        assertThrows(UnauthorizedCouponAccessException.class,
                () -> coupon.redeem(notOwnerId));
    }

    @Test
    void shouldThrowCouponNotActiveExceptionWhenRedeemingAnInactiveCoupon() {
        var coupon = new Coupon(loyaltyAccount, NominalValue.TWENTY);
        var loyaltyAccountID = loyaltyAccount.getId();
        coupon.redeem(loyaltyAccountID);

        assertThrows(CouponNotActiveException.class,
                () -> coupon.redeem(loyaltyAccountID));
    }
}