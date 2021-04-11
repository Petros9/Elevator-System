# Elevator-System
Prosty system do zarządzania windami.

System można rozdzielić na dwie składowe -> część obsługi zamówień z piętra oraz część zarządzania windami. Działają one jako osobne procesy i porozumiewają się za pomocą gniazd TCP poprzez sieć lokalną.

### Zamówienia z piętra

Część składa się z pojedyńczej klasy `Floor`. Na samym początku działania tworzone jest gniazdo TCP, pobierany jest numer piętra (w tym przypadku z konsoli) oraz uruchamiane są wątki odpowiedzialne za przywoływanie windy oraz otrzymywanie wiadomości do jakiej windy należy się udać.
