package pl.punktozaur.loyalty.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.punktozaur.AcceptanceTest;
import pl.punktozaur.BaseIntegrationTest;
import pl.punktozaur.loyalty.application.dto.CreateLoyaltyAccountDto;
import pl.punktozaur.loyalty.application.dto.LoyaltyAccountDto;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest
class CreateLoyaltyAccountAcceptanceTest extends BaseIntegrationTest {

    @Autowired
    private FixtureLoyaltyAccountAcceptance fixture;

    @Test
    @DisplayName("""
            given valid request to create loyalty account,
            when request is sent,
            then loyalty account is created and HTTP 201 status returned with location header""")
    void givenValidRequestToCreateLoyaltyAccount_whenRequestIsSent_thenLoyaltyAccountCreatedAndHttp201Returned() {
        // given
        var createDto = new CreateLoyaltyAccountDto(UUID.randomUUID());

        // when
        var postResponse = testRestTemplate.postForEntity(
                getBaseLoyaltyAccountsUrl(), createDto, Void.class);

        // then
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postResponse.getHeaders().getLocation()).isNotNull();

        var getResponse = testRestTemplate.getForEntity(postResponse.getHeaders().getLocation(), LoyaltyAccountDto.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("customerId", createDto.customerId())
                .hasFieldOrPropertyWithValue("points", 0)
                .extracting("id").isNotNull();
    }

    String getBaseLoyaltyAccountsUrl() {
        return "http://localhost:" + port + "/loyalty-accounts";
    }
}