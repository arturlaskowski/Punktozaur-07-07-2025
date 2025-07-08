Zaimplementuj wzorzec architektoniczny CQRS w domenie Coupon, wykorzystując wzorzec Mediator, tak aby
[CouponController](src/main/java/pl/punktozaur/coupon/web/CouponController.java) tworzył komendy (command) i wysyłał je do mediatora.
Mediator ma za zadanie delegować te komendy do odpowiednich command handlerów.
Do projektu są już dodane klasy obsługujące Mediatora w common: [pakient-command](src/main/java/pl/punktozaur/common/command)
Przykład implementacji znajdziesz w projekcie `kopytka` na branchu `cqrs-command-handler`.

W testach architektury [ArchitectureTest](src/test/java/pl/punktozaur/architecture/ArchitectureTest.java) weryfikowane jest, czy `CouponController` nie ma zależności do `CouponService`.
Po tej implementacji obserwowane zachowanie aplikacji (request/response) nie powinno się zmienić. W testach może być konieczne dostosowanie DTO wykorzystywanych w metodach pomocniczych oraz zmiana wywołań z serwisów na handlery.

Powodzenia!