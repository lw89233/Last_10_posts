# Last_10_posts

## Opis Projektu

Testowa aplikacja CNApp oparta na protokole SSMMP. Interfejsem użytkownika jest klient wiersza poleceń (CLI), który komunikuje się z systemem mikrousług poprzez API Gateway. Żądania i odpowiedzi przesyłane są w formie obiektów klasy String, a cała architektura jest bezstanowa.

## Rola Komponentu

Ta mikrousługa odpowiada za pobieranie ostatnich 10 postów dodanych przez wszystkich użytkowników. Odbiera żądania typu `retrive_last_10_posts_request` od API Gateway, wykonuje zapytanie do bazy danych, a następnie zwraca listę postów.

## Konfiguracja

Ten komponent wymaga następujących zmiennych środowiskowych w pliku `.env`:

LAST_10_POSTS_MICROSERVICE_PORT=

DB_HOST=

DB_PORT=

DB_NAME=

DB_USER=

DB_PASSWORD=


## Uruchomienie

Serwis można uruchomić, wykonując główną metodę `main` w klasie `Last_10_posts.java`