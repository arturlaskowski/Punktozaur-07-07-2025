# punktozaur - Zadanie 2.1

Zaimplementuj testy kontraktowe dla komunikacji po REST API.

## Przygotowanie

W plikach `pom.xml` zostały już dodane zależności do testów kontraktowych, więc nie musisz nic zmieniać.

## Implementacja

Na start zaimplementuj [ContractTestBase](./loyalty-service/src/test/java/pl/punktozaur/loyalty/contracts/ContractTestBase.java).
Definiuje jak aplikacja ma odpowiadać na żądania opisane w kontraktach.

Następnie utwórz kontrakty.

## Uwagi techniczne

Jeśli robisz `@AutoConfigureStubRunner` podając nazwę `stubs` to odwołujesz się do jar z innego mikroserwisu, on musi istnieć. 
Wcześniej w mikroserwisie, który ma go stworzyć, możesz użyć komendy `maven package`.

## Weryfikacja

Po implementacji kontraktów zweryfikuj ich działanie:
- Zmieniając nazwę jakiegoś pola u dostawcy - test powinien się wysypać
- Zmieniając nazwę pola u odbiorcy - test powinien się wysypać

### Powodzenia!