package pl.punktozaur.kafka;


public interface DomainEventPublisher<T extends DomainEvent> {

    void publish(T domainEvent);
}
