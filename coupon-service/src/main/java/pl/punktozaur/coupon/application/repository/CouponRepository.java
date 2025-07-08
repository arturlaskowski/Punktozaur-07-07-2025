package pl.punktozaur.coupon.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.punktozaur.coupon.domain.Coupon;
import pl.punktozaur.coupon.domain.CouponId;

public interface CouponRepository extends JpaRepository<Coupon, CouponId> {
}
