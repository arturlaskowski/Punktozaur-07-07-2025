## Zadanie 6 

# Zaimplementuj wzorzec Saga dla procesu wydawania kuponu

## Happy Path

1. Podczas tworzenia kuponu wysyłana jest komenda do odjęcia punktów.
2. Po wysłaniu tej komendy tworzony jest kupon w statusie `PENDING`.
3. Serwis lojalnościowy (Loyalty) odbiera tę komendę i ją przetwarza.
4. Po pomyślnym odjęciu punktów, Loyalty wysyła event z informacją, że udało się odjąć punkty.
5. Na ten event nasłuchuje serwis kuponów (Coupon Service) i zmienia status kuponu na `ACTIVE`.  
   *(Od tej pory kupon może być używany.)*

## Kompensacja

1. Podczas tworzenia kuponu wysyłana jest komenda do odjęcia punktów.
2. Po wysłaniu tej komendy tworzony jest kupon w statusie `PENDING`.
3. Serwis lojalnościowy (Loyalty) odbiera tę komendę i ją przetwarza, ale nie udaje się odjąć punktów.
4. Loyalty wysyła event z informacją, że nie udało się odjąć punktów.
5. Na ten event nasłuchuje serwis kuponów (Coupon Service) i zmienia status kuponu na `REJECTED`.  
   *(Kupon nie będzie mógł być używany, ale powinna być dostępna informacja o przyczynie odrzucenia)*

Encja [Coupon](coupon-service/src/main/java/pl/punktozaur/coupon/domain/Coupon.java)  została już dostosowana i zawiera metody oraz statusy, które będą potrzebne do wykonania tego zadania.

Aby sprawdzić poprawność swojego Sagi, przy uruchomionych wszystkich mikroserwisach uruchom test:
[Saga Pattern Test](coupon-service/src/test/java/pl/punktozaur/coupon/SagaEndToEndTest.java).

Możliwość dodawania konta lojalnościowego przez REST API została usunięta, teraz jest to proces wewnętrzny podczas tworzenia customera.
Jednak odejmowanie punktów w lojalności może odbywać się także z innych powodów niż wydawanie kuponu (pamiętaj o tym, projektując sagę).

### Wskazówki

- **Jeśli chcesz stworzyć nowy topic dodaj go w [docker-compose](infrastructure/docker-compose.yml).**

- **Zatrzymaj niepotrzebne kontenery Dockera z projktu kopytka**

- **Uruchom infrastrukture**  [docker-compose](infrastructure/docker-compose.yml).

- **Czyszczenie danych**  
  Jeśli chcesz usunąć dane z bazy, topiki z Kafki i ubić wszystkie kontenery Docker, użyj skryptu [docker-clean.sh](infrastructure/docker-clean.sh).

- **GUI do Kafki**  
  Po odpaleniu pod adresem [http://localhost:8080/](http://localhost:8080/) dostępne jest GUI do Kafki z podłączonym Schema Registry. Możesz tam weryfikować, jakie wiadomości pojawiły się na danym topicu.

### Baza danych

Każda baza danych to osobny schemat, co zapewnia separację i oszczędza lokalnie trochę zasobów, a na środowisku wdrożeniowym pozwala używać osobnych baz danych.
Po zalogowaniu się do bazy jako użytkownik `admin_user` z hasłem `admin_password` (`jdbc:postgresql://localhost:5432/punktozaurdb`), masz dostęp do wszystkich schematów.