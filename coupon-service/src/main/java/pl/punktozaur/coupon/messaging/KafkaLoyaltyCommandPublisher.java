package pl.punktozaur.coupon.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.punktozaur.avro.loyalty.SubtractPointsCommandAvroModel;
import pl.punktozaur.coupon.application.LoyaltyCommandPublisher;
import pl.punktozaur.domain.LoyaltyAccountId;
import pl.punktozaur.domain.LoyaltyPoints;
import pl.punktozaur.kafka.config.producer.KafkaProducer;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class KafkaLoyaltyCommandPublisher implements LoyaltyCommandPublisher {

    private final TopicsConfigData topicsConfigData;
    private final KafkaProducer<String, SubtractPointsCommandAvroModel> kafkaProducer;

    @Override
    public void publishSubtractPointsCommand(LoyaltyAccountId loyaltyAccountId, LoyaltyPoints loyaltyPoints) {
        var processPaymentCommandAvroModel = new SubtractPointsCommandAvroModel(
                loyaltyAccountId.id(),
                loyaltyPoints.points(),
                Instant.now()
        );

        kafkaProducer.send(topicsConfigData.getLoyaltyCommand(),
                loyaltyAccountId.toString(),
                processPaymentCommandAvroModel);
    }
}
