package pl.punktozaur.coupon.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.punktozaur.common.LoyaltyAccountId;
import pl.punktozaur.coupon.domain.exception.CouponNotActiveException;
import pl.punktozaur.coupon.domain.exception.UnauthorizedCouponAccessException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    private CouponId id;

    @NotNull
    private LoyaltyAccountId loyaltyAccountId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NominalValue nominalValue;

    private boolean active;

    @Version
    private int version;

    public Coupon(LoyaltyAccountId loyaltyAccountId, NominalValue nominalValue) {
        this.id = CouponId.newOne();
        this.loyaltyAccountId = loyaltyAccountId;
        this.nominalValue = nominalValue;
        this.active = true;
    }

    public void redeem(LoyaltyAccountId loyaltyAccountId) {
        if (!isOwnedBy(loyaltyAccountId)) {
            throw new UnauthorizedCouponAccessException(this.id, loyaltyAccountId);
        }

        if (!isActive()) {
            throw new CouponNotActiveException(this.id);
        }

        this.active = false;
    }

    private boolean isOwnedBy(LoyaltyAccountId loyaltyAccountId) {
        return this.loyaltyAccountId.equals(loyaltyAccountId);
    }
}
