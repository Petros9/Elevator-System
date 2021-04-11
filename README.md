# Elevator-System
Prosty system do zarządzania windami.

System można rozdzielić na dwie składowe -> część obsługi zamówień z piętra oraz część zarządzania windami. Działają one jako osobne procesy i porozumiewają się za pomocą gniazd TCP poprzez sieć lokalną. Założyłem, że taki sposób komunikacji może okazać siębardziej uniwersalny i nie będzie wymagał np. ciągnięcia kabli po całym budynku. 

### Zamówienia z piętra

Część składa się z pojedyńczej klasy `Floor`. Na samym początku działania tworzone jest gniazdo TCP, pobierany jest numer piętra (w tym przypadku z konsoli) oraz uruchamiane są wątki odpowiedzialne za przywoływanie windy oraz otrzymywanie wiadomości do jakiej windy należy się udać.

Założyłem, że na każdym piętrze znajduje się jeden osobny panel podpięty do sieci lokalnej, na którym każdy może podać piętro, na które chce się udać. System zarządzający windami wyznacza określoną windę, po czym użytkownik jest informowany, że np. `na piętro nr 5 zawiezie go winda nr 8`.

Wątek o nazwie `sendRequest` ma za zadanie w nieskończonej pętli pobierać przywołania wind. Wątek o nazwie `getResponse` ma za zadanie informować użytkownika do jakiej ma się udać.


### Zarządzanie windami

Część składa się z poszczególnych klas:

- Elevator Panel (pełni funkcję serwera TCP),
- FloorHandelrThread (wątek służący do porozumiewania się z poszczególnymi piętrami),
- ElevatorDistrubutor (klasa służąca do przydzielania windom zadań oraz wybierania numery windy dla użytkowanika),
- Elevator (klasa reprezentująca windę).

