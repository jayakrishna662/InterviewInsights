# InterviewInsights- A platform for sharing Interview Experiences

**Live Website:** https://interview-insights-ebon.vercel.app/

InterviewInsights is a web application that helps students share and explore interview experiences. Students can submit their interview experiences, browse experiences from different companies, and discover frequently asked interview questions. The application uses AI to extract interview questions from submitted experiences, identify similar questions, and maintain question frequency so that commonly asked questions can be discovered more easily.

## Features

- **User Authentication:** Users can register and log in securely using JWT-based authentication.
- **Password Security:** User passwords are securely hashed using BCrypt before being stored in the database.
- **Interview Experience Submission:** Students can submit detailed interview experiences, including company, interview year, result, and round-wise experiences.
- **Company-wise Experiences:** Browse interview experiences shared by students for different companies.
- **AI-Powered Question Extraction:** Automatically extracts interview questions from submitted experiences using the Gemini API.
- **AI-Based Duplicate Question Detection:** Uses text embeddings to identify similar interview questions and reduce duplicate entries.
- **Question Frequency Tracking:** Tracks the frequency of interview questions to identify commonly asked questions.
- **Company, Batch, and Department Management:** Supports management of companies, batches, and departments.
- **Admin Dashboard:** Provides administrators with tools to manage application data and monitor AI processing.
- **RESTful APIs:** Provides REST APIs for authentication, interview experiences, companies, questions, batches, and departments.

## Tech Stack

| Category | Technologies |
|----------|--------------|
| **Frontend** | React, Vite, JavaScript, HTML, CSS |
| **Backend** | Java 21, Spring Boot, Spring Security, JWT, Spring Data JPA, Hibernate, Maven |
| **Database & Migrations** | PostgreSQL, Flyway |
| **AI** | Google Gemini API, Text Embeddings |
| **Deployment & Infrastructure** | Docker, Vercel, Supabase |

## Screenshots

### Home Page

<img width="1677" height="722" alt="image" src="https://github.com/user-attachments/assets/61adaa14-1a4a-4cd0-8cdb-ba20a8a0b004" />



### User Registration
<img width="552" height="782" alt="image" src="https://github.com/user-attachments/assets/3095cb27-074c-4fc7-a21a-f01be53aee59" />



### User Login

<img width="670" height="542" alt="image" src="https://github.com/user-attachments/assets/175d2e9e-f205-49ee-9a0a-2b18a45c8f31" />


### Experience Details
<img width="1300" height="561" alt="image" src="https://github.com/user-attachments/assets/11370a48-006e-4abb-958d-2d8ac1744dff" />
<img width="1290" height="686" alt="image" src="https://github.com/user-attachments/assets/8bebd070-a1e1-4bd7-9685-f0ad41a9f140" />


### Submit Interview Experience

<img width="683" height="797" alt="image" src="https://github.com/user-attachments/assets/20be04e0-bdfc-42b0-be1e-cf5b74526802" />


### Question Filters
<img width="1501" height="546" alt="image" src="https://github.com/user-attachments/assets/33b76d8d-3937-451a-a7db-e3208b1f940c" />


### Company Wise Questions
<img width="1198" height="778" alt="image" src="https://github.com/user-attachments/assets/aff2d8cc-a0e9-4e0a-ad85-338fcd77efae" />

 ## How It Works

1. Users register and log in using JWT authentication.
2. Students submit their interview experiences.
3. The interview experience is saved in the database.
4. Interview questions are extracted in the background using the Gemini API.
5. Similar questions are identified using text embeddings, and duplicate questions are handled by updating their frequency.
6. Extracted questions are stored and linked to the corresponding interview experience.
7. Students can browse interview experiences and discover frequently asked interview questions.

## Setup

**Prerequisites**
  - Java 21
  - Maven
  - Node.js and npm
  - PostgreSQL
  - Gemini API key

 ### 1. Clone the Repository

```bash
git clone https://github.com/jayakrishna662/InterviewInsights.git
cd InterviewInsights
```
### 2. Configure PostgreSQL

Create a PostgreSQL database for the application.

Make sure PostgreSQL is running before starting the backend

### 3. Configure the Backend

Open the following file:

```text
backend/src/main/resources/application.properties
```

For local development, replace the environment variable placeholders with your local PostgreSQL credentials, JWT secret, and Gemini API key.

Update the following properties:

```properties
jwt.secret=your_jwt_secret

spring.datasource.url=jdbc:postgresql://localhost:5432/interview_insights
spring.datasource.username=postgres
spring.datasource.password=your_postgres_password

gemini.api.key=your_gemini_api_key
```

Replace the placeholder values with your own:

- `your_jwt_secret` — A strong secret key used for JWT authentication.
- `postgres` — Your PostgreSQL username.
- `your_postgres_password` — Your PostgreSQL password.
- `your_gemini_api_key` — Your Google Gemini API key.

The remaining properties in `application.properties` can be left unchanged.

### 4. Run the Backend
```bash
mvnw.cmd spring-boot:run
```
The backend will start on:
```bash
http://localhost:8080
```
### 5. Run the Frontend
Open a new terminal and navigate to the frontend directory <br>
Install the required dependencies: 
```bash
npm install
```
Start the development server: 
```bash
npm run dev
```
Vite will display the frontend URL in the terminal. By default, it will be available at:
```bash
http://localhost:5173
```
