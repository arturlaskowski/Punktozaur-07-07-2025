package pl.punktozaur.loyalty.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import pl.punktozaur.loyalty.application.dto.ModifyPointsDto;
import pl.punktozaur.web.ApiErrorResponse;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AddPointsAcceptanceTest extends BaseAcceptanceTest {

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
        var modifyPointsDto = new ModifyPointsDto(pointsToAdd);

        // when
        var response = restTemplate.exchange(
                getBaseLoyaltyAccountsUrl() + "/" + accountId + "/add-points",
                HttpMethod.POST,
                new HttpEntity<>(modifyPointsDto),
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
        var modifyPointsDto = new ModifyPointsDto(10);
        var nonExistentLoyaltyAccountId = UUID.randomUUID();

        // when
        var response = restTemplate.exchange(
                getBaseLoyaltyAccountsUrl() + "/" + nonExistentLoyaltyAccountId + "/add-points",
                HttpMethod.POST,
                new HttpEntity<>(modifyPointsDto),
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