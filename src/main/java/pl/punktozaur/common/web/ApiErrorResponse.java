package pl.punktozaur.common.web;

import java.time.Instant;

public record ApiErrorResponse(String message, Instant timestamp) {

    public ApiErrorResponse(String message) {
        this(message, Instant.now());
    }
}
