package pl.punktozaur;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import pl.punktozaur.common.ApiErrorResponse;
import pl.punktozaur.common.LoyaltyPoints;
import pl.punktozaur.coupon.application.dto.CouponDto;
import pl.punktozaur.coupon.domain.NominalValue;
import pl.punktozaur.coupon.web.dto.CreateCouponRequest;
import pl.punktozaur.coupon.web.dto.NominalValueApi;
import pl.punktozaur.loyalty.CreateLoyaltyAccountDto;
import pl.punktozaur.loyalty.LoyaltyAccountDto;
import pl.punktozaur.loyalty.ModifyPointsRequest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateCouponEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("""
            end-to-end:
            -> create loyalty account
            -> add points
            -> create coupon
            -> get loyalty account and verify points
            """)
    void shouldCreateCouponAndDeductPoints_whenLoyaltyAccountIsCreatedAndPointsAreAdded() {
        // Arrange: create a new loyalty account
        var newLoyaltyAccountRequest = new CreateLoyaltyAccountDto(UUID.randomUUID());

        // Act: send request to create loyalty account
        var createAccountResponse = restTemplate.postForEntity(
                getBaseLoyaltyAccountsUrl(), newLoyaltyAccountRequest, Void.class);

        // Assert: account is created
        assertThat(createAccountResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createAccountResponse.getHeaders().getLocation()).isNotNull();
        var loyaltyAccountId = createAccountResponse.getHeaders().getLocation().getPath().split("/")[2];

        // Arrange: add points to the loyalty account
        var initialPoints = new LoyaltyPoints(1000);
        var addPointsRequest = new ModifyPointsRequest(initialPoints.points());

        // Act: send request to add points
        var addPointsResponse = restTemplate.exchange(
                getBaseLoyaltyAccountsUrl() + "/" + loyaltyAccountId + "/add-points",
                HttpMethod.POST,
                new HttpEntity<>(addPointsRequest),
                Void.class);

        // Assert: points added successfully
        assertThat(addPointsResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        var expectedPointsAfterCoupon = initialPoints.subtract(NominalValue.TWENTY.getRequiredPoints());
        var createCouponRequest = new CreateCouponRequest(UUID.fromString(loyaltyAccountId), NominalValueApi.TWENTY);

        // Act: create coupon
        var createCouponResponse = restTemplate.postForEntity(getBaseCouponsUrl(), createCouponRequest, Void.class);

        // Assert: coupon created
        assertThat(createCouponResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createCouponResponse.getHeaders().getLocation()).isNotNull();
        var couponId = createCouponResponse.getHeaders().getLocation().getPath().split("/")[2];

        // Act: get created coupon
        var getCouponResponse = restTemplate.getForEntity(createCouponResponse.getHeaders().getLocation(), CouponDto.class);

        // Assert: coupon details are correct
        assertThat(getCouponResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getCouponResponse.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", UUID.fromString(couponId))
                .hasFieldOrPropertyWithValue("loyaltyAccountId", createCouponRequest.loyaltyAccountId())
                .hasFieldOrPropertyWithValue("isActive", true)
                .extracting(CouponDto::nominalValue)
                .extracting(Enum::name)
                .isEqualTo(createCouponRequest.nominalValue().name());

        // Act: get loyalty account details
        var getAccountResponse = restTemplate.getForEntity(
                getBaseLoyaltyAccountsUrl() + "/" + loyaltyAccountId, LoyaltyAccountDto.class);

        // Assert: points are deducted after coupon creation
        assertThat(getAccountResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getAccountResponse.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("customerId", newLoyaltyAccountRequest.customerId())
                .hasFieldOrPropertyWithValue("points", expectedPointsAfterCoupon.points())
                .extracting("id").isNotNull();
    }

    @Test
    @DisplayName("""
            end-to-end:
            -> create loyalty account
            -> create coupon rejected
            """)
    void shouldRejectCouponCreation_whenInsufficientPoints() {
        // Arrange: create a new loyalty account
        var newLoyaltyAccountRequest = new CreateLoyaltyAccountDto(UUID.randomUUID());

        // Act: send request to create loyalty account
        var createAccountResponse = restTemplate.postForEntity(
                getBaseLoyaltyAccountsUrl(), newLoyaltyAccountRequest, Void.class);

        // Assert: account is created
        assertThat(createAccountResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createAccountResponse.getHeaders().getLocation()).isNotNull();
        var loyaltyAccountId = createAccountResponse.getHeaders().getLocation().getPath().split("/")[2];

        // Do NOT add points

        var createCouponRequest = new CreateCouponRequest(UUID.fromString(loyaltyAccountId), NominalValueApi.TWENTY);

        // Act: try to create coupon (should fail)
        var createCouponResponse = restTemplate.postForEntity(getBaseCouponsUrl(), createCouponRequest, ApiErrorResponse.class);

        // Assert: coupon creation is rejected
        assertThat(createCouponResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(createCouponResponse.getBody())
                .extracting("message")
                .asString()
                .contains("Subtracting points failed for loyalty account");
    }

    @Test
    @DisplayName("""
            end-to-end:
            -> create coupon rejected (loyalit accunt not exists)
            """)
    void shouldRejectCouponCreation_whenLoyaltyAccountDoesNotExist() {
        // Arrange: use a random UUID not associated with any loyalty account
        var nonExistentLoyaltyAccountId = UUID.randomUUID();
        var createCouponRequest = new CreateCouponRequest(nonExistentLoyaltyAccountId, NominalValueApi.TWENTY);

        // Act: try to create coupon (should fail)
        var createCouponResponse = restTemplate.postForEntity(getBaseCouponsUrl(), createCouponRequest, ApiErrorResponse.class);

        // Assert: coupon creation is rejected with 400 Bad Request
        assertThat(createCouponResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(createCouponResponse.getBody())
                .extracting("message")
                .asString()
                .contains("Subtracting points failed for loyalty account");
    }

    String getBaseCouponsUrl() {
        return "http://localhost:" + port + "/coupons";
    }

    String getBaseLoyaltyAccountsUrl() {
        return "http://localhost:" + port + "/loyalty-accounts";
    }
}