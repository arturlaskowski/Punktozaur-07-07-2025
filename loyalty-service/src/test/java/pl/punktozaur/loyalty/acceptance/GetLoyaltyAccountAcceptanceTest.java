package pl.punktozaur.loyalty.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pl.punktozaur.loyalty.application.dto.LoyaltyAccountDto;
import pl.punktozaur.web.ApiErrorResponse;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class GetLoyaltyAccountAcceptanceTest extends BaseAcceptanceTest {

    @Test
    @DisplayName("""
            given request to get existing loyalty account,
            when request is sent,
            then return loyalty account details and HTTP 200 status""")
    void givenRequestToGetExistingLoyaltyAccount_whenRequestIsSent_thenLoyaltyAccountDetailsReturnedAndHttp200() {
        // given
        var points = 90;
        var customerId = UUID.randomUUID();
        var accountId = fixture.createLoyaltyAccount(customerId, points);

        // when
        var response = restTemplate.getForEntity(
                getBaseLoyaltyAccountsUrl() + "/" + accountId, LoyaltyAccountDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("customerId", customerId)
                .hasFieldOrPropertyWithValue("points", points)
                .extracting("id").isNotNull();
    }

    @Test
    @DisplayName("""
            given non-existing loyalty account id,
            when request is sent,
            then do not return loyalty account details and HTTP 404 status""")
    void givenRequestToGetNotExistingLoyaltyAccountsByCustomerId_whenRequestIsSent_thenLoyaltyAccountNotReturnedAndHttp404() {
        // given
        var nonExistentLoyaltyAccountId = UUID.randomUUID();

        // when
        var response = restTemplate.getForEntity(
                getBaseLoyaltyAccountsUrl() + "/" + nonExistentLoyaltyAccountId, ApiErrorResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .extracting("message")
                .asString()
                .contains("Could not find loyalty account");
    }

    String getBaseLoyaltyAccountsUrl() {
        return "http://localhost:" + port + "/loyalty-accounts";
    }
}