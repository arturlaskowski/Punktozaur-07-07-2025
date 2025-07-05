package pl.punktozaur.customer.contracts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.punktozaur.customer.application.integration.loyalty.CreateLoyaltyAccountRequest;
import pl.punktozaur.customer.application.integration.loyalty.LoyaltyServiceFeignClient;
import pl.punktozaur.domain.CustomerId;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode.LOCAL;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {"eureka.client.enabled=false"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureStubRunner(
        stubsMode = LOCAL,
        ids = "pl.punktozaur:loyalty-service:+:stubs:9082")
class LoyaltyServiceContractTest {

    @Autowired
    private LoyaltyServiceFeignClient loyaltyServiceFeignClient;

    @Test
    void shouldNotThrowExceptionWhenCreatingLoyaltyAccountForExistingCustomer() {
        var customerId = CustomerId.newOne();
        var request = new CreateLoyaltyAccountRequest(customerId.id());

        assertDoesNotThrow(() ->
                loyaltyServiceFeignClient.createLoyaltyAccount(request)
        );
    }
}
