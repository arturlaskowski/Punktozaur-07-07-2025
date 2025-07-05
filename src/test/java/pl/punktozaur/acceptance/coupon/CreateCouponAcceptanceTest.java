package pl.punktozaur.acceptance.coupon;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import pl.punktozaur.application.dto.CouponDto;
import pl.punktozaur.domain.LoyaltyPoints;
import pl.punktozaur.domain.NominalValue;
import pl.punktozaur.web.dto.CreateCouponRequest;
import pl.punktozaur.web.dto.NominalValueApi;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateCouponAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FixtureCouponAcceptance fixture;

    @Test
    @DisplayName("""
            given valid coupon creation request,
            when request is sent,
            then coupon is created and HTTP 201 status returned with location header""")
    void givenValidCouponCreationRequest_whenRequestIsSent_thenCouponCreatedAndHttp201Returned() {
        // given
        var intPoints = new LoyaltyPoints(1000);
        var loyaltyAccountWithPointsId = fixture.createLoyaltyAccountWithPoints(intPoints);
        var expectedPointsAfterCreateCoupon = intPoints.subtract(NominalValue.TWENTY.getRequiredPoints());
        var createCouponRequest = new CreateCouponRequest(loyaltyAccountWithPointsId, NominalValueApi.TWENTY);

        //when
        var postResponse = restTemplate.postForEntity(getBaseCouponsUrl(), createCouponRequest, Void.class);

        //then
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postResponse.getHeaders().getLocation()).isNotNull();
        assertThat(postResponse.getHeaders().getLocation()).isNotNull();
        var couponId = postResponse.getHeaders().getLocation().getPath().split("/")[2];

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

        assertThat(fixture.getLoyaltyAccountPoints(loyaltyAccountWithPointsId))
                .isEqualTo(expectedPointsAfterCreateCoupon.points());
    }

    String getBaseCouponsUrl() {
        return "http://localhost:" + port + "/coupons";
    }
}