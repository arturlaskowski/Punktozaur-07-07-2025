package pl.punktozaur.customer.acceptance;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.punktozaur.AcceptanceTest;
import pl.punktozaur.KafkaIntegrationTest;
import pl.punktozaur.customer.application.CustomerService;
import pl.punktozaur.customer.application.dto.CreateCustomerDto;
import pl.punktozaur.customer.application.dto.CustomerDto;
import pl.punktozaur.web.ApiErrorResponse;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@AcceptanceTest(topics = CustomerAcceptanceTest.CUSTOMER_EVENT_TOPIC)
class CustomerAcceptanceTest extends KafkaIntegrationTest {

    static final String CUSTOMER_EVENT_TOPIC = "customer-events";

    @Autowired
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        setupKafkaConsumer(CUSTOMER_EVENT_TOPIC);
    }

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
    void givenRequestForCreatingCustomer_whenRequestIsSent_thenCustomerAddedAndHttp201() throws InterruptedException {
        //given
        var createCustomerDto = new CreateCustomerDto("Marianek", "Paździoch", "mario@gemail.com");

        //when
        ResponseEntity<UUID> postResponse = testRestTemplate.postForEntity(getBaseCustomersUrl(), createCustomerDto, UUID.class);

        //then
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        var location = postResponse.getHeaders().getLocation();
        assertThat(location).isNotNull();
        var customerId = location.getPath().split("/")[2];
        var getResponse = testRestTemplate.getForEntity(location, CustomerDto.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(getResponse.getBody())
                .hasFieldOrPropertyWithValue("firstName", createCustomerDto.firstName())
                .hasFieldOrPropertyWithValue("lastName", createCustomerDto.lastName())
                .hasFieldOrPropertyWithValue("email", createCustomerDto.email());

        // Verify that event was sent to Kafka
        ConsumerRecord<String, String> customerEvent = records.poll(5, TimeUnit.SECONDS);
        assertThat(customerEvent).isNotNull();
        assertThat(customerEvent.topic()).isEqualTo(CUSTOMER_EVENT_TOPIC);
        assertThat(customerEvent.key()).isEqualTo((customerId));
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