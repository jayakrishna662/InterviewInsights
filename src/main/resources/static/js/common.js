function logout() {
    localStorage.removeItem("token"); // delete the token when user logs out
    window.location.href = "/auth"; // redirect to login page
}


// Every API request needs to send the token in the header to prove you're logged in
function getAuthHeaders() {

    // Get the token from local storage
    // If token exists, returns an object with the token,  eg:{ Authorization: "Bearer abc123xyz"}
    const token = localStorage.getItem("token");

    if (token) {
        return {
            Authorization: `Bearer ${token}`
        };
    }

    return {};  // If no token, returns empty object {}
}


// Get the token and find all navbar elements
// If any element doesn't exist, stop and return
async function updateNavbar() {
    const token = localStorage.getItem("token");

    const loginLink = document.getElementById("loginLink");
    const signupLink = document.getElementById("signupLink");
    const logoutBtn = document.getElementById("logoutBtn");
    const welcomeText = document.getElementById("welcomeText");
    const userInfo = document.getElementById("userInfo");

    if (!loginLink || !signupLink || !logoutBtn || !welcomeText) {
        return;
    }


    /* If NO token (user not logged in):
       Show: Login link ✓
       Show: Sign Up link ✓
       Hide: Logout button X
       Hide: Welcome message X
      */

    if (!token) {
        loginLink.style.display = "inline-block";
        signupLink.style.display = "inline-block";
        logoutBtn.style.display = "none";
        welcomeText.style.display = "none";
        if (userInfo) userInfo.style.display = "none";
        return;
    }


    /*If token exists (user IS logged in):
        Fetch user info from /api/auth/me endpoint
        If successful, show: <user name>
        Logout button ✓
        Hide:
             Login link X
             Sign Up link X
    */
    try {
        const response = await fetch("/api/auth/me", {
            headers: getAuthHeaders()
        });

        if (handleUnauthorizedResponse(response)) {
            return;
        }

        const user = await response.json();

        loginLink.style.display = "none";
        signupLink.style.display = "none";

        if (userInfo) {
            userInfo.style.display = "flex";
        }

        welcomeText.textContent = user.name;
        welcomeText.style.display = "inline";

        logoutBtn.style.display = "inline-block";

    } catch (error) {
        console.error("Failed to load current user:", error);
    }
}


// This function is called from pages that requires login (like /experiences, /question)
// Check if user has token, if no token - redirect to login page, if yes let them continue
function requireAuthentication() {
    const token = localStorage.getItem("token");

    if (!token) {
        window.location.href = "/auth";
    }
}



// If your token expires, automatically logs you out instead of showing errors
// Check if the API response is "401 Unauthorized" (token expired or invalid)
// If YES, log the user out automatically  if NO, continue normally
function handleUnauthorizedResponse(response) {
    if (response.status === 401) {
        logout();
        return true;
    }

    return false;
}


// Find the logout button When clicked, call the logout() function
const logoutBtn = document.getElementById("logoutBtn");

if (logoutBtn) {
    logoutBtn.addEventListener("click", logout);
}

// Runs automatically when the page loads
//  Updates the navbar based on login status
updateNavbar();