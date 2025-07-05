# punktozaur - Zadanie 1

Punktozaur to aplikacja, która pozwala zbierać punkty lojalnościowe i wymieniać je na kupony rabatowe.

## Zadanie

Twoim zadaniem jest oddzielenie modułu lojalnościowego (loyalty) od modułu kuponów (coupon).
Po wykonaniu zadania powinny istnieć pakiety:
* [pl.punktozaur.coupon](./src/main/java/pl/punktozaur/coupon)
* [pl.punktozaur.loyalty](./src/main/java/pl/punktozaur/loyalty) (został już stworzony)
* [pl.punktozaur.common](./src/main/java/pl/punktozaur/common) (tu powinny trafić tylko `ApiErrorResponse` i klasa z `@RestControllerAdvice`)

Moduł lojalnościowy nie może korzystać z żadnych elementów modułu kuponów, a moduł kuponów może korzystać tylko z [LoyaltyFacade](./src/main/java/pl/punktozaur/loyalty/LoyaltyFacade.java).

Istnieją już testy architektoniczne, które sprawdzą poprawność implementacji – jeśli wszystko zostanie zaimplementowane prawidłowo, testy będą zielone.
[Testy architektury](./src/test/java/pl/punktozaur/architecture/ArchitectureTest.java)

Oczywiście, po wprowadzeniu tych zmian wszystkie pozostałe testy po Rest API rowież powinny przejść.
Do weryfikacji relacji między modułami służy test [End to End](./src/test/java/pl/punktozaur/CreateCouponEndToEndTest.java).

### Powodzenia!
