package pl.punktozaur.acceptance.coupon;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import pl.punktozaur.loyalty.LoyaltyFacade;

@TestConfiguration
class TestConfig {

    @Bean
    @Primary
    public LoyaltyFacade loyaltyFacade() {
        return new StubLoyaltyFacade(false);
    }
}
