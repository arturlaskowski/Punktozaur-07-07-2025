package pl.punktozaur.coupon.domain;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public record CouponId(UUID couponId) {

    public static CouponId newOne() {
        return new CouponId(UUID.randomUUID());
    }

    public UUID id() {
        return couponId;
    }
}
