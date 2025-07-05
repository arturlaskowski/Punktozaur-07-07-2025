package pl.punktozaur.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.punktozaur.domain.exception.CouponNotActiveException;
import pl.punktozaur.domain.exception.UnauthorizedCouponAccessException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    private CouponId id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "loyalty_account_id")
    private LoyaltyAccount loyaltyAccount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NominalValue nominalValue;

    private boolean active;

    @Version
    private int version;

    public Coupon(LoyaltyAccount loyaltyAccount, NominalValue nominalValue) {
        this.id = CouponId.newOne();
        this.loyaltyAccount = loyaltyAccount;
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
        return this.loyaltyAccount.getId().equals(loyaltyAccountId);
    }
}
