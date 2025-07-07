package pl.punktozaur.coupon.acceptance;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import pl.punktozaur.common.domain.LoyaltyAccountId;
import pl.punktozaur.common.web.ApiErrorResponse;
import pl.punktozaur.coupon.command.create.CouponCreateCommand;
import pl.punktozaur.coupon.command.create.CouponCreateHandler;
import pl.punktozaur.coupon.domain.CouponId;
import pl.punktozaur.coupon.domain.NominalValue;
import pl.punktozaur.coupon.query.CouponDto;

import static org.assertj.core.api.Assertions.assertThat;

@Import(CouponTestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GetCouponAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CouponCreateHandler couponCreateHandler;

    @Test
    @DisplayName("""
            given existing coupon id,
            when request is sent,
            then return coupon details and HTTP 200 status""")
    void givenExistingCouponId_whenRequestIsSent_thenCouponDetailsReturnedAndHttp200() {
        //given
        var couponId = CouponId.newOne();
        var loyaltyAccountId = LoyaltyAccountId.newOne();
        couponCreateHandler.handle(new CouponCreateCommand(couponId, loyaltyAccountId, NominalValue.TWENTY));

        //when
        var response = restTemplate.getForEntity(
                getBaseCouponsUrl() + "/" + couponId.id(), CouponDto.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", couponId.id())
                .hasFieldOrPropertyWithValue("loyaltyAccountId", loyaltyAccountId.id())
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