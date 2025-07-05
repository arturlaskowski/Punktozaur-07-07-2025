# punktozaur - Zadanie 2.1

Zaimplementuj testy kontraktowe dla komunikacji po REST API.

## Przygotowanie

W plikach `pom.xml` zostały już dodane zależności do testów kontraktowych, więc nie musisz nic zmieniać.

## Implementacja

Na start zaimplementuj [ContractTestBase](./loyalty-service/src/test/java/pl/punktozaur/loyalty/contracts/ContractTestBase.java).
Definiuje jak aplikacja ma odpowiadać na żądania opisane w kontraktach.

Następnie utwórz kontrakty.

Twój opis jest bardzo bliski ideału – zawiera kluczowe informacje i poprawne zrozumienie działania Spring Cloud Contract. Wymaga tylko kilku drobnych korekt językowych i doprecyzowań. Oto poprawiona wersja:


## Kontrakty
`loyalty-service` wystawia API, z którego korzystają `coupon-service` i `customer-service`.
Kontrakty (np. w formacie `.groovy`) będą znajdować się w `loyalty-service`.

W zależności od przyjętego podejścia, kontrakty mogą być tworzone przez **producenta** (tzw. *producer-driven contracts*) lub przez **konsumentów** (*consumer-driven contracts*).
Przenoszenie kontraktów do odpowiednich katalogów oraz generowanie stubów można zautomatyzować w ramach procesu CI/CD.

Na podstawie kontraktów, w `loyalty-service` generowany jest plik `.stubs.jar`, który trafia do katalogu `target`.
W typowym scenariuszu plik ten byłby publikowany do repozytorium artefaktów (np. Nexus).
Konsumenci mogą następnie pobierać konkretną wersję stubów, aby przetestować zgodność integracji **przed wdrożeniem na środowisko**.

Konsumenci, czyli `coupon-service` i `customer-service`, definiują testy integracyjne, które korzystają z wygenerowanych stubów (`.stubs.jar`) z `loyalty-service`. 
Dzięki temu mogą upewnić się, że ich implementacja klienta jest zgodna z wymaganiami opisanymi w kontrakcie.

## Uwagi techniczne

Jeśli robisz `@AutoConfigureStubRunner` podając nazwę `stubs` to odwołujesz się do jar z innego mikroserwisu, on musi istnieć. 
Wcześniej w mikroserwisie, który ma go stworzyć, możesz użyć komendy `maven package`.


## Weryfikacja

Po implementacji kontraktów zweryfikuj ich działanie:
- Zmieniając nazwę jakiegoś pola u dostawcy - test powinien się wysypać
- Zmieniając nazwę pola u odbiorcy - test powinien się wysypać

### Powodzenia!