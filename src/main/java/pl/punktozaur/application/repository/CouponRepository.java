package pl.punktozaur.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.punktozaur.domain.Coupon;
import pl.punktozaur.domain.CouponId;

public interface CouponRepository extends JpaRepository<Coupon, CouponId> {
}
