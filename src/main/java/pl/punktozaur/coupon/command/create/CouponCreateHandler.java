package pl.punktozaur.coupon.command.create;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.punktozaur.common.command.CommandHandler;
import pl.punktozaur.coupon.command.CouponRepository;
import pl.punktozaur.coupon.domain.Coupon;
import pl.punktozaur.loyalty.application.LoyaltyFacade;


@Service
@RequiredArgsConstructor
@Slf4j
public class CouponCreateHandler implements CommandHandler<CouponCreateCommand> {

    private final CouponRepository couponRepository;
    private final LoyaltyFacade loyaltyFacade;

    @Transactional
    public void handle(CouponCreateCommand command) {
        var coupon = new Coupon(command.couponId(), command.loyaltyAccountId(), command.nominalValue());
        var requiredPoints = command.nominalValue().getRequiredPoints();

        try {
            loyaltyFacade.subtractPoints(command.loyaltyAccountId(), requiredPoints);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new PointsNotSubtractedException(command.loyaltyAccountId());
        }

        couponRepository.save(coupon);
    }
}
