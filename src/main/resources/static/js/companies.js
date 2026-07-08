// Load companies automatically when page opens
document.addEventListener("DOMContentLoaded", loadCompanies);

// Load all companies
async function loadCompanies() {

    // Fetch companies from backend and store in response
    const response = await fetch("/api/companies", {
        headers: getAuthHeaders()  // adds the token to the request(from common.js) to prove you are logged in
    });

    const companies = await response.json(); // Convert the JSON response into a JavaScript array

    const content = document.getElementById("content");  // Find the <div id="content"> element on the page

    content.innerHTML = ""; // Clear any HTML that was already in the container and also this Prevents duplicate companies from appearing if function runs multiple times


   // Loop through each company and creates a card for it
   // Display all cards on the page
    companies.forEach(company => {

        content.innerHTML += `
            <div class="company-card">
                ${company.companyName}
            </div>
        `;

    });

}