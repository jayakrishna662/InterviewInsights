requireAuthentication();

document.addEventListener("DOMContentLoaded", async () => {

    const companySelect = document.getElementById("companySelect");

    try {

        const response = await fetch("/api/companies", {
            headers: getAuthHeaders()
        });

        const companies = await response.json();

        companies.forEach(company => {

            const option = document.createElement("option");

            option.value = company.id;
            option.textContent = company.companyName;

            companySelect.appendChild(option);
        });

    } catch (error) {
        console.error(error);
    }

});

const experienceForm = document.getElementById("experienceForm");

experienceForm.addEventListener("submit", async (e) => {

    e.preventDefault();

    const experience = {

        companyId: parseInt(document.getElementById("companySelect").value),

        interviewYear: parseInt(document.getElementById("interviewYear").value),

        result: document.getElementById("result").value,

        aptitudeExperience: document.getElementById("aptitudeExperience").value,

        codingExperience: document.getElementById("codingExperience").value,

        technicalExperience: document.getElementById("technicalExperience").value,

        hrExperience: document.getElementById("hrExperience").value,

        gdExperience: document.getElementById("gdExperience").value,

        overallSuggestions: document.getElementById("overallSuggestions").value
    };

    const experiences = [
        document.getElementById("aptitudeExperience").value.trim(),
        document.getElementById("codingExperience").value.trim(),
        document.getElementById("technicalExperience").value.trim(),
        document.getElementById("hrExperience").value.trim(),
        document.getElementById("gdExperience").value.trim(),
        document.getElementById("overallSuggestions").value.trim()
    ];

    const hasExperience = experiences.some(exp => exp !== "");

    if (!hasExperience) {
        alert("Please enter at least one interview experience.");
        return;
    }

    try {

        const response = await fetch("/api/interview-experiences", {

            method: "POST",

            headers: {
                "Content-Type": "application/json",
                ...getAuthHeaders()
            },

            body: JSON.stringify(experience)
        });

        const data = await response.json();

        if (!response.ok) {
            alert(data.message);
            return;
        }

        alert("Interview experience submitted successfully!");

        experienceForm.reset();

    } catch (error) {
        console.error(error);
    }

});