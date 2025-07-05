package pl.punktozaur.coupon.contracts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.punktozaur.coupon.application.integration.loyalty.LoyaltyServiceFeignClient;
import pl.punktozaur.coupon.application.integration.loyalty.SubtractPointsRequest;
import pl.punktozaur.domain.LoyaltyAccountId;
import pl.punktozaur.domain.LoyaltyPoints;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode.LOCAL;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource(properties = {"eureka.client.enabled=false"})
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureStubRunner(
        stubsMode = LOCAL,
        ids = "pl.punktozaur:loyalty-service:+:stubs:9082")
class LoyaltyServiceContractTest {

    @Autowired
    private LoyaltyServiceFeignClient loyaltyServiceFeignClient;

    @Test
    void shouldNotThrowExceptionWhenSubtractPoints() {
        LoyaltyAccountId accountId = LoyaltyAccountId.newOne();
        LoyaltyPoints pointsToSubtract = new LoyaltyPoints(100);
        var subtractPointsRequest = new SubtractPointsRequest(pointsToSubtract.points());

        assertDoesNotThrow(() ->
                loyaltyServiceFeignClient.subtractPoints(accountId.id(), subtractPointsRequest)
        );
    }
}
