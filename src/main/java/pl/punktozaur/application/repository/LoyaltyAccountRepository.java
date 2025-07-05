package pl.punktozaur.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.punktozaur.domain.CustomerId;
import pl.punktozaur.domain.LoyaltyAccount;
import pl.punktozaur.domain.LoyaltyAccountId;

import java.util.List;

public interface LoyaltyAccountRepository extends JpaRepository<LoyaltyAccount, LoyaltyAccountId> {

    List<LoyaltyAccount> findByCustomerId(CustomerId customerId);
}
