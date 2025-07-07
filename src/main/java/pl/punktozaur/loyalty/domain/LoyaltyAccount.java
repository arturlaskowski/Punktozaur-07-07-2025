package pl.punktozaur.loyalty.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.punktozaur.common.domain.LoyaltyAccountId;
import pl.punktozaur.common.domain.LoyaltyPoints;
import pl.punktozaur.loyalty.application.exception.InsufficientPointsException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoyaltyAccount {

    @Id
    private LoyaltyAccountId id;

    @NotNull
    private CustomerId customerId;

    @NotNull
    private LoyaltyPoints points;

    @Version
    private int version;

    public LoyaltyAccount(CustomerId customerId) {
        this.id = LoyaltyAccountId.newOne();
        this.customerId = customerId;
        this.points = LoyaltyPoints.ZERO;
    }

    public void addPoints(LoyaltyPoints pointsToAdd) {
        points = points.add(pointsToAdd);
    }

    public void subtractPoints(LoyaltyPoints pointsToSubtract) {
        if (!points.isGreaterOrEqualThan(pointsToSubtract)) {
            throw new InsufficientPointsException(id);
        }
        points = points.subtract(pointsToSubtract);
    }
}
