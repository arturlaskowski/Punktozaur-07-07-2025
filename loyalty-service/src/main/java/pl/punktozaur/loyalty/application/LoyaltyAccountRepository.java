package pl.punktozaur.loyalty.application;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.punktozaur.domain.CustomerId;
import pl.punktozaur.domain.LoyaltyAccountId;
import pl.punktozaur.loyalty.domain.LoyaltyAccount;

import java.util.List;

public interface LoyaltyAccountRepository extends JpaRepository<LoyaltyAccount, LoyaltyAccountId> {

    List<LoyaltyAccount> findByCustomerId(CustomerId customerId);
}
