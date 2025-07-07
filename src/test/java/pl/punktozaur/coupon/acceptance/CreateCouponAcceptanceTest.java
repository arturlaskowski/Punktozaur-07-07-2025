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
import pl.punktozaur.coupon.query.CouponDto;
import pl.punktozaur.coupon.web.dto.CreateCouponRequest;
import pl.punktozaur.coupon.web.dto.NominalValueApi;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Import(CouponTestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateCouponAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StubLoyaltyFacade stubLoyaltyFacade;

    @Test
    @DisplayName("""
            given valid coupon creation request,
            when request is sent,
            then coupon is created and HTTP 201 status returned with location header""")
    void givenValidCouponCreationRequest_whenRequestIsSent_thenCouponCreatedAndHttp201Returned() {
        // given
        var loyaltyAccountId = LoyaltyAccountId.newOne().id();
        var createCouponRequest = new CreateCouponRequest(loyaltyAccountId, NominalValueApi.TWENTY);

        //when
        var postResponse = restTemplate.postForEntity(getBaseCouponsUrl(), createCouponRequest, Void.class);
        assertThat(postResponse.getHeaders().getLocation()).isNotNull();
        var couponId = postResponse.getHeaders().getLocation().getPath().split("/")[2];

        //then
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postResponse.getHeaders().getLocation()).isNotNull();

        var getResponse = restTemplate.getForEntity(postResponse.getHeaders().getLocation(), CouponDto.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", UUID.fromString(couponId))
                .hasFieldOrPropertyWithValue("loyaltyAccountId", createCouponRequest.loyaltyAccountId())
                .hasFieldOrPropertyWithValue("isActive", true)
                .extracting(CouponDto::nominalValue)
                .extracting(Enum::name)
                .isEqualTo(createCouponRequest.nominalValue().name());
    }

    @Test
    @DisplayName("""
            given points subtraction fails,
            when coupon creation request is sent,
            then HTTP 400 status returned""")
    void givenPointsSubtractionFails_whenCouponCreationRequestSent_thenHttp400Returned() {
        // given
        stubLoyaltyFacade.setShouldFail(true);
        stubLoyaltyFacade.setFailureMessage("Insufficient points");

        var loyaltyAccountId = LoyaltyAccountId.newOne().id();
        var createCouponDto = new CreateCouponRequest(loyaltyAccountId, NominalValueApi.TWENTY);

        // when
        var response = restTemplate.postForEntity(getBaseCouponsUrl(), createCouponDto, ApiErrorResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody())
                .isNotNull()
                .extracting("message")
                .asString()
                .contains("Subtracting points failed");

        // Reset stub for other tests
        stubLoyaltyFacade.setShouldFail(false);
    }

    String getBaseCouponsUrl() {
        return "http://localhost:" + port + "/coupons";
    }
}