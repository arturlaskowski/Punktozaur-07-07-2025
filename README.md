# punktozaur - Zadanie 2

Używając Feign Client zaimplementuj komunikację synchroniczną między usługami.
Wymagania są następujące:
* Podczas tworzenia klienta (endpoint `/customers`) ma się utworzyć konto lojalnościowe.
* Podczas tworzenia kuponu (endpoint `/coupons`) mają zostać odjęte punkty z konta lojalnościowego. Jeśli brakuje punktów lub konto nie istnieje, kupon nie może zostać utworzony.

Wszystkie zależności są już dodane, więc nie trzeba nic zmieniać w `pom.xml`.
Po implementacji pamiętaj o dostosowaniu testów. W testach są klasy `BaseAcceptanceTest`, w nich wystarczy dodać mock dla wywołania zewnętrznej usługi.

Żeby zweryfikować, czy implementacja jest prawidłowa, odpal wszystkie mikroserwisy, zaczekaj około 30 sekund (rejestracja w Eurece),
a następnie uruchom test [End to End](./coupon-service/src/test/java/pl/punktozaur/coupon/CreateCouponEndToEndTest.java).
Oczywiście w taki sposób nie testuje się relacji między usługami, ten test jest tylko po to, żeby sprawdzić, czy udało się wykonać zadanie.

Do twojej dyspozycji jest kolekcja Postman, żeby poweryfikować różne scenariusze.

### Powodzenia!