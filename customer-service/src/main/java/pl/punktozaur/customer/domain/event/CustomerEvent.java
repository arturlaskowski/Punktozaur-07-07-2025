package pl.punktozaur.customer.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.punktozaur.customer.domain.Customer;
import pl.punktozaur.kafka.DomainEvent;

import java.time.Instant;

@RequiredArgsConstructor
@Getter
public abstract class CustomerEvent implements DomainEvent {
    private final Customer customer;
    private final Instant createdAt;

    CustomerEvent(Customer customer) {
        this.customer = customer;
        this.createdAt = Instant.now();
    }
}
