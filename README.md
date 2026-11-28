# mistake-museum

# Mistake Museum

Mistake Museum is a Spring Boot web application for logging learning mistakes, diagnosing errors, and tracking study progress.

## Tech Stack & Libraries

- **Java 17** & **Apache Maven** (`./mvnw` / `mvnw.cmd`)
- **Spring Boot 4.2.0**
  - Spring Web MVC (`spring-boot-starter-webmvc`)
  - Thymeleaf (`spring-boot-starter-thymeleaf`)
  - Spring Data JPA (`spring-boot-starter-data-jpa`)
  - Spring Security (`spring-boot-starter-security`)
  - Bean Validation (`spring-boot-starter-validation`)
  - H2 Console (`spring-boot-h2console`)
  - Spring Boot Test Starters (`spring-boot-starter-data-jpa-test`, `spring-boot-starter-thymeleaf-test`, `spring-boot-starter-validation-test`, `spring-boot-starter-webmvc-test`)
- **H2 Database** (`com.h2database:h2`) — local/development database
- **PostgreSQL JDBC Driver** (`org.postgresql:postgresql`) — cloud/production database driver
- **Chart.js 4.4.1 (via jsDelivr CDN): Used to render the weekly mistakes bar chart on the dashboard.**

## External APIs & Browser Features

- **Google Gemini API** (`https://generativelanguage.googleapis.com/v1beta/models/`): Used by `GeminiDiagnosisService` when active to diagnose mistakes via `GEMINI_API_KEY` and `GEMINI_MODEL`. Also supports running with the `stub` profile (`StubDiagnosisService`) for offline or fallback testing.
- **Web Speech API (**`SpeechRecognition` **/** `webkitSpeechRecognition`**)**: Used in the browser interface for microphone voice narration/dictation (subject to browser support and microphone permissions).
- **Chart.js 4.4.1** (https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js`): Used to render the weekly mistakes bar chart on the dashboard.

## AI Assistance Disclosure

- AI coding assistants were used during development within the project window; all core application logic, configuration, and integrations were implemented and tested by our team.

## Running Locally

Set your environment variables in your terminal session and start the app:

```bash
# Windows (PowerShell)
$env:GEMINI_API_KEY="your_api_key_here"
$env:GEMINI_MODEL="gemini-2.5-flash-lite"
.\mvnw.cmd spring-boot:run
```