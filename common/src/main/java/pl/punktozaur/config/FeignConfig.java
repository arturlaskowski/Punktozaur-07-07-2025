package pl.punktozaur.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"pl.punktozaur"})
public class FeignConfig {
}
