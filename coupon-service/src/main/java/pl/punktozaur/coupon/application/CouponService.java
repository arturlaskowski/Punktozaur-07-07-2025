package pl.punktozaur.coupon.application;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.punktozaur.coupon.application.dto.CouponDto;
import pl.punktozaur.coupon.application.dto.CreateCouponDto;
import pl.punktozaur.coupon.application.dto.RedeemCouponDto;
import pl.punktozaur.coupon.application.exception.CouponNotFoundException;
import pl.punktozaur.coupon.application.repository.CouponRepository;
import pl.punktozaur.coupon.domain.Coupon;
import pl.punktozaur.coupon.domain.CouponId;
import pl.punktozaur.domain.LoyaltyAccountId;

@Service
@AllArgsConstructor
@Slf4j
public class CouponService {

    private final CouponRepository couponRepository;
    private final LoyaltyCommandPublisher loyaltyCommandPublisher;

    @Transactional
    public CouponId createCoupon(CreateCouponDto dto) {
        var loyaltyAccountId = new LoyaltyAccountId(dto.loyaltyAccountId());
        var coupon = new Coupon(loyaltyAccountId, dto.nominalValue());
        var couponId = couponRepository.save(coupon).getId();
        var points = coupon.getNominalValue().getRequiredPoints();
        loyaltyCommandPublisher.publishSubtractPointsCommand(loyaltyAccountId, points);

        log.info("Coupon {} created", couponId.id());
        return couponId;
    }

    @Transactional
    public void activeCoupon(CouponId couponId) {
        var coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException(couponId));
        coupon.active();

        log.info("Coupon {} activated", couponId.id());
    }

    @Transactional
    public void redeemCoupon(CouponId couponId, RedeemCouponDto dto) {
        var coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException(couponId));
        var loyaltyAccountId = new LoyaltyAccountId(dto.loyaltyAccountId());

        coupon.redeem(loyaltyAccountId);
    }

    public CouponDto getCoupon(CouponId id) {
        var coupon = couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id));

        return new CouponDto(coupon.getId().id(), coupon.getLoyaltyAccountId().id(),
                coupon.getNominalValue(), coupon.getStatus(), coupon.getFailureMessage());
    }
}
