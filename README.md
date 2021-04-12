# Elevator-System
Prosty system do zarządzania windami.

System można rozdzielić na dwie składowe -> część obsługi zamówień z piętra oraz część zarządzania windami. Działają one jako osobne procesy i porozumiewają się za pomocą gniazd TCP poprzez sieć lokalną. Założyłem, że taki sposób komunikacji może okazać siębardziej uniwersalny i nie będzie wymagał np. ciągnięcia kabli po całym budynku. 

### Zamówienia z piętra

Część składa się z pojedyńczej klasy `Floor`. Na samym początku działania tworzone jest gniazdo TCP, pobierany jest numer piętra (w tym przypadku z konsoli) oraz uruchamiane są wątki odpowiedzialne za przywoływanie windy, jak i otrzymywanie wiadomości do jakiej windy należy się udać.

Założyłem, że na każdym piętrze znajduje się jeden osobny panel podpięty do sieci lokalnej, na którym każdy może podać docelowe piętro. System zarządzający windami wyznacza określoną windę, po czym użytkownik dostaje przykładową informajcę `na piętro nr 5 zawiezie Ciebie winda nr 8`.

* **Wątek:** `sendRequest`->
Wątek ma za zadanie w nieskończonej pętli pobierać przywołania wind. 

* **Wątek:** `getResponse` ->
Wątek ma za zadanie informować użytkownika do jakiej ma się udać.


### Zarządzanie windami

Część składa się z poszczególnych klas:

- ElevatorPanel (pełni funkcję serwera TCP),
- FloorHandlerThread (wątek służący do porozumiewania się z poszczególnymi piętrami),
- ElevatorDistrubutor (klasa służąca do przydzielania windom zadań oraz wybierania numery windy dla użytkowanika),
- Elevator (klasa reprezentująca windę).

***ElevatorPanel*** 
Klasa pełni funkcję gniazda serwera. W zamyśle jest to program znajdujący się na urządzeniu, które bezpośrednio staruje windami.
W klasie znajduje się również mapa o wartościach: (numer piętra, wątek obsługujący komunikację z tym piętrem). Mapa jest pomocna podczas sprawdzania, czy w danym systemie istnieje piętro o danym numerze.
* **Metoda:** `main`->
Główna metoda programu. Ustawiana jest w niej ilość wind (przy użyciu stałej `ELEVATORS_QUANTITY`) oraz uruchamiany jest gniazdo serwera TCP.
Dla każdego nowego połączenia (w domyśle dla każdego podłączonego piętra) tworzony jest osobny wątek `FloorHandlerThread`, który odpowiada za komunikację.
W metodzie tworzony jest również obiekt `ElevatorDistributor`, który odpowiada za bezpośredni przydział wind do zadań.

* **Wątek:** `statusChecker`->
Wątek ma za zadanie w nieskończonej pętli pobierać zapytania o stan konkretnej windy. Po podaniu ID wyświetlane są: { jej numer, aktualne położenie, piętro na które winda zmierza }.

 
***FloorHandlerThread***
Klasa działająca jako osobne wątki, która jest odpowiedzialna za komunikację z określonym piętrem.

* **Metoda:** `sendMessage`-> Metoda ma za zadanie wysyłać do procesu danego piętra wiadomości zwrotne (np. numer windy która pojedzie na konkretne piętro).

* **Metoda:** `run`-> Metoda w nieskończonej pętli nasłuchuje nadchodzące komunikaty z piętra. Po otrzymaniu numeru sprawdza czy w systemie znajduje się dane piętro oraz czy użytkownik nie już nie znajduje się na piętrze docelowym.
Jeśli nie, to zwracany jest wynik metody `getNumber` z obiektu `elevatorDistributor`.

***ElevatorDistributor***
Klasa bezpośrednio zarządza windami. Wyznacza im cele oraz pobiera ich informacje.

* **Metoda:** `generateElevators`-> Metoda ma za zadanie wygenerować odpowiednią ilość wind.
* **Metoda:** `calculateDistance`-> Metoda ma za zadanie ustalić jaką drogę ma do przebycia winda zanim będzie mogła wykonać określone zadanie. Zdecydowałem się na prostrzy algorymt, który zakłada, że winda ma kolejkę zadań do wykonania.
* **Metoda:** `chooseTransportingElevatorNumber`-> Metoda ma za zadanie wybranie windy, która będzie transportowała użytkownika. Jeżeli winda w danym momencie jest bezczynna, to jest wybierana jako pierwsza do wykonania zadania. Jeśli w systemie nie ma takich wind, to wybierana jest taka, która ma najmniejszy dystans do wykonania względem piętra, gdzie zamawiana jest winda.
Jeśli jakaś winda udaje się na dane piętro, to system nie zamawia nowej. Zakładam, że winda wykonuje najpierw wszystkie swoje zadania i dopiero potem udaje się na wyznaczone piętro.
* **Metoda:** `getElevatorStatus`-> Metoda ma za zadanie podać informacje na temat windy takie jak: { jej numer, aktualne piętro przebywania, piętro do którego winda zmierza }
* **Metoda:** `getNumber`-> Metoda ma za zadanie zwrócić wątkowi numer windy do jakiej ma się udać użytkownik. Aby nie nastąpiły potencjalne aberracje, użyty jest system synchronizacyjny.


***Elevator***
Klasa ma za zadanie reprezentować pojedyńczą windę. Posiada jej numer, kolejkę zadań, aktualne piętro oraz aktualna destynację.
Aby zapewnić każdej windzie autonomię realizowane są one jako osobne wątki.
* **Metody:** `gettery i settery`-> Wydaje mi się, że ich nazwy intuicyjnie tłumaczą za co dana metoda służy.
* **Metoda:** `getWholeDistance`-> Metoda ma za zadanie zwrócić ile pięter musi dana winda pokonać, aby wypełnić swoje zadania.
* **Metoda:** `run`-> Metoda ma za zadanie w nieskończonej pętli reprezentować ruch windy. Trwanie poszczególnych akcji wyznaczają stałe: { ARRIVAL_TIME, WAITING_TIME, MOVE_TIME }.
Jeśli winda właśnie przybyła na okreslone piętro, to usuwane jest z początku jej kolejki zadanie, jeśli winda nie ma aktualnie zadań, to wchodzi w stan oczekiwania
, w innych przypadkach winda udaje się do miejsca swojej destynacji. 