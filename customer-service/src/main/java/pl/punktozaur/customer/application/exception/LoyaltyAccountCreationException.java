package pl.punktozaur.customer.application.exception;

import lombok.Getter;
import pl.punktozaur.domain.CustomerId;

@Getter
public class LoyaltyAccountCreationException extends RuntimeException {

    public enum Reason {
        CLIENT_ERROR, SERVER_ERROR, UNKNOWN
    }

    private final Reason reason;

    public LoyaltyAccountCreationException(CustomerId customerId, Reason reason) {
        super(createExceptionMessage(customerId, reason));
        this.reason = reason;
    }

    private static String createExceptionMessage(CustomerId customerId, Reason reason) {
        String base = "Failed to create loyalty account for customer with ID: %s".formatted(customerId);

        String suffix = switch (reason) {
            case CLIENT_ERROR -> " (client error)";
            case SERVER_ERROR -> " (loyalty service unavailable)";
            case UNKNOWN -> " (unknown error)";
        };

        return base + suffix;
    }
}
