package pl.punktozaur.loyalty.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import pl.punktozaur.common.web.ApiErrorResponse;
import pl.punktozaur.loyalty.web.dto.ModifyPointsDto;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddPointsAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FixtureLoyaltyAccountAcceptance fixture;

    @Test
    @DisplayName("""
            given valid request to add points,
            when request is sent to an existing account,
            then HTTP 204 status received""")
    void givenValidRequestToAddPoints_whenRequestIsSent_thenHttp204Received() {
        // given
        int initPoints = 100;
        int pointsToAdd = 50;
        var accountId = fixture.createLoyaltyAccountWithPoints(initPoints);
        var modifyPointsRequest = new ModifyPointsDto(pointsToAdd);

        // when
        var response = restTemplate.exchange(
                getBaseLoyaltyAccountsUrl() + "/" + accountId + "/add-points",
                HttpMethod.POST,
                new HttpEntity<>(modifyPointsRequest),
                Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(fixture.getLoyaltyAccountPoints(accountId))
                .isEqualTo(initPoints + pointsToAdd);
    }

    @Test
    @DisplayName("""
            given request to add points for non-existent loyalty account,
            when request is sent,
            then HTTP 404 status received""")
    void givenRequestToAddPointsForNonExistentLoyaltyAccount_whenRequestIsSent_thenHttp404Received() {
        //given
        var modifyPointsRequest = new ModifyPointsDto(10);
        var nonExistentLoyaltyAccountId = UUID.randomUUID();

        // when
        var response = restTemplate.exchange(
                getBaseLoyaltyAccountsUrl() + "/" + nonExistentLoyaltyAccountId + "/add-points",
                HttpMethod.POST,
                new HttpEntity<>(modifyPointsRequest),
                ApiErrorResponse.class);

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