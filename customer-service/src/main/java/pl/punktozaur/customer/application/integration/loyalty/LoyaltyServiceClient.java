package pl.punktozaur.customer.application.integration.loyalty;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.punktozaur.customer.application.exception.LoyaltyAccountCreationException;
import pl.punktozaur.domain.CustomerId;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoyaltyServiceClient {

    private final LoyaltyServiceFeignClient loyaltyServiceFeignClient;

    public void createLoyaltyAccount(CustomerId customerId) {
        log.info("Creating loyalty account for customer: {}", customerId.id());

        try {
            var request = new CreateLoyaltyAccountRequest(customerId.id());
            loyaltyServiceFeignClient.createLoyaltyAccount(request);

            log.info("Successfully created wallet for customer: {}", customerId.id());
        } catch (FeignException e) {
            var httpStatus = HttpStatus.resolve(e.status());
            var reason = resolveReason(httpStatus);

            log.error(
                    "Error creating loyalty account for customer: {}, status: {}, response: {}",
                    customerId.id(), e.status(), e.contentUTF8(), e
            );

            throw new LoyaltyAccountCreationException(customerId, reason);
        } catch (Exception e) {
            log.error("Unexpected error while creating loyalty account for customer: {}", customerId.id(), e);
            throw new LoyaltyAccountCreationException(customerId, LoyaltyAccountCreationException.Reason.SERVER_ERROR);
        }
    }

    private LoyaltyAccountCreationException.Reason resolveReason(HttpStatus status) {
        if (status == null) return LoyaltyAccountCreationException.Reason.UNKNOWN;

        return switch (status.series()) {
            case CLIENT_ERROR -> LoyaltyAccountCreationException.Reason.CLIENT_ERROR;
            case SERVER_ERROR -> LoyaltyAccountCreationException.Reason.SERVER_ERROR;
            default -> LoyaltyAccountCreationException.Reason.UNKNOWN;
        };
    }
}
