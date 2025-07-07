package pl.punktozaur.coupon.query;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.punktozaur.coupon.domain.Coupon;
import pl.punktozaur.coupon.domain.CouponId;

public interface CouponQueryRepository extends JpaRepository<Coupon, CouponId> {
}
