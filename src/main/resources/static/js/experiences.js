requireAuthentication();
async function loadCompanies() {

   const response = await fetch("/api/companies", {
       headers: getAuthHeaders()
   });

    const companies =
            await response.json();

    const content =
                    document.getElementById("content");

    content.innerHTML = "";

    companies.forEach(company => {

      content.innerHTML += `
          <div
              class="company-card"
              onclick="loadExperiences(${company.id})">

              ${company.companyName}

          </div>
      `;

            });

    console.log(companies);
}

window.loadExperiences =
    async function(companyId) {

        const response =
                await fetch(
                    `/api/interview-experiences/company/${companyId}`,
                    {
                        headers: getAuthHeaders()
                    }
                );

        const experiences =
                await response.json();

        const content =
                        document.getElementById("content");

                content.innerHTML = "";

        experiences.forEach(experience => {

            content.innerHTML += `
        <div
            class="company-card"
            onclick="loadExperienceDetails(${experience.id})">

            ${experience.userName}
            -
            ${experience.interviewYear}
            -
            ${experience.result}

        </div>
            `;

        });

        console.log(experiences);

    };



window.loadExperienceDetails =
        async function(experienceId) {

            const response =
                    await fetch(
                        `/api/interview-experiences/${experienceId}`,
                        {
                            headers: getAuthHeaders()
                        }
                    );

            const experience =
                    await response.json();

                    const content =
                            document.getElementById("content");

                    content.innerHTML = "";

            content.innerHTML = `
                <h2>${experience.companyName}</h2>

                <p>
                    <strong>Result:</strong>
                    ${experience.result}
                </p>

                <p>
                    <strong>Aptitude Experience:</strong><br>
                    ${experience.aptitudeExperience}
                </p>

                <p>
                    <strong>Coding Experience:</strong><br>
                    ${experience.codingExperience}
                </p>

                <p>
                    <strong>Technical Experience:</strong><br>
                    ${experience.technicalExperience}
                </p>

                <p>
                    <strong>HR Experience:</strong><br>
                    ${experience.hrExperience}
                </p>

                <p>
                    <strong>GD Experience:</strong><br>
                    ${experience.gdExperience}
                </p>

                <p>
                    <strong>Overall Suggestions:</strong><br>
                    ${experience.overallSuggestions}
                </p>
            `;

            // Fetch and display experience questions
           const questionsResponse =
                   await fetch(
                       `/api/experience-questions/experience/${experienceId}`,
                       {
                           headers: getAuthHeaders()
                       }
                   );
            const experienceQuestions =
                    await questionsResponse.json();

            content.innerHTML += `<h3>Questions</h3>`;

            experienceQuestions.forEach(eq => {
                content.innerHTML += `
                    <div class="company-card">
                        ${eq.questionText}
                    </div>
                `;
            });

            console.log(experienceQuestions);

        };


document.addEventListener("DOMContentLoaded", () => {
    loadCompanies();
});