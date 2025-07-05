package pl.punktozaur.coupon.application.integration.loyalty;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "loyalty-service")
public interface LoyaltyServiceFeignClient {

    @PostMapping("/loyalty-accounts/{loyaltyAccount}/subtract-points")
    void subtractPoints(@PathVariable UUID loyaltyAccount, @RequestBody SubtractPointsRequest request);
}
