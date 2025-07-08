package pl.punktozaur.customer.application;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.punktozaur.customer.domain.Customer;
import pl.punktozaur.domain.CustomerId;

public interface CustomerRepository extends JpaRepository<Customer, CustomerId> {

    boolean existsByEmail(String email);
}
