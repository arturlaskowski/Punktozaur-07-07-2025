package pl.punktozaur;

import org.springframework.core.annotation.AliasFor;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EmbeddedKafka
public @interface KafkaTestConfig {
    @AliasFor(annotation = EmbeddedKafka.class, attribute = "topics")
    String[] topics() default {};

    @AliasFor(annotation = EmbeddedKafka.class, attribute = "partitions")
    int partitions() default 1;
}