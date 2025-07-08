package pl.punktozaur.coupon.application.exception;

import lombok.Getter;
import pl.punktozaur.domain.LoyaltyAccountId;

@Getter
public class LoyaltyPointsSubtractionException extends RuntimeException {

    public enum Reason {
        CLIENT_ERROR,
        SERVER_ERROR,
        UNKNOWN
    }

    private final Reason reason;

    public LoyaltyPointsSubtractionException(LoyaltyAccountId accountId, Reason reason) {
        super(createExceptionMessage(accountId, reason));
        this.reason = reason;
    }

    private static String createExceptionMessage(LoyaltyAccountId accountId, Reason reason) {
        String base = "Failed to subtract points from loyalty account with ID: %s".formatted(accountId);

        String suffix = switch (reason) {
            case CLIENT_ERROR -> " (client error)";
            case SERVER_ERROR -> " (loyalty service unavailable)";
            case UNKNOWN -> " (unknown error)";
        };

        return base + suffix;
    }
}
