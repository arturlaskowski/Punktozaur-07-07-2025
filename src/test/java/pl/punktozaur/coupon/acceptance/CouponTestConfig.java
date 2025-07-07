package pl.punktozaur.coupon.acceptance;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
class CouponTestConfig {

    @Bean
    @Primary
    public StubLoyaltyFacade loyaltyFacade() {
        return new StubLoyaltyFacade();
    }
}
