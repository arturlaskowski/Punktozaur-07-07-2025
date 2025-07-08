package pl.punktozaur.loyalty.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.punktozaur.avro.customer.CustomerEventAvroModel;
import pl.punktozaur.kafka.config.consumer.AbstractKafkaConsumer;
import pl.punktozaur.loyalty.application.LoyaltyAccountService;
import pl.punktozaur.loyalty.application.dto.CreateLoyaltyAccountDto;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class CreatedCustomerEventListener extends AbstractKafkaConsumer<CustomerEventAvroModel> {

    private final LoyaltyAccountService loyaltyAccountService;

    @Override
    @KafkaListener(id = "CreatedCustomerEventListener",
            groupId = "${loyalty-service.kafka.group-id}",
            topics = "${loyalty-service.kafka.topics.customer-event}")
    protected void processMessages(List<CustomerEventAvroModel> messages) {

        messages.forEach(event ->
                loyaltyAccountService.addAccount(new CreateLoyaltyAccountDto(event.getCustomerId())));
    }

    @Override
    protected String getMessageTypeName() {
        return "customerEvent";
    }
}
