package pl.punktozaur.loyalty;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.punktozaur.common.LoyaltyAccountId;

import java.util.List;

public interface LoyaltyAccountRepository extends JpaRepository<LoyaltyAccount, LoyaltyAccountId> {

    List<LoyaltyAccount> findByCustomerId(CustomerId customerId);
}
