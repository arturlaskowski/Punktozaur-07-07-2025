package pl.punktozaur.customer.application.integration.loyalty;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "loyalty-service")
public interface LoyaltyServiceFeignClient {

    @PostMapping("/loyalty-accounts")
    void createLoyaltyAccount(@RequestBody CreateLoyaltyAccountRequest request);
}
