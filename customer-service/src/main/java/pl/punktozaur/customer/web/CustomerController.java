package pl.punktozaur.customer.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.punktozaur.customer.application.CustomerService;
import pl.punktozaur.customer.application.dto.CreateCustomerDto;
import pl.punktozaur.customer.web.dto.CreateCustomerRequest;
import pl.punktozaur.customer.web.dto.CustomerResponse;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable UUID id) {
        var customer = customerService.getCustomer(id);
        var customerResponse = new CustomerResponse(customer.id(), customer.firstName(),
                customer.lastName(), customer.email());
        return ResponseEntity.ok(customerResponse);
    }

    @PostMapping
    public ResponseEntity<Void> addCustomer(@RequestBody @Valid CreateCustomerRequest request) {
        var createCustomerDto = new CreateCustomerDto(request.firstName(), request.lastName(), request.email());
        var customerId = customerService.addCustomer(createCustomerDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customerId.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
