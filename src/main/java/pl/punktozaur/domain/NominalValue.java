package pl.punktozaur.domain;

import lombok.Getter;


@Getter
public enum NominalValue {
    TEN(new LoyaltyPoints(100)),
    TWENTY(new LoyaltyPoints(200)),
    FIFTY(new LoyaltyPoints(500));

    private final LoyaltyPoints requiredPoints;

    NominalValue(LoyaltyPoints requiredPoints) {
        this.requiredPoints = requiredPoints;
    }
}
