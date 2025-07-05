package pl.punktozaur.coupon.acceptance;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.punktozaur.coupon.application.dto.CouponDto;
import pl.punktozaur.coupon.application.dto.CreateCouponDto;
import pl.punktozaur.coupon.domain.CouponId;
import pl.punktozaur.coupon.domain.NominalValue;
import pl.punktozaur.domain.LoyaltyAccountId;
import pl.punktozaur.web.ApiErrorResponse;

import static org.assertj.core.api.Assertions.assertThat;

class GetCouponAcceptanceTest extends BaseAcceptanceTest {

    @Test
    @DisplayName("""
            given existing coupon id,
            when request is sent,
            then return coupon details and HTTP 200 status""")
    void givenExistingCouponId_whenRequestIsSent_thenCouponDetailsReturnedAndHttp200() {
        //given
        var loyaltyAccountId = LoyaltyAccountId.newOne().id();
        var couponId = couponService.createCoupon(new CreateCouponDto(loyaltyAccountId, NominalValue.TWENTY));

        //when
        var response = restTemplate.getForEntity(
                getBaseCouponsUrl() + "/" + couponId.id(), CouponDto.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", couponId.id())
                .hasFieldOrPropertyWithValue("loyaltyAccountId", loyaltyAccountId)
                .hasFieldOrPropertyWithValue("nominalValue", NominalValue.TWENTY)
                .hasFieldOrPropertyWithValue("isActive", true);
    }

    @Test
    @DisplayName("""
            given non-existing coupon id,
            when request is sent,
            then do not return coupon details and HTTP 404 status""")
    void givenNonExistingCouponId_whenRequestIsSent_thenCouponDetailsNotReturnedAndHttp404() {
        //given
        var notExistingId = CouponId.newOne();

        //when
        var response = restTemplate.getForEntity(
                getBaseCouponsUrl() + "/" + notExistingId.id(), ApiErrorResponse.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .extracting("message")
                .asString()
                .contains("Coupon not found");
    }

    String getBaseCouponsUrl() {
        return "http://localhost:" + port + "/coupons";
    }
}