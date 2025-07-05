package pl.punktozaur.acceptance.loyaltyaccount;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import pl.punktozaur.application.dto.CreateLoyaltyAccountDto;
import pl.punktozaur.application.dto.LoyaltyAccountDto;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateLoyaltyAccountAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("""
            given valid request to create loyalty account,
            when request is sent,
            then loyalty account is created and HTTP 201 status returned with location header""")
    void givenValidRequestToCreateLoyaltyAccount_whenRequestIsSent_thenLoyaltyAccountCreatedAndHttp201Returned() {
        // given
        var createDto = new CreateLoyaltyAccountDto(UUID.randomUUID());

        // when
        var postResponse = restTemplate.postForEntity(
                getBaseLoyaltyAccountsUrl(), createDto, Void.class);

        // then
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postResponse.getHeaders().getLocation()).isNotNull();

        var getResponse = restTemplate.getForEntity(postResponse.getHeaders().getLocation(), LoyaltyAccountDto.class);
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