## Zadanie 5

Twoim zadaniem jest przesłanie za pośrednictwem Kafki wiadomości podczas tworzenia kuponu z **coupon service** do **loyalty service**, aby odjąć punkty.

Wiadomość powinna być wysłana na topic `loyalty-commands` (taki topic jest automatycznie tworzony w [docker-compose](infrastructure/docker-compose.yml)).

### Kroki do wykonania

1. **Zdefiniuj strukturę wiadomości (schemat Avro)**  
   Schemat należy umieścić w katalogu [src/main/resources/avro](common/src/main/resources/avro).

2. **Wygeneruj klasy Java**  
   Po zdefiniowaniu schematu Avro, w bibliotece `common` wykonaj polecenie:
   ```
   mvn compile
   ```
   Spowoduje to automatyczne wygenerowanie klas Java, które mogą być używane zarówno w producerze, jak i listenerze.

3. **Wyślij wiadomość podczas tworzenia kuponu**  
   Zaimplementuj wysyłanie wiadomości na topic `loyalty-commands` po utworzeniu kuponu.

4. **Zaktualizuj test akceptacyjny**  
   Zaktualizuj test akceptacyjny [CreateCouponAcceptanceTest](coupon-service/src/test/java/pl/punktozaur/coupon/acceptance/CreateCouponAcceptanceTest.java), aby weryfikował, czy wiadomość na Kafkę jest wysyłana po stworzeniu kuponu.

5. **Przetestuj działanie aplikacji**  
   Skorzystaj z dołączonej [kolekcji Postmana](punktozaur-2.postman_collection.json).

---

### Wskazówki

- **Zatrzymaj niepotrzebne kontenery Dockera z projktu kopytka**  

- **Uruchom infrastrukture**  [docker-compose](infrastructure/docker-compose.yml).

- **Czyszczenie danych**  
  Jeśli chcesz usunąć dane z bazy, topiki z Kafki i ubić wszystkie kontenery Docker, użyj skryptu [docker-clean.sh](infrastructure/docker-clean.sh).

- **GUI do Kafki**  
  Po odpaleniu pod adresem [http://localhost:8080/](http://localhost:8080/) dostępne jest GUI do Kafki z podłączonym Schema Registry. Możesz tam weryfikować, jakie wiadomości pojawiły się na danym topicu.

### Baza danych

Każda baza danych to osobny schemat, co zapewnia separację i oszczędza lokalnie trochę zasobów, a na środowisku wdrożeniowym pozwala używać osobnych baz danych.
Po zalogowaniu się do bazy jako użytkownik `admin_user` z hasłem `admin_password` (`jdbc:postgresql://localhost:5432/punktozaurdb`), masz dostęp do wszystkich schematów.

## PgAdmin

Po uruchomieniu będzie dostępny pod adresem:  
http://localhost:5050

Aby się zalogować do pgAdmin:
admin@admin.com
admin

Aby się zalogować do bazy:
- **Host address:** postgres
- **Port:** 5432
- **Username:** postgres
- **Password:** postgres
