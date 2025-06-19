# 🍲 Kukino – System Informatyczny do Zarządzania Przepisami

Projekt zespołowy realizowany w ramach przedmiotu **"Projekt zespołowy systemu informatycznego"**. Celem było stworzenie pełnoprawnej aplikacji webowej do zarządzania przepisami kulinarnymi z wykorzystaniem technologii backendowych, baz danych oraz interfejsu użytkownika.

---

## 👥 Skład zespołu
- Paweł Pawłowski (20998)
- Mateusz Szczęch (20331)
- Emilia Tymosiewicz (16728)

---

## 🎯 Główne funkcjonalności

- 🔐 **Rejestracja i logowanie użytkowników (BCrypt + HttpSession)**
- 👤 **Panel użytkownika** z listą ulubionych i własnych przepisów
- 📝 **Dodawanie, edytowanie i usuwanie przepisów kulinarnych**
- 🔍 **Wyszukiwanie przepisów po tytule i składnikach**
- 📈 **Lista najpopularniejszych przepisów (Redis cache)**
- 🛠️ **Panel administratora** z zarządzaniem użytkownikami i przepisami

---

## 🧰 Technologie i biblioteki

| Warstwa | Technologia |
|--------|-------------|
| Backend | Java 23, Spring Boot 3 |
| Baza danych | MongoDB (NoSQL) |
| UI | HTML5, CSS3, Bootstrap |
| API docs | Swagger / OpenAPI |
| Logowanie | Spring Security + HttpSession |
| Build | Maven |
| Deployment | Spring Boot JAR |
| Repozytorium | GitHub |

---

## 🧠 Uzasadnienie technologii

Wybrano nierelacyjne bazy danych, ponieważ struktura przepisu (dynamiczna lista składników, instrukcje, daty) lepiej pasuje do elastycznego modelu dokumentowego MongoDB. Redis zapewnia natychmiastowy dostęp do najpopularniejszych przepisów, zwiększając wydajność aplikacji.

---

## 🖥️ Uruchamianie aplikacji

### 🔧 Wymagania:
- Java 17+ (lub Java 23)
- MongoDB (`localhost:27017`)
- Maven (`mvn` w konsoli)

### 🚀 Kroki:

1. Sklonuj repozytorium:
   ```bash
   git clone https://github.com/N1hoo/Kukino.git
   cd 
2. Uruchom bazę MongoDB i Redis.

3. Zbuduj aplikację:
  mvn clean install

5. Uruchom:
  mvn spring-boot:run

6. Otwórz w przeglądarce:
  http://localhost:8080/

## 🗂️ Struktura katalogów
  przepisy/
  ├── src/
  │   ├── main/
  │   │   ├── java/com/n1hoo/Kukino/
  │   │   │   ├── controllers/
  │   │   │   ├── model/
  │   │   │   ├── repository/
  │   │   │   └── service/
  │   │   └── resources/
  │   │       ├── static/        # HTML, CSS, JS
  │   │       └── application.properties
  ├── pom.xml
  └── README.md

## 📄 Licencja
  Projekt stworzony wyłącznie do celów edukacyjnych w ramach przedmiotu „Projekt zespołowy systemu informatycznego”.
