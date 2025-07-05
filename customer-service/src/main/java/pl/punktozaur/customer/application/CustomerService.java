package pl.punktozaur.customer.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.punktozaur.customer.application.dto.CreateCustomerDto;
import pl.punktozaur.customer.application.dto.CustomerDto;
import pl.punktozaur.customer.application.exception.CustomerAlreadyExistsException;
import pl.punktozaur.customer.application.exception.CustomerNotFoundException;
import pl.punktozaur.customer.domain.Customer;
import pl.punktozaur.domain.CustomerId;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerDto getCustomer(UUID id) {
        CustomerId customerId = new CustomerId(id);
        return customerRepository.findById(customerId)
                .map(customer -> new CustomerDto(customer.getCustomerId().id(), customer.getFirstName(), customer.getLastName(), customer.getEmail()))
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Transactional
    public CustomerId addCustomer(CreateCustomerDto customerDto) {
        if (customerRepository.existsByEmail(customerDto.email())) {
            throw new CustomerAlreadyExistsException(customerDto.email());
        }
        
        var customer = new Customer(customerDto.firstName(), customerDto.lastName(), customerDto.email());
        return customerRepository.save(customer).getCustomerId();
    }
}