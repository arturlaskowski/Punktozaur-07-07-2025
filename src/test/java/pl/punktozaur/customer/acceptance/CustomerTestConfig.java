package pl.punktozaur.customer.acceptance;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
class CustomerTestConfig {

    @Bean
    @Primary
    public StubLoyaltyFacade loyaltyFacade() {
        return new StubLoyaltyFacade();
    }
}
