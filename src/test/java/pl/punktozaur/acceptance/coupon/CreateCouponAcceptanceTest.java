package pl.punktozaur.acceptance.coupon;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pl.punktozaur.coupon.application.dto.CouponDto;
import pl.punktozaur.coupon.web.dto.CreateCouponRequest;
import pl.punktozaur.coupon.web.dto.NominalValueApi;
import pl.punktozaur.loyalty.LoyaltyFacade;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateCouponAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockitoBean
    private LoyaltyFacade loyaltyFacade;

    @Test
    @DisplayName("""
            given valid coupon creation request,
            when request is sent,
            then coupon is created and HTTP 201 status returned with location header""")
    void givenValidCouponCreationRequest_whenRequestIsSent_thenCouponCreatedAndHttp201Returned() {
        // given
        UUID accountId = UUID.randomUUID();
        var createCouponRequest = new CreateCouponRequest(accountId, NominalValueApi.TWENTY);

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
    }

    String getBaseCouponsUrl() {
        return "http://localhost:" + port + "/coupons";
    }
}