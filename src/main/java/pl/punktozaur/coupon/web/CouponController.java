package pl.punktozaur.coupon.web;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.punktozaur.coupon.application.CouponService;
import pl.punktozaur.coupon.application.dto.CouponDto;
import pl.punktozaur.coupon.application.dto.CreateCouponDto;
import pl.punktozaur.coupon.application.dto.RedeemCouponDto;
import pl.punktozaur.coupon.domain.CouponId;
import pl.punktozaur.coupon.domain.NominalValue;
import pl.punktozaur.coupon.web.dto.CreateCouponRequest;
import pl.punktozaur.coupon.web.dto.RedeemCouponRequest;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<Void> createCoupon(@Valid @RequestBody CreateCouponRequest request) {
        var couponId = couponService.createCoupon(new CreateCouponDto(request.loyaltyAccountId(),
                NominalValue.valueOf(request.nominalValue().name())));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(couponId.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{id}/redeem")
    public ResponseEntity<Void> redeemCoupon(@PathVariable UUID id, @Valid @RequestBody RedeemCouponRequest request) {
        var couponId = new CouponId(id);
        couponService.redeemCoupon(couponId, new RedeemCouponDto(request.loyaltyAccountId()));

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponDto> getCoupon(@PathVariable UUID id) {
        var couponId = new CouponId(id);
        var couponDto = couponService.getCoupon(couponId);
        return ResponseEntity.ok(couponDto);
    }
}
