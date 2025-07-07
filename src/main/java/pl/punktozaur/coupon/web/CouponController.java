package pl.punktozaur.coupon.web;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.punktozaur.common.command.CommandHandlerExecutor;
import pl.punktozaur.common.domain.LoyaltyAccountId;
import pl.punktozaur.coupon.command.create.CouponCreateCommand;
import pl.punktozaur.coupon.command.redeem.CouponRedeemCommand;
import pl.punktozaur.coupon.domain.CouponId;
import pl.punktozaur.coupon.domain.NominalValue;
import pl.punktozaur.coupon.query.CouponDto;
import pl.punktozaur.coupon.query.CouponQueryService;
import pl.punktozaur.coupon.web.dto.CreateCouponRequest;
import pl.punktozaur.coupon.web.dto.RedeemCouponRequest;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CommandHandlerExecutor commandHandlerExecutor;
    private final CouponQueryService couponQueryService;

    @PostMapping
    public ResponseEntity<Void> createCoupon(@Valid @RequestBody CreateCouponRequest request) {
        var couponId = CouponId.newOne();
        var command = new CouponCreateCommand(couponId, new LoyaltyAccountId(request.loyaltyAccountId()),
                NominalValue.valueOf(request.nominalValue().name()));

        commandHandlerExecutor.execute(command);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(couponId.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{id}/redeem")
    public ResponseEntity<Void> redeemCoupon(@PathVariable UUID id, @Valid @RequestBody RedeemCouponRequest request) {
        var command = new CouponRedeemCommand(new CouponId(id), new LoyaltyAccountId(request.loyaltyAccountId()));
        commandHandlerExecutor.execute(command);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponDto> getCoupon(@PathVariable UUID id) {
        var couponId = new CouponId(id);
        var couponDto = couponQueryService.getCoupon(couponId);
        return ResponseEntity.ok(couponDto);
    }
}
