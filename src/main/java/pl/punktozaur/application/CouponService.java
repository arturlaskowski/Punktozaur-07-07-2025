package pl.punktozaur.application;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.punktozaur.application.dto.CouponDto;
import pl.punktozaur.application.dto.CreateCouponDto;
import pl.punktozaur.application.dto.RedeemCouponDto;
import pl.punktozaur.application.exception.CouponNotFoundException;
import pl.punktozaur.application.exception.LoyaltyAccountNotFoundException;
import pl.punktozaur.application.repository.CouponRepository;
import pl.punktozaur.application.repository.LoyaltyAccountRepository;
import pl.punktozaur.domain.Coupon;
import pl.punktozaur.domain.CouponId;
import pl.punktozaur.domain.LoyaltyAccountId;
import pl.punktozaur.domain.exception.PointsNotSubtractedException;

@Service
@AllArgsConstructor
@Slf4j
public class CouponService {

    private final CouponRepository couponRepository;
    private final LoyaltyAccountService loyaltyAccountService;
    private final LoyaltyAccountRepository loyaltyAccountRepository;

    @Transactional
    public CouponId createCoupon(CreateCouponDto dto) {
        var loyaltyAccountId = new LoyaltyAccountId(dto.loyaltyAccountId());
        var loyaltyAccount = loyaltyAccountRepository.findById(loyaltyAccountId)
                .orElseThrow(() -> new PointsNotSubtractedException(loyaltyAccountId));
        var coupon = new Coupon(loyaltyAccount, dto.nominalValue());
        var requiredPoints = dto.nominalValue().getRequiredPoints();

        try {
            loyaltyAccountService.subtractPoints(loyaltyAccountId, requiredPoints);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new PointsNotSubtractedException(loyaltyAccountId);
        }
        return couponRepository.save(coupon).getId();
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

        return new CouponDto(coupon.getId().id(), coupon.getLoyaltyAccount().getId().id(),
                coupon.getNominalValue(), coupon.isActive());
    }
}
