package pl.punktozaur.coupon.command.redeem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.punktozaur.common.command.CommandHandler;
import pl.punktozaur.coupon.command.CouponNotFoundException;
import pl.punktozaur.coupon.command.CouponRepository;


@Service
@RequiredArgsConstructor
public class CouponRedeemHandler implements CommandHandler<CouponRedeemCommand> {

    private final CouponRepository couponRepository;

    @Transactional
    public void handle(CouponRedeemCommand couponRedeemCommand) {
        var coupon = couponRepository.findById(couponRedeemCommand.couponId())
                .orElseThrow(() -> new CouponNotFoundException(couponRedeemCommand.couponId()));

        coupon.redeem(couponRedeemCommand.loyaltyAccountId());
    }
}
