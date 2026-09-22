# InterviewInsights- A platform for sharing Interview Experiences

**Live Website:** https://interviewinsights-frontend.onrender.com

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
### Frontend

- React
- Vite
- JavaScript
- HTML
- CSS

### Backend

- Java 21
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- Maven

### Database

- PostgreSQL

### AI

- Google Gemini API
- Text Embeddings

### Deployment

- Docker
- Render
- Supabase PostgreSQL

## Screenshots

### Home Page

<img width="1917" height="612" alt="Homepage" src="https://github.com/user-attachments/assets/fc3d9c3e-c889-4bc9-bd7e-b0fc2cf661e7" />


### User Registration
<img width="777" height="895" alt="RegistrationPage" src="https://github.com/user-attachments/assets/329533b8-c116-4019-a6e5-d67b3b81c778" />


### User Login

<img width="1077" height="817" alt="LoginPage" src="https://github.com/user-attachments/assets/001eafc3-83b0-45e2-a70c-96a570bfbbe6" />


### Browse Companies

<img width="1913" height="772" alt="ListOfCompaniesPage" src="https://github.com/user-attachments/assets/31c06a2a-6426-4985-ac32-808c0a3d360c" />


### Company Wise Experiences
<img width="1912" height="698" alt="SpecificCompanyExperiencesPage" src="https://github.com/user-attachments/assets/a71fc764-7deb-4afe-9983-44475b42caeb" />


### Experience Details
<img width="1886" height="892" alt="SpecificCompanyUserExperiencePage" src="https://github.com/user-attachments/assets/dc9a6ea9-5231-46bd-a1dc-56789442d6e3" />

<img width="1898" height="898" alt="SpecificCompanyUserExperiencePage2" src="https://github.com/user-attachments/assets/fb1e471f-2656-4ee3-84d8-52cf213b8940" />


### Submit Interview Experience

<img width="825" height="912" alt="ExperienceSubmittionForm" src="https://github.com/user-attachments/assets/d5a6112e-fe03-40f0-a633-8d37a1a4fdbd" />


### Question Filters

<img width="1905" height="677" alt="ViewComapanyWiseQuestions1" src="https://github.com/user-attachments/assets/bf18c9de-466c-4883-81e7-16c2ce3571f8" />


### Company Wise Questions
<img width="1877" height="926" alt="ViewCompanyWiseQuestions2" src="https://github.com/user-attachments/assets/f5207071-bfcd-43e6-803f-4973804aca36" />


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

### 3. Configure Backend Environment Variables
   Go to the backend directory , create an .env file<br>
   Open .env and replace the placeholder values with your own:<br><br>
   DB_URL=jdbc:postgresql://localhost:5432/interview_insights<br>
   DB_USERNAME=postgres<br>
   DB_PASSWORD=your_postgres_password<br>
   JWT_SECRET=your_jwt_secret<br>
   GEMINI_API_KEY=your_gemini_api_key<br>

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
