package pl.punktozaur.coupon.acceptance;


import lombok.Setter;
import pl.punktozaur.common.domain.LoyaltyAccountId;
import pl.punktozaur.common.domain.LoyaltyPoints;
import pl.punktozaur.loyalty.application.LoyaltyFacade;

@Setter
class StubLoyaltyFacade implements LoyaltyFacade {

    private boolean shouldFail = false;
    private String failureMessage = "Points subtraction failed";

    public void setShouldFail(boolean shouldFail) {
        this.shouldFail = shouldFail;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    @Override
    public void subtractPoints(LoyaltyAccountId loyaltyAccountId, LoyaltyPoints requiredPoints) {
        if (shouldFail) {
            throw new RuntimeException(failureMessage);
        }
        // Simulate success
    }
}