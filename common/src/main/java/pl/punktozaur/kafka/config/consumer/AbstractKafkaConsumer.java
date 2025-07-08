package pl.punktozaur.kafka.config.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;

@Slf4j
public abstract class AbstractKafkaConsumer<T extends SpecificRecordBase> implements KafkaConsumer<T> {

    @Override
    public final void receive(@Payload List<T> messages,
                              @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                              @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                              @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of {} messages with keys:{}, partitions:{} and offsets: {}",
                messages.size(),
                getMessageTypeName(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        processMessages(messages);
    }

    protected abstract void processMessages(List<T> messages);

    protected abstract String getMessageTypeName();
}