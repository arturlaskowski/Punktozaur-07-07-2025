## Zadanie 4

Wykorzystaj mechanizm messagingu wbudowany w Spring (`ApplicationEventPublisher`) (jest to messaging, który domyślnie opiera się na pamięci aplikacji, domyślnie działa synchronicznie. Dzięki adnotacji `@Async` można zmienić na podejście asynchroniczne, ale zrobimy to w kolejnym zadaniu z użyciem brokera wiadomości).

Zadbaj, aby wszystkie moduły były luźno powiązane (bezpośdenio z sobą nie rozmaiwały).
Zostanie to zweryfikowane przez [Testy Architektoniczne](src/test/java/pl/punktozaur/architecture/ArchitectureTest.java).

Zastąp obecną implementację messagingiem.
Klasy `...Facade` powinny zostać usunięte. 

Po utworzeniu Customera nadal powinno być tworzone konto lojalnościowe, a przy tworzeniu kuponu powinny być odejmowane punkty z konta lojalnościowego.
Obserwowalne zachowanie aplikacji się nie zmienia.

Zostanie to zweryfikowane przez [Testy End to End](src/test/java/pl/punktozaur/CreateCouponEndToEndTest.java).


W konfiguracji testów akceptacyjnych zamiast stubowania fasady, zastąp ją stubem dla `ApplicationEventPublisher`.
```java
@TestConfiguration
class CustomerTestConfig {

    @Bean
    @Primary
    public ApplicationEventPublisher applicationEventPublisher() {
        ApplicationEventPublisher publisher = Mockito.mock(ApplicationEventPublisher.class);
        doNothing().when(publisher).publishEvent(CustomerCreatedEvent.class);
        return publisher;
    }
}
```