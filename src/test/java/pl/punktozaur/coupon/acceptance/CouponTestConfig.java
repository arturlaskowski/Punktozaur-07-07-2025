package pl.punktozaur.coupon.acceptance;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import pl.punktozaur.common.messaging.SubtractPointsCommand;

import static org.mockito.Mockito.doNothing;

@TestConfiguration
class CouponTestConfig {

    @Bean
    @Primary
    public ApplicationEventPublisher applicationEventPublisher() {
        ApplicationEventPublisher publisher = Mockito.mock(ApplicationEventPublisher.class);
        doNothing().when(publisher).publishEvent(SubtractPointsCommand.class);
        return publisher;
    }
}
