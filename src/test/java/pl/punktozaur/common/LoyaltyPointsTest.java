package pl.punktozaur.common;

import org.junit.jupiter.api.Test;
import pl.punktozaur.common.domain.LoyaltyPoints;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoyaltyPointsTest {

    @Test
    void shouldCretePointsWhenPointsAreZeroOrPositive() {
        int zero = 0;
        var zeroPoints = new LoyaltyPoints(zero);
        assertThat(zero).isEqualTo(zeroPoints.points());


        var ten = 10;
        var tenPoints = new LoyaltyPoints(ten);
        assertThat(ten).isEqualTo(tenPoints.points());
    }

    @Test
    void constructorShouldThrowExceptionWhenPointsAreNegative() {
        assertThatThrownBy(() -> new LoyaltyPoints(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Points cannot be negative");
    }

    @Test
    void isGreaterOrEqualThanShouldCorrectlyCompareTwoLoyaltyPointsObjects() {
        var basePoints = new LoyaltyPoints(100);
        var greaterPoints = new LoyaltyPoints(150);
        var equalPoints = new LoyaltyPoints(100);

        assertThat(basePoints.isGreaterOrEqualThan(greaterPoints)).isFalse();
        assertThat(basePoints.isGreaterOrEqualThan(equalPoints)).isTrue();
        assertThat(greaterPoints.isGreaterOrEqualThan(basePoints)).isTrue();
    }

    @Test
    void addShouldCorrectlyAddTwoLoyaltyPoints() {
        var points1 = new LoyaltyPoints(100);
        var points2 = new LoyaltyPoints(50);
        var expectedResult = new LoyaltyPoints(150);

        assertThat(points1.add(points2)).isEqualTo(expectedResult);
    }

    @Test
    void subtractShouldCorrectlySubtractTwoLoyaltyPoints() {
        var points1 = new LoyaltyPoints(100);
        var points2 = new LoyaltyPoints(50);
        var expectedResult = new LoyaltyPoints(50);

        assertThat(points1.subtract(points2)).isEqualTo(expectedResult);
    }

    @Test
    void subtractResultingInNegativePointsShouldThrow() {
        var points1 = new LoyaltyPoints(50);
        var points2 = new LoyaltyPoints(100);

        assertThatThrownBy(() -> points1.subtract(points2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Points cannot be negative");
    }
}