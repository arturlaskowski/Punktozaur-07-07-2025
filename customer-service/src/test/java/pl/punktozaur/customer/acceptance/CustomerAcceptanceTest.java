package pl.punktozaur.customer.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.punktozaur.customer.application.dto.CreateCustomerDto;
import pl.punktozaur.customer.application.dto.CustomerDto;
import pl.punktozaur.web.ApiErrorResponse;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerAcceptanceTest extends BaseAcceptanceTest {

    @Test
    @DisplayName("""
            given existing Customer id,
            when request is sent,
            then return Customer details and HTTP 200 status""")
    void givenExistingCustomerId_whenRequestIsSent_thenCustomerDetailsReturnedAndHttp200() {
        //given
        var createCustomerDto = new CreateCustomerDto("Ferdzio", "Kiepski", "ferdek@gemail.com");
        var customerId = customerService.addCustomer(createCustomerDto);

        //when
        ResponseEntity<CustomerDto> response = testRestTemplate.getForEntity(
                getBaseCustomersUrl() + "/" + customerId.id(), CustomerDto.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", customerId.id())
                .hasFieldOrPropertyWithValue("firstName", createCustomerDto.firstName())
                .hasFieldOrPropertyWithValue("lastName", createCustomerDto.lastName())
                .hasFieldOrPropertyWithValue("email", createCustomerDto.email());
    }

    @Test
    @DisplayName("""
            given non-existing Customer id,
            when request is sent,
            then do not return Customer details and HTTP 404 status""")
    void givenNonExistingCustomerId_whenRequestIsSent_thenCustomerDetailsNotReturnedAndHttp404() {
        //given
        var notExistingId = UUID.randomUUID();

        //when
        ResponseEntity<ApiErrorResponse> response = testRestTemplate.getForEntity(getBaseCustomersUrl() + "/" + notExistingId, ApiErrorResponse.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .extracting("message")
                .asString()
                .contains("Could not find customer");
    }

    @Test
    @DisplayName("""
            given request for creating Customer with all mandatory data correctly,
            when request is sent,
            then Customer is added and HTTP 201 status received""")
    void givenRequestForCreatingCustomer_whenRequestIsSent_thenCustomerAddedAndHttp201() {
        //given
        var createCustomerDto = new CreateCustomerDto("Marianek", "Paździoch", "mario@gemail.com");

        //when
        ResponseEntity<UUID> postResponse = testRestTemplate.postForEntity(getBaseCustomersUrl(), createCustomerDto, UUID.class);

        //then
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        var location = postResponse.getHeaders().getLocation();
        var getResponse = testRestTemplate.getForEntity(location, CustomerDto.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(getResponse.getBody())
                .hasFieldOrPropertyWithValue("firstName", createCustomerDto.firstName())
                .hasFieldOrPropertyWithValue("lastName", createCustomerDto.lastName())
                .hasFieldOrPropertyWithValue("email", createCustomerDto.email());
    }

    @Test
    @DisplayName("""
            given request for creating Customer with existing email,
            when request is sent,
            then Customer not added and HTTP 409 status received""")
    void givenRequestForCreatingCustomerWithExistingEmail_whenRequestIsSent_thenCustomerNotAddedAndHttp409() {
        //given
        String email = "waldek12@gmail.com";
        customerService.addCustomer(new CreateCustomerDto("Waldemar", "Kiepski", email));

        var createCustomerDto = new CreateCustomerDto("Walduś", "Boczek", email);

        //when
        ResponseEntity<ApiErrorResponse> response = testRestTemplate.postForEntity(getBaseCustomersUrl(), createCustomerDto, ApiErrorResponse.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getHeaders().getLocation()).isNull();
        assertThat(response.getBody())
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .extracting("message")
                .isNotNull();
    }

    private String getBaseCustomersUrl() {
        return "http://localhost:" + port + "/customers";
    }
}