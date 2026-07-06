# InterviewInsights- A platform for sharing Interview Experiences

InterviewInsights is a web application that helps students share and explore interview experiences. Users can submit their interview experiences, browse experiences from different companies, and discover frequently asked interview questions. The application uses AI to extract interview questions from submitted experiences and organize them to help students prepare for future interviews.

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- PostgreSQL
- HTML
- CSS
- JavaScript
- Gemini API

## Features

- **User Authentication:** Users can register and log in securely using JWT-based authentication.
- **Interview Experience Submission:** Students can submit detailed interview experiences, including company, interview year, result, and round-wise experiences.
- **Company-wise Experiences:** Browse interview experiences shared by students for different companies.
- **AI-Powered Question Extraction:** Automatically extracts interview questions from submitted experiences using the Gemini API.
- **AI-Based Duplicate Question Detection:** Uses text embeddings to identify similar interview questions and avoid duplicate entries.
- **Question Frequency Tracking:** Maintains the frequency of interview questions to identify commonly asked questions across companies.
- **Company, Batch, and Department Management:** Supports management of companies, batches, and departments for organized data.
- **RESTful APIs:** Provides REST endpoints for managing users, interview experiences, companies, questions, batches, and departments.

  ## How It Works

1. Users register and log in using JWT authentication.
2. Students submit their interview experiences.
3. The experience is saved immediately in the database.
4. Interview questions are extracted in the background using the Gemini API.
5. Similar questions are identified using embeddings, and duplicate questions are merged by increasing their frequency.
6. Extracted questions are stored and linked to the corresponding interview experience.
7. Students can browse interview experiences and view frequently asked interview questions.

## Setup

### Requirements

- Java 21
- Maven
- PostgreSQL
- Gemini API Key

### Steps

1. Clone this repository.

```bash
git clone https://github.com/jayakrishna/InterviewInsights.git
cd InterviewInsights
```

2. Open the project in IntelliJ IDEA and allow Maven to download the required dependencies.
 
3. Create a PostgreSQL database (for example, `interview_insights`).

4. Update `src/main/resources/application.properties` with your PostgreSQL credentials and Gemini API key.

5. Run the `InterviewInsightsApplication` class.

6. Open the application in your browser at:

http://localhost:8080
 
