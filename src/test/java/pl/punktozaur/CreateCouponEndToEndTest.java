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
import pl.punktozaur.common.domain.LoyaltyPoints;
import pl.punktozaur.common.web.ApiErrorResponse;
import pl.punktozaur.coupon.domain.NominalValue;
import pl.punktozaur.coupon.query.CouponDto;
import pl.punktozaur.coupon.web.dto.CreateCouponRequest;
import pl.punktozaur.coupon.web.dto.NominalValueApi;
import pl.punktozaur.customer.application.dto.CreateCustomerDto;
import pl.punktozaur.loyalty.application.dto.LoyaltyAccountDto;
import pl.punktozaur.loyalty.web.dto.ModifyPointsDto;

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
            -> create customer
            -> get loyalty account for customer
            -> add points
            -> create coupon
            -> get loyalty account and verify points
            """)
    void shouldCreateCouponAndDeductPoints_whenLoyaltyAccountIsCreatedAndPointsAreAdded() {

        // Arrange: create a new customer
        var newCustomer = new CreateCustomerDto("Edzio", "Listonosz", "edzio@gemail.com");

        // Act: send request to create customer
        var createCustomerResponse = restTemplate.postForEntity(getBaseCustomerUrl(), newCustomer, Void.class);

        // Assert: customer created successfully
        assertThat(createCustomerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createCustomerResponse.getHeaders().getLocation()).isNotNull();
        var customerId = createCustomerResponse.getHeaders().getLocation().getPath().split("/")[2];

        // Act: retrieve loyalty account for the created customer
        var loyaltyAccountResponse = restTemplate.getForEntity(
                getBaseLoyaltyAccountsUrl() + "?customerId=" + customerId,
                LoyaltyAccountDto[].class);

        // Assert: loyalty account exists
        assertThat(loyaltyAccountResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loyaltyAccountResponse.getBody()).isNotNull();
        assertThat(loyaltyAccountResponse.getBody()).hasSize(1);

        var loyaltyAccountId = loyaltyAccountResponse.getBody()[0].id();

        // Arrange: prepare to add points to the loyalty account
        var pointsToAdd = new LoyaltyPoints(1000);
        var addPointsDto = new ModifyPointsDto(pointsToAdd.points());

        // Act: send request to add points
        var addPointsResult = restTemplate.exchange(
                getBaseLoyaltyAccountsUrl() + "/" + loyaltyAccountId + "/add-points",
                HttpMethod.POST,
                new HttpEntity<>(addPointsDto),
                Void.class);

        // Assert: points added successfully
        assertThat(addPointsResult.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        var expectedPointsAfterCoupon = pointsToAdd.subtract(NominalValue.TWENTY.getRequiredPoints());
        var createCouponRequest = new CreateCouponRequest(loyaltyAccountId, NominalValueApi.TWENTY);

        // Act: create a coupon
        var createCouponResponse = restTemplate.postForEntity(getBaseCouponsUrl(), createCouponRequest, Void.class);

        // Assert: coupon created successfully
        assertThat(createCouponResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createCouponResponse.getHeaders().getLocation()).isNotNull();
        var couponId = createCouponResponse.getHeaders().getLocation().getPath().split("/")[2];

        // Act: retrieve the created coupon
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

        // Act: retrieve updated loyalty account details
        var getUpdatedAccountResponse = restTemplate.getForEntity(
                getBaseLoyaltyAccountsUrl() + "/" + loyaltyAccountId, LoyaltyAccountDto.class);

        // Assert: points are deducted after coupon creation
        assertThat(getUpdatedAccountResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getUpdatedAccountResponse.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("customerId", UUID.fromString(customerId))
                .hasFieldOrPropertyWithValue("points", expectedPointsAfterCoupon.points())
                .extracting("id").isNotNull();
    }

    @Test
    @DisplayName("""
            end-to-end:
            -> create customer
            -> get loyalty account for customer
            -> create coupon rejected
            """)
    void shouldRejectCouponCreation_whenInsufficientPoints() {
        // Arrange: create a new customer
        var newCustomer = new CreateCustomerDto("Arnold", "Boczek", "boczek@gemail.com");

        // Act: send request to create customer
        var createCustomerResponse = restTemplate.postForEntity(getBaseCustomerUrl(), newCustomer, Void.class);

        // Assert: customer created successfully
        assertThat(createCustomerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createCustomerResponse.getHeaders().getLocation()).isNotNull();
        var customerId = createCustomerResponse.getHeaders().getLocation().getPath().split("/")[2];

        // Act: retrieve loyalty account for the created customer
        var loyaltyAccountResponse = restTemplate.getForEntity(
                getBaseLoyaltyAccountsUrl() + "?customerId=" + customerId,
                LoyaltyAccountDto[].class);

        // Assert: loyalty account exists
        assertThat(loyaltyAccountResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loyaltyAccountResponse.getBody()).isNotNull();
        assertThat(loyaltyAccountResponse.getBody()).hasSize(1);

        var loyaltyAccountId = loyaltyAccountResponse.getBody()[0].id();

        // Do NOT add points

        var createCouponRequest = new CreateCouponRequest(loyaltyAccountId, NominalValueApi.TWENTY);

        // Act: try to create coupon (should fail)
        var createCouponResponse = restTemplate.postForEntity(getBaseCouponsUrl(), createCouponRequest, ApiErrorResponse.class);

        // Assert: coupon creation is rejected
        assertThat(createCouponResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(createCouponResponse.getBody())
                .extracting("message")
                .asString()
                .contains("Subtracting points failed for loyalty account");
    }

    String getBaseCustomerUrl() {
        return "http://localhost:" + port + "/customers";
    }

    String getBaseCouponsUrl() {
        return "http://localhost:" + port + "/coupons";
    }

    String getBaseLoyaltyAccountsUrl() {
        return "http://localhost:" + port + "/loyalty-accounts";
    }
}