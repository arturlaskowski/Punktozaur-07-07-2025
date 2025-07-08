package pl.punktozaur.loyalty.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.punktozaur.avro.loyalty.SubtractPointsCommandAvroModel;
import pl.punktozaur.domain.LoyaltyAccountId;
import pl.punktozaur.domain.LoyaltyPoints;
import pl.punktozaur.kafka.config.consumer.AbstractKafkaConsumer;
import pl.punktozaur.loyalty.application.LoyaltyAccountService;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class SubtractPointsCommandListener extends AbstractKafkaConsumer<SubtractPointsCommandAvroModel> {

    private final LoyaltyAccountService loyaltyAccountService;

    @Override
    @KafkaListener(id = "SubtractPointsCommandListener",
            groupId = "${loyalty-service.kafka.group-id}",
            topics = "${loyalty-service.kafka.topics.loyalty-command}")
    protected void processMessages(List<SubtractPointsCommandAvroModel> messages) {
        messages.forEach(message -> {
            var loyaltyPoints = new LoyaltyPoints(message.getPoints());
            var loyaltyAccountId = new LoyaltyAccountId(message.getLoyaltyAccountId());
            loyaltyAccountService.subtractPoints(loyaltyAccountId, loyaltyPoints);
        });
    }

    @Override
    protected String getMessageTypeName() {
        return "subtractPointsCommand";
    }
}
