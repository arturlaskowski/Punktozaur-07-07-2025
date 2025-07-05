package pl.punktozaur.coupon.application.integration.loyalty;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.punktozaur.coupon.application.exception.LoyaltyPointsSubtractionException;
import pl.punktozaur.domain.LoyaltyAccountId;
import pl.punktozaur.domain.LoyaltyPoints;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoyaltyServiceClient {

    private final LoyaltyServiceFeignClient loyaltyServiceFeignClient;

    public void subtractPoints(LoyaltyAccountId accountId, LoyaltyPoints pointsToSubtract) {
        log.info("Attempting to subtract {} points from loyalty account: {}", pointsToSubtract.points(), accountId.id());

        try {
            SubtractPointsRequest request = new SubtractPointsRequest(pointsToSubtract.points());
            loyaltyServiceFeignClient.subtractPoints(accountId.id(), request);

            log.info("Successfully subtracted {} points from loyalty account: {}", pointsToSubtract.points(), accountId.id());
        } catch (FeignException e) {
            HttpStatus httpStatus = HttpStatus.resolve(e.status());

            LoyaltyPointsSubtractionException.Reason reason = resolveReason(httpStatus);
            log.warn("Failed to subtract points from loyalty account: {}. Reason: {}, HTTP status: {}", accountId.id(), reason, httpStatus);

            throw new LoyaltyPointsSubtractionException(accountId, reason);
        } catch (Exception e) {
            log.error("Unexpected error while subtracting points from loyalty account: {}", accountId.id(), e);

            throw new LoyaltyPointsSubtractionException(accountId, LoyaltyPointsSubtractionException.Reason.UNKNOWN);
        }
    }

    private LoyaltyPointsSubtractionException.Reason resolveReason(HttpStatus status) {
        if (status == null) {
            return LoyaltyPointsSubtractionException.Reason.UNKNOWN;
        }

        return switch (status.series()) {
            case CLIENT_ERROR -> LoyaltyPointsSubtractionException.Reason.CLIENT_ERROR;
            case SERVER_ERROR -> LoyaltyPointsSubtractionException.Reason.SERVER_ERROR;
            default -> LoyaltyPointsSubtractionException.Reason.UNKNOWN;
        };
    }
}
