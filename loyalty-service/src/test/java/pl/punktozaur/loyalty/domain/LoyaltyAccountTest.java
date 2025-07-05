package pl.punktozaur.loyalty.domain;

import org.junit.jupiter.api.Test;
import pl.punktozaur.domain.CustomerId;
import pl.punktozaur.domain.LoyaltyPoints;
import pl.punktozaur.loyalty.application.exception.InsufficientPointsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoyaltyAccountTest {

    private final CustomerId customerId = CustomerId.newOne();

    @Test
    void shouldCreateLoyaltyAccount() {
        var account = new LoyaltyAccount(customerId);

        assertThat(account.getId()).isNotNull();
        assertThat(account.getCustomerId()).isEqualTo(customerId);
        assertThat(account.getPoints().points()).isZero();
    }

    @Test
    void shouldAddPoints() {
        var account = new LoyaltyAccount(customerId);
        var pointsToAdd = new LoyaltyPoints(100);

        account.addPoints(pointsToAdd);
        assertEquals(pointsToAdd, account.getPoints());

        account.addPoints(pointsToAdd);
        assertEquals(pointsToAdd.add(pointsToAdd), account.getPoints());
    }

    @Test
    void shouldSubtractPoints() {
        var account = new LoyaltyAccount(customerId);
        var initPoints = new LoyaltyPoints(100);
        account.addPoints(initPoints);
        var pointsToSubtract = new LoyaltyPoints(40);

        account.subtractPoints(pointsToSubtract);
        assertEquals(initPoints.subtract(pointsToSubtract), account.getPoints());

        account.subtractPoints(pointsToSubtract);
        assertEquals(initPoints.subtract(pointsToSubtract).subtract(pointsToSubtract), account.getPoints());
    }

    @Test
    void shouldThrowInsufficientPointsExceptionWhenSubtractingMorePointsThanExist() {
        var account = new LoyaltyAccount(customerId);
        var initPoints = new LoyaltyPoints(100);
        account.addPoints(initPoints);
        var pointsToSubtract = new LoyaltyPoints(101);

        assertThrows(InsufficientPointsException.class,
                () -> account.subtractPoints(pointsToSubtract));

        assertEquals(initPoints, account.getPoints());

    }
}