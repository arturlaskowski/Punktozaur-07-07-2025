package pl.punktozaur.coupon.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.punktozaur.coupon.domain.exception.CouponNotActiveException;
import pl.punktozaur.coupon.domain.exception.UnauthorizedCouponAccessException;
import pl.punktozaur.domain.LoyaltyAccountId;

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

    @NotNull
    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    @Column(length = 2000)
    private String failureMessage;

    @Version
    private int version;

    public Coupon(LoyaltyAccountId loyaltyAccountId, NominalValue nominalValue) {
        this.id = CouponId.newOne();
        this.loyaltyAccountId = loyaltyAccountId;
        this.nominalValue = nominalValue;
        this.status = CouponStatus.PENDING;
    }

    public void active() {
        if (this.status != CouponStatus.PENDING) {
            throw new IllegalStateException("Can only confirm pending coupons");
        }
        this.status = CouponStatus.ACTIVE;
    }

    public void reject(String failureMessage) {
        if (this.status != CouponStatus.PENDING) {
            throw new IllegalStateException("Can only reject pending coupons");
        }
        this.status = CouponStatus.REJECTED;
        this.failureMessage = failureMessage;
    }

    public boolean isActive() {
        return this.status == CouponStatus.ACTIVE;
    }

    public void redeem(LoyaltyAccountId loyaltyAccountId) {
        if (!isOwnedBy(loyaltyAccountId)) {
            throw new UnauthorizedCouponAccessException(this.id, loyaltyAccountId);
        }

        if (!isActive()) {
            throw new CouponNotActiveException(this.id);
        }

        this.status = CouponStatus.REDEEMED;
    }

    private boolean isOwnedBy(LoyaltyAccountId loyaltyAccountId) {
        return this.loyaltyAccountId.equals(loyaltyAccountId);
    }
}
