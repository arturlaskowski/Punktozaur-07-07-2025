package pl.punktozaur.loyalty.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.punktozaur.AcceptanceTest;
import pl.punktozaur.BaseIntegrationTest;
import pl.punktozaur.loyalty.application.dto.LoyaltyAccountDto;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest
class GetLoyaltyAccountsByCustomerIdAcceptanceTest extends BaseIntegrationTest {

    @Autowired
    private FixtureLoyaltyAccountAcceptance fixture;

    @Test
    @DisplayName("""
            given request to get existing loyalty accounts by customerId,
            when request is sent,
            then return loyalty accounts list and HTTP 200 status""")
    void givenRequestToGetExistingLoyaltyAccount_whenRequestIsSent_thenLoyaltyAccountDetailsReturnedAndHttp200() {
        // given
        var points = 90;
        var customerId = UUID.randomUUID();
        var expectedAccountId1 = fixture.createLoyaltyAccount(customerId, points);
        var expectedAccountId2 = fixture.createLoyaltyAccount(customerId, points);
        var expectedAccountId3 = fixture.createLoyaltyAccount(customerId, points);

        var nonExpectedAccountId1 = fixture.createLoyaltyAccount(UUID.randomUUID(), points);
        var nonExpectedAccountId2 = fixture.createLoyaltyAccount(UUID.randomUUID(), points);

        // when
        var response = testRestTemplate.getForEntity(
                getBaseLoyaltyAccountsUrl() + "?customerId=" + customerId, LoyaltyAccountDto[].class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(3);
        assertThat(response.getBody()).extracting(LoyaltyAccountDto::id)
                .containsExactlyInAnyOrder(expectedAccountId1, expectedAccountId2, expectedAccountId3)
                .doesNotContain(nonExpectedAccountId1, nonExpectedAccountId2);
    }

    String getBaseLoyaltyAccountsUrl() {
        return "http://localhost:" + port + "/loyalty-accounts";
    }
}