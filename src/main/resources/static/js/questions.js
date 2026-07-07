requireAuthentication();
async function loadViewCompanyWise() {
    const filtersDiv =
            document.getElementById("filters");
    const content =
            document.getElementById("content");

    // Fetch all companies for dropdown
    const companiesResponse =
            await fetch("/api/companies",
                {
                    headers: getAuthHeaders()
                }
            );

    const companies =
            await companiesResponse.json();

    // Build company dropdown options
    let companyOptions = `<option value="">Select Company</option>`;
    companies.forEach(company => {
        companyOptions += `
            <option value="${company.id}">
                ${company.companyName}
            </option>
        `;
    });


  // Display form with dropdowns
      filtersDiv.innerHTML = `
          <h2>View Company-Wise Questions</h2>

        <div>
            <label>Company:</label>
            <select id="companyFilter">
                ${companyOptions}
            </select>
        </div>

        <div>
            <label>Duration:</label>
            <select id="durationFilter">
                <option value="all">All time</option>
                <option value="1">1 year</option>
                <option value="2">2 years</option>
            </select>
        </div>

        <div>
            <label>Filter by Category:</label>
            <select id="categoryFilter">
                <option value="">All Categories</option>
                <option value="CODING">Coding</option>
                <option value="TECHNICAL">Technical</option>
                <option value="APTITUDE">Aptitude</option>
                <option value="GD">GD</option>
                <option value="HR">HR</option>
            </select>
        </div>

        <button id="applyFiltersBtn">
            Apply Filters
        </button>

        <div id="filteredResults"></div>
    `;

    // Add event listener to Apply button
    const applyBtn =
            document.getElementById("applyFiltersBtn");

    applyBtn.addEventListener(
        "click",
        applyCompanyFilter
    );

    console.log(companies);
}


async function applyCompanyFilter() {
    const companyId =
            document.getElementById("companyFilter").value;

    const duration =
            document.getElementById("durationFilter").value;

    const category =
            document.getElementById("categoryFilter").value;

    if (!companyId) {
        alert("Please select a company");
        return;
    }

    // Fetch experiences for the company
    const response =
            await fetch(
                `/api/interview-experiences/company/${companyId}`,
                {
                    headers: getAuthHeaders()
                }
            );

    let experiences =
            await response.json();

    // Filter by duration
    if (duration && duration !== "") {
        const currentYear = new Date().getFullYear();

        experiences = experiences.filter(exp => {
            if (duration === "all") {
                return true;
            }

            const yearsBack = currentYear - parseInt(duration);
            return exp.interviewYear >= yearsBack;
        });
    }

    // Fetch all questions for these experiences
    const allQuestions = [];

    for (let exp of experiences) {
        const expQuestionsResponse =
                await fetch(
                    `/api/experience-questions/experience/${exp.id}`,
                    {
                        headers: getAuthHeaders()
                    }
                );

        const expQuestions =
                await expQuestionsResponse.json();

        allQuestions.push(...expQuestions);
    }

    // Filter by category if selected
    let filteredQuestions = allQuestions;

    if (category && category !== "") {
        filteredQuestions = allQuestions.filter(
            q => q.category === category
        );
    }

    // Count question frequency
    const questionFrequency = {};

    filteredQuestions.forEach(q => {
        const key = q.questionText;
        questionFrequency[key] = (questionFrequency[key] || 0) + 1;
    });

    // Sort by frequency (descending)
    const sortedQuestions = Object.entries(questionFrequency)
            .sort((a, b) => b[1] - a[1]);

    // Display results
    const resultsDiv =
            document.getElementById("filteredResults");

    resultsDiv.innerHTML = "";

    if (sortedQuestions.length === 0) {
        resultsDiv.innerHTML = `<p>No questions found</p>`;
        return;
    }

    // Group questions by category
    const groupedByCategory = {};

    sortedQuestions.forEach(([question, frequency]) => {
        const matchingQuestion = filteredQuestions.find(
            q => q.questionText === question
        );

        const cat = matchingQuestion.category;

        if (!groupedByCategory[cat]) {
            groupedByCategory[cat] = [];
        }

        groupedByCategory[cat].push({
            text: question,
            frequency: frequency
        });
    });

    // Display each category and its questions
    Object.keys(groupedByCategory).forEach(cat => {
        resultsDiv.innerHTML += `<h3>${cat}</h3>`;

        groupedByCategory[cat].forEach(q => {
            resultsDiv.innerHTML += `
                <div class="company-card">
                    ${q.text} [${q.frequency}]
                </div>
            `;
        });
    });

    console.log(groupedByCategory);
}



document.addEventListener("DOMContentLoaded", () => {
    loadViewCompanyWise();
});
