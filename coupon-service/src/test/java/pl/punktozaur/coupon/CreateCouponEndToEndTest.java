package pl.punktozaur.coupon;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateCouponEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("""
            Full integration test:
            1. Create customer
            2. Wait for loyalty account creation
            3. Verify loyalty account exists
            4. Add 200 points
            5. Create TEN coupon (100 points required)
            6. Wait for points deduction
            7. Verify loyalty account has 100 points (200 - 100)
            """)
    void shouldCreateCustomerLoyaltyAccountAddPointsCreateCouponAndVerifyPointsDeduction() {
        // Step 1: Create customer
        String uniqueEmail = generateUniqueEmail();
        String createCustomerRequestBody = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "%s"
                }
                """.formatted(uniqueEmail);

        ResponseEntity<Void> createCustomerResponse = restTemplate.postForEntity(
                getCustomerServiceUrl() + "/customers",
                createJsonHttpEntity(createCustomerRequestBody),
                Void.class);

        assertThat(createCustomerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createCustomerResponse.getHeaders().getLocation()).isNotNull();

        String customerId = extractIdFromLocation(createCustomerResponse.getHeaders().getLocation().getPath());

        // Step 2: Directly check loyalty account creation (synchronous)
        ResponseEntity<String> loyaltyAccountsResponse = restTemplate.getForEntity(
                getLoyaltyServiceUrl() + "/loyalty-accounts?customerId=" + customerId,
                String.class);

        assertThat(loyaltyAccountsResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode loyaltyAccountsArray = parseJson(loyaltyAccountsResponse.getBody());
        assertThat(loyaltyAccountsArray.isArray()).isTrue();
        assertThat(loyaltyAccountsArray.size()).isGreaterThan(0);

        // Step 3: Retrieve and verify loyalty account
        assertThat(loyaltyAccountsArray.size()).isEqualTo(1);

        JsonNode loyaltyAccount = loyaltyAccountsArray.get(0);
        assertThat(loyaltyAccount.get("customerId").asText()).isEqualTo(customerId);
        assertThat(loyaltyAccount.get("points").asInt()).isZero();

        String loyaltyAccountId = loyaltyAccount.get("id").asText();

        // Step 4: Add 200 points to loyalty account
        String addPointsRequestBody = """
                {
                  "points": 200
                }
                """;

        ResponseEntity<Void> addPointsResponse = restTemplate.exchange(
                getLoyaltyServiceUrl() + "/loyalty-accounts/" + loyaltyAccountId + "/add-points",
                HttpMethod.POST,
                createJsonHttpEntity(addPointsRequestBody),
                Void.class);

        assertThat(addPointsResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify points were added
        ResponseEntity<String> loyaltyAccountAfterAddingPoints = restTemplate.getForEntity(
                getLoyaltyServiceUrl() + "/loyalty-accounts/" + loyaltyAccountId,
                String.class);

        assertThat(loyaltyAccountAfterAddingPoints.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode loyaltyAccountWithPoints = parseJson(loyaltyAccountAfterAddingPoints.getBody());
        assertThat(loyaltyAccountWithPoints.get("points").asInt()).isEqualTo(200);

        // Step 5: Create TEN coupon (requires 50 points)
        String createCouponRequestBody = """
                {
                  "loyaltyAccountId": "%s",
                  "nominalValue": "TEN"
                }
                """.formatted(loyaltyAccountId);

        ResponseEntity<Void> createCouponResponse = restTemplate.postForEntity(
                getCouponServiceUrl() + "/coupons",
                createJsonHttpEntity(createCouponRequestBody),
                Void.class);

        assertThat(createCouponResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createCouponResponse.getHeaders().getLocation()).isNotNull();

        // Step 6: Directly check points deduction (synchronous)
        ResponseEntity<String> loyaltyAccountAfterCoupon = restTemplate.getForEntity(
                getLoyaltyServiceUrl() + "/loyalty-accounts/" + loyaltyAccountId,
                String.class);

        assertThat(loyaltyAccountAfterCoupon.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode loyaltyAccountJson = parseJson(loyaltyAccountAfterCoupon.getBody());
        assertThat(loyaltyAccountJson.get("points").asInt()).isEqualTo(100); // 200 - 100 (TEN coupon cost)

        // Step 7: Final verification - loyalty account should have 100 points
        ResponseEntity<String> finalLoyaltyAccountResponse = restTemplate.getForEntity(
                getLoyaltyServiceUrl() + "/loyalty-accounts/" + loyaltyAccountId,
                String.class);

        assertThat(finalLoyaltyAccountResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        JsonNode finalLoyaltyAccount = parseJson(finalLoyaltyAccountResponse.getBody());
        assertThat(finalLoyaltyAccount.get("customerId").asText()).isEqualTo(customerId);
        assertThat(finalLoyaltyAccount.get("points").asInt()).isEqualTo(100);
    }

    private String generateUniqueEmail() {
        return "john.doe.%d@example.com".formatted(System.currentTimeMillis());
    }

    private HttpEntity<String> createJsonHttpEntity(String jsonBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(jsonBody, headers);
    }

    private String extractIdFromLocation(String locationPath) {
        return locationPath.substring(locationPath.lastIndexOf('/') + 1);
    }

    private JsonNode parseJson(String jsonString) {
        try {
            return objectMapper.readTree(jsonString);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON: " + jsonString, e);
        }
    }

    private String getCustomerServiceUrl() {
        return "http://localhost:8581";
    }

    private String getLoyaltyServiceUrl() {
        return "http://localhost:8582";
    }

    private String getCouponServiceUrl() {
        return "http://localhost:" + port;
    }
}