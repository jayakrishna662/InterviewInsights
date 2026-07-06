// Load companies automatically when page opens
document.addEventListener("DOMContentLoaded", loadCompanies);

// Load all companies
async function loadCompanies() {

    const response = await fetch("/api/companies", {
        headers: getAuthHeaders()
    });

    const companies = await response.json();

    const content = document.getElementById("content");

    content.innerHTML = "";

    companies.forEach(company => {

        content.innerHTML += `
            <div class="company-card">
                ${company.companyName}
            </div>
        `;

    });

}