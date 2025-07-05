package pl.punktozaur.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record LoyaltyPoints(int points) {

    public static final LoyaltyPoints ZERO = new LoyaltyPoints(0);

    public LoyaltyPoints {
        if (points < 0) {
            throw new IllegalArgumentException("Points cannot be negative");
        }
    }

    public boolean isGreaterOrEqualThan(LoyaltyPoints otherPoints) {
        return this.points >= otherPoints.points();
    }

    public LoyaltyPoints add(LoyaltyPoints otherPoints) {
        return new LoyaltyPoints(this.points + otherPoints.points);
    }

    public LoyaltyPoints subtract(LoyaltyPoints otherPoints) {
        return new LoyaltyPoints(this.points - otherPoints.points);
    }
}