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
- **Password Security:** User passwords are securely hashed using BCrypt before being stored in the database.
- **Interview Experience Submission:** Students can submit detailed interview experiences, including company, interview year, result, and round-wise experiences.
- **Company-wise Experiences:** Browse interview experiences shared by students for different companies.
- **AI-Powered Question Extraction:** Automatically extracts interview questions from submitted experiences using the Gemini API.
- **AI-Based Duplicate Question Detection:** Uses text embeddings to identify similar interview questions and avoid duplicate entries.
- **Question Frequency Tracking:** Maintains the frequency of interview questions to identify commonly asked questions across companies.
- **Company, Batch, and Department Management:** Supports management of companies, batches, and departments for organized data.
- **RESTful APIs:** Provides REST endpoints for managing users, interview experiences, companies, questions, batches, and departments.

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
 
