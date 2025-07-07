package pl.punktozaur.coupon.command;

import org.springframework.data.repository.CrudRepository;
import pl.punktozaur.coupon.domain.Coupon;
import pl.punktozaur.coupon.domain.CouponId;

public interface CouponRepository extends CrudRepository<Coupon, CouponId> {
}
