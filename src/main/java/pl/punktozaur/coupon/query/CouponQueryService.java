package pl.punktozaur.coupon.query;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.punktozaur.coupon.command.CouponNotFoundException;
import pl.punktozaur.coupon.domain.CouponId;

@Service
@AllArgsConstructor
@Slf4j
public class CouponQueryService {

    private final CouponQueryRepository couponRepository;

    public CouponDto getCoupon(CouponId id) {
        var coupon = couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id));

        return new CouponDto(coupon.getId().id(), coupon.getLoyaltyAccountId().id(),
                coupon.getNominalValue(), coupon.isActive());
    }
}
