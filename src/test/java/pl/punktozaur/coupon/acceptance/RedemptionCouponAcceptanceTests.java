package pl.punktozaur.coupon.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import pl.punktozaur.common.domain.LoyaltyAccountId;
import pl.punktozaur.common.web.ApiErrorResponse;
import pl.punktozaur.coupon.command.create.CouponCreateCommand;
import pl.punktozaur.coupon.command.create.CouponCreateHandler;
import pl.punktozaur.coupon.command.redeem.CouponRedeemCommand;
import pl.punktozaur.coupon.command.redeem.CouponRedeemHandler;
import pl.punktozaur.coupon.domain.CouponId;
import pl.punktozaur.coupon.domain.NominalValue;
import pl.punktozaur.coupon.query.CouponQueryService;
import pl.punktozaur.coupon.web.dto.RedeemCouponRequest;

import static org.assertj.core.api.Assertions.assertThat;

@Import(CouponTestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RedemptionCouponAcceptanceTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CouponQueryService couponQueryService;

    @Autowired
    private CouponCreateHandler createCouponHandler;

    @Autowired
    private CouponRedeemHandler couponRedeemHandler;

    @Test
    @DisplayName("""
            given request to redeem coupon for valid coupon,
            when request is sent,
            then HTTP 204 status received""")
    void givenRequestToRedeemValidCoupon_whenRequestIsSent_thenHttp204Received() {
        //given
        var couponId = CouponId.newOne();
        var loyaltyAccountId = LoyaltyAccountId.newOne();
        createCouponHandler.handle(new CouponCreateCommand(couponId, loyaltyAccountId, NominalValue.TWENTY));
        var redeemCouponRequest = new RedeemCouponRequest(loyaltyAccountId.id());

        // when
        var response = restTemplate.exchange(
                getBaseCouponsUrl() + "/" + couponId.id() + "/redeem",
                HttpMethod.POST,
                new HttpEntity<>(redeemCouponRequest),
                Void.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        var coupon = couponQueryService.getCoupon(couponId);
        assertThat(coupon)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", couponId.id())
                .hasFieldOrPropertyWithValue("loyaltyAccountId", loyaltyAccountId.id())
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
        var loyaltyAccountId = LoyaltyAccountId.newOne();
        var redeemCouponRequest = new RedeemCouponRequest(loyaltyAccountId.id());
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
        var couponId = CouponId.newOne();
        var loyaltyAccountId = LoyaltyAccountId.newOne();
        createCouponHandler.handle(new CouponCreateCommand(couponId, loyaltyAccountId, NominalValue.TWENTY));
        couponRedeemHandler.handle(new CouponRedeemCommand(couponId, loyaltyAccountId));
        var redeemCouponRequest = new RedeemCouponRequest(loyaltyAccountId.id());

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
        var couponId = CouponId.newOne();
        var ownerLoyaltyAccountId = LoyaltyAccountId.newOne();
        var nonOwnerLoyaltyAccountId = LoyaltyAccountId.newOne();
        createCouponHandler.handle(new CouponCreateCommand(couponId, ownerLoyaltyAccountId, NominalValue.TWENTY));
        var redeemCouponRequest = new RedeemCouponRequest(nonOwnerLoyaltyAccountId.id());

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
