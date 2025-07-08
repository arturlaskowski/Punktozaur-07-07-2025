package pl.punktozaur.customer.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.punktozaur.avro.customer.CustomerEventAvroModel;
import pl.punktozaur.customer.domain.event.CustomerCreatedEvent;
import pl.punktozaur.kafka.DomainEventPublisher;
import pl.punktozaur.kafka.config.producer.KafkaProducer;


@Component
@RequiredArgsConstructor
public class CustomerCreatedEventPublisher implements DomainEventPublisher<CustomerCreatedEvent> {

    private final TopicsConfigData topicsConfigData;
    private final KafkaProducer<String, CustomerEventAvroModel> kafkaProducer;

    @Override
    public void publish(CustomerCreatedEvent domainEvent) {
        var customerId = domainEvent.getCustomer().getCustomerId().id();
        var customerEventAvroModel = new CustomerEventAvroModel(customerId, domainEvent.getCreatedAt());

        kafkaProducer.send(topicsConfigData.getCustomerEvent(),
                customerId.toString(),
                customerEventAvroModel);
    }
}
