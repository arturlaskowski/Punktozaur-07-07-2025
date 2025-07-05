package pl.punktozaur.coupon.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import pl.punktozaur.coupon.application.dto.CreateCouponDto;
import pl.punktozaur.coupon.application.dto.RedeemCouponDto;
import pl.punktozaur.coupon.domain.CouponId;
import pl.punktozaur.coupon.domain.NominalValue;
import pl.punktozaur.domain.LoyaltyAccountId;
import pl.punktozaur.web.ApiErrorResponse;

import static org.assertj.core.api.Assertions.assertThat;

class RedemptionCouponAcceptanceTests extends BaseAcceptanceTest {

    @Test
    @DisplayName("""
            given request to redeem coupon for valid coupon,
            when request is sent,
            then HTTP 204 status received""")
    void givenRequestToRedeemValidCoupon_whenRequestIsSent_thenHttp204Received() {
        //given
        var loyaltyAccountId = LoyaltyAccountId.newOne().id();
        var couponId = couponService.createCoupon(new CreateCouponDto(loyaltyAccountId, NominalValue.TWENTY));
        var redeemCouponRequest = new RedeemCouponDto(loyaltyAccountId);

        // when
        var response = restTemplate.exchange(
                getBaseCouponsUrl() + "/" + couponId.id() + "/redeem",
                HttpMethod.POST,
                new HttpEntity<>(redeemCouponRequest),
                Void.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        var coupon = couponService.getCoupon(couponId);
        assertThat(coupon)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", couponId.id())
                .hasFieldOrPropertyWithValue("loyaltyAccountId", loyaltyAccountId)
                .hasFieldOrPropertyWithValue("nominalValue", NominalValue.TWENTY)
                .hasFieldOrPropertyWithValue("active", false);
    }

    @Test
    @DisplayName("""
            given request to redeem coupon for non-existent coupon,
            when request is sent,
            then HTTP 404 status received""")
    void givenRequestToRedeemCouponForNonExistentCoupon_whenRequestIsSent_thenHttp404Received() {
        //given
        var loyaltyAccountId = LoyaltyAccountId.newOne().id();
        var redeemCouponRequest = new RedeemCouponDto(loyaltyAccountId);
        var nonExistentCouponId = CouponId.newOne();

        // when
        var response = restTemplate.exchange(
                getBaseCouponsUrl() + "/" + nonExistentCouponId.id() + "/redeem",
                HttpMethod.POST,
                new HttpEntity<>(redeemCouponRequest),
                ApiErrorResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .extracting("message")
                .asString()
                .contains("Coupon not found");
    }

    @Test
    @DisplayName("""
            given coupon is not active,
            when redeem attempt is made,
            then receive HTTP 409 Conflict""")
    void givenCouponIsNotActive_whenRedeemAttemptIsMade_thenReceiveHttp409Conflict() {
        //given
        var loyaltyAccountId = LoyaltyAccountId.newOne().id();
        var couponId = couponService.createCoupon(new CreateCouponDto(loyaltyAccountId, NominalValue.TWENTY));
        var redeemCouponRequest = new RedeemCouponDto(loyaltyAccountId);
        couponService.redeemCoupon(couponId, redeemCouponRequest);

        // when
        var response = restTemplate.exchange(
                getBaseCouponsUrl() + "/" + couponId.id() + "/redeem",
                HttpMethod.POST,
                new HttpEntity<>(redeemCouponRequest),
                ApiErrorResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .extracting("message")
                .asString()
                .contains("Coupon with ID: " + couponId.id() + " is not active");
    }

    @Test
    @DisplayName("""
            given coupon does not belong to the loyalty account,
            when redeem attempt is made,
            then receive HTTP 403 Forbidden""")
    void givenCouponDoesNotBelongToLoyaltyAccount_whenRedeemAttemptIsMade_thenReceiveHttp403Forbidden() {
        //given
        var ownerLoyaltyAccountId = LoyaltyAccountId.newOne().id();
        var nonOwnerLoyaltyAccountId = LoyaltyAccountId.newOne().id();
        var couponId = couponService.createCoupon(new CreateCouponDto(ownerLoyaltyAccountId, NominalValue.TWENTY));
        var redeemCouponRequest = new RedeemCouponDto(nonOwnerLoyaltyAccountId);

        // when
        var response = restTemplate.exchange(
                getBaseCouponsUrl() + "/" + couponId.id() + "/redeem",
                HttpMethod.POST,
                new HttpEntity<>(redeemCouponRequest),
                ApiErrorResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .extracting("message")
                .asString()
                .contains("This coupon with ID: " + couponId.id() + " does not belong to the loyalty account");
    }

    String getBaseCouponsUrl() {
        return "http://localhost:" + port + "/coupons";
    }
}
