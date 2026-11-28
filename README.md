# Mistake Museum

### Every mistake is an opportunity to learn.

**Mistake Museum** is an AI-powered learning platform designed to help students understand, organize, and learn from their academic mistakes.

Instead of simply marking an answer as correct or incorrect, Mistake Museum helps students explore *why* they made a mistake, identify areas that need improvement, and practise with questions tailored to their learning needs.

By combining AI-assisted analysis, mistake classification, progress tracking, and personalized practice, the platform turns mistakes into opportunities for improvement.

**Live Application:** https://mistake-museum-production.up.railway.app

**GitHub Repository:** https://github.com/ZainabSaeed0cs/mistake-museum

---

## 1. The Problem

Students often repeat the same academic mistakes because they do not fully understand why their answers were incorrect.

Traditional learning platforms commonly focus on providing the correct answer without helping students identify the underlying issue.

As a result:

- Students struggle to recognize recurring mistakes.
- Conceptual misunderstandings remain unresolved.
- Students may practise without targeting their weak areas.
- Tracking academic improvement becomes difficult.

## 2. Our Solution

Mistake Museum provides a centralized platform where students can submit incorrect answers, receive AI-assisted explanations, track recurring mistakes, and practise based on their learning gaps.

The platform focuses not only on identifying incorrect answers but also on helping students understand and learn from them.

## 3. Key Features

### AI-Powered Mistake Analysis

- Submit answers using text or image uploads.
- Provide the original question, attempted answer, and correction.
- Use Google Gemini to analyze submitted work.
- Identify possible reasons behind mistakes.
- Classify mistakes into meaningful categories.
- Extract readable content from submitted images where supported.

### Mistake Classification

Mistakes are organized into categories to help students understand the nature of their errors.

| Category | Description |
|---|---|
| Concept Gap | The student has not fully understood the underlying concept. |
| Misread Question | The question was misunderstood or interpreted incorrectly. |
| Careless Slip | An avoidable calculation or writing error occurred. |
| Formula Misuse | An incorrect formula or method was applied. |
| Incomplete Answer | The response did not fully address the question. |
| None | No specific mistake category was identified. |

### Student Dashboard

- View the total number of analyzed answers.
- Explore mistake category breakdowns.
- Identify recurring mistakes and topics to revisit.
- Review weekly mistake trends through a visual chart.
- Access answer history and practice-related progress.

### Practice Room

- Generate practice questions based on selected subjects and topics.
- Choose difficulty levels and the number of questions.
- Generate additional question sets.
- Reveal answers to generated questions.
- Save generated questions to the Answer Library.
- Record practice progress using the **Got it** and **Missed it** options.

### Answer History and Answer Library

- Review previously analyzed answers.
- Revisit mistakes and their explanations.
- Save practice questions for later review.
- Organize learning material for continued practice.

### Practice Results Tracking

- Record whether a student understood or missed a practice question.
- Track the total number of questions answered.
- Track the number of correct practice responses.
- Display practice summaries on the dashboard.

### Account Management

- Student registration and login.
- Secure authentication and logout.
- Delete My Data functionality to remove a student's stored account-related learning data.

### Additional Features

- Image-based answer submissions.
- Browser-supported voice input and narration.
- Responsive user interface.
- Student-specific learning progress.

---

## 4. Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Backend programming language |
| Spring Boot 4.2.0 | Application framework |
| Spring Web MVC | Web requests and controllers |
| Thymeleaf | Server-side HTML rendering |
| Spring Data JPA | Database operations |
| Spring Security | Authentication and security |
| Bean Validation | Input validation |
| Maven | Build and dependency management |
| HTML, CSS, JavaScript | Frontend development |
| Chart.js 4.4.1 | Dashboard visualizations |
| Google Gemini API | AI-powered mistake analysis and practice question generation |
| Web Speech API | Browser-supported speech recognition and narration |

### Database Configuration

Mistake Museum uses different databases for development and production:

- **Local Development:** H2 Database
- **Production Deployment (Railway):** PostgreSQL

Spring Data JPA is used to manage database operations.

---

## 5. How It Works

1. **Register or Log In:** Students create an account and access their personal dashboard.
2. **Submit an Answer:** Students provide a question and their attempted answer through text or image submission.
3. **AI Analysis:** The application uses Google Gemini to analyze the submitted work.
4. **Identify Mistakes:** The system provides an explanation and identifies a possible mistake category.
5. **Track Progress:** Results are stored and reflected in the student's dashboard.
6. **Practise Weak Areas:** Students generate practice questions based on selected subjects and topics.
7. **Review and Improve:** Students revisit analyzed answers and saved practice questions to strengthen their understanding.

---

## 6. AI Integration

Mistake Museum integrates the **Google Gemini API** to support its learning and analysis features.

AI capabilities include:

- Analyzing submitted answers.
- Extracting readable content from images.
- Identifying possible reasons for incorrect answers.
- Suggesting mistake categories.
- Providing explanatory feedback.
- Generating topic-focused practice questions.

The Gemini integration is configurable through the following environment variables:

- `GEMINI_API_KEY`
- `GEMINI_MODEL`

A stub diagnosis service is also available through the application's `stub` profile for offline or fallback testing.

**Important:** AI-generated feedback is intended as a learning aid. It may occasionally be inaccurate, so students should independently review explanations and corrections.

---

## 7. Getting Started

### Prerequisites

Before running the application locally, make sure you have:

- Java Development Kit (JDK) 17
- Git
- An internet connection for Gemini-powered functionality
- A Google Gemini API key
- A terminal such as PowerShell or Bash

Maven Wrapper is included in the repository, so a separate Maven installation is not required.

### Clone the Repository

```bash
git clone https://github.com/ZainabSaeed0cs/mistake-museum.git
cd mistake-museum
```

### Configure Environment Variables

Set your Gemini API credentials before starting the application.

**Windows PowerShell:**

```powershell
$env:GEMINI_API_KEY="your_gemini_api_key"
$env:GEMINI_MODEL="gemini-3.5-flash-lite"
```

Replace the placeholder API key with your own key. Keep API credentials private and never commit them to GitHub.

### Run the Application

**Windows:**

```powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux:**

```bash
./mvnw spring-boot:run
```

Once the application starts, open:

http://localhost:8080

The application uses H2 for local development.

---

## 8. Running Tests

Run the project's test suite using Maven Wrapper.

**Windows:**

```powershell
.\mvnw.cmd clean test
```

**macOS / Linux:**

```bash
./mvnw clean test
```

To clean and compile the project on Windows:

```powershell
.\mvnw.cmd clean compile
```

---

## 9. API Endpoints

The application provides endpoints for selected application operations, including:

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/practice/result` | Save a student's practice result |
| GET | `/api/practice/summary` | Retrieve a student's practice summary |
| DELETE | `/api/me` | Delete the authenticated student's account data |

These endpoints support practice tracking and account management.

---

## 10. Deployment

Mistake Museum is deployed on **Railway**.

- **Hosting Platform:** Railway
- **Production Database:** PostgreSQL
- **Source Repository:** GitHub

**Live Application:** https://mistake-museum-production.up.railway.app

The production deployment is configured separately from the local H2 development environment.

---

## 11. Privacy and Responsible AI Use

- Student accounts and learning records are managed through the application.
- Gemini API credentials are configured through environment variables rather than committed source code.
- Submitted content may be sent to Google Gemini for AI-powered analysis and generation.
- AI-generated explanations may contain errors and should be reviewed independently.
- Students should avoid submitting unnecessary personal or sensitive information.

---

## 12. Future Improvements

Potential areas for further development include:

- More detailed learning analytics and progress insights.
- Improved mistake classification and evaluation.
- More personalized practice recommendations.
- Expanded subject and topic coverage.
- Multilingual support and accessibility improvements.
- More detailed explanations and learning resources.

---

## 13. Team Contributions

Mistake Museum was developed collaboratively by a five-member team.

| Team Member | Contribution |
|---|---:|
| Manahil Wali | 39% |
| Nabeeha Sajid | 38% |
| Zainab Saeed | 12% |
| Laiba Talib | 7% |
| Zalish | 4% |
| **Total** | **100%** |

### Areas of Contribution

- **Manahil Wali:** Backend and Gemini integration, profile and saved-question functionality, dashboard integration, database work, deployment and team integration.
- **Nabeeha Sajid:** Frontend pages, authentication, Spring Security, student and attempt services, database models and repositories.
- **Zainab Saeed:** Practice Room frontend, practice controller and service, health endpoint, project setup and documentation.
- **Laiba Talib:** Delete My Data functionality, practice results tracking, database entity and repository, frontend integration and account-deletion testing.
- **Zalish:** Account and data deletion services, Gemini practice question generation, student service, database models and repositories, and testing.

**Contribution Note:** The contribution percentages are based on lines of code added, excluding auto-generated files. They measure the volume of code contributed, not the difficulty or impact of the work. All team members contributed to design decisions, testing, and debugging throughout the project.

---

## 14. AI Assistance Disclosure

AI coding assistants were used during development within the project window. The team implemented, integrated, and tested the application's core logic, configuration, and integrations.

---

## 15. Project Summary

Mistake Museum aims to make learning more effective by helping students understand their mistakes instead of simply moving past them.

By combining AI-assisted analysis, structured mistake classification, personalized practice, and progress tracking, it turns academic mistakes into meaningful learning opportunities.

**Mistake Museum — Learn from every mistake. Improve with every attempt.**
