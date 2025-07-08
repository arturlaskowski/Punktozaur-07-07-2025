package pl.punktozaur.customer.domain.event;

import pl.punktozaur.customer.domain.Customer;

public class CustomerCreatedEvent extends CustomerEvent {

    public CustomerCreatedEvent(Customer customer) {
        super(customer);
    }
}