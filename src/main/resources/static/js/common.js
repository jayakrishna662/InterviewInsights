function logout() {
    console.trace("logout() called");
    localStorage.removeItem("token");
    window.location.href = "/auth";
}

function getAuthHeaders() {
    const token = localStorage.getItem("token");

    if (token) {
        return {
            Authorization: `Bearer ${token}`
        };
    }

    return {};
}

async function updateNavbar() {
    const token = localStorage.getItem("token");

    const loginLink = document.getElementById("loginLink");
    const signupLink = document.getElementById("signupLink");
    const logoutBtn = document.getElementById("logoutBtn");
    const welcomeText = document.getElementById("welcomeText");

    if (!loginLink || !signupLink || !logoutBtn || !welcomeText) {
        return;
    }

    if (!token) {
        loginLink.style.display = "inline-block";
        signupLink.style.display = "inline-block";
        logoutBtn.style.display = "none";
        welcomeText.style.display = "none";
        return;
    }

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

        welcomeText.textContent = `Welcome, ${user.name}`;
        welcomeText.style.display = "inline";

        logoutBtn.style.display = "inline-block";

    } catch (error) {
        console.error("Failed to load current user:", error);
    }
}

function requireAuthentication() {
    const token = localStorage.getItem("token");

    if (!token) {
        window.location.href = "/auth";
    }
}

function handleUnauthorizedResponse(response) {
    if (response.status === 401) {
        logout();
        return true;
    }

    return false;
}

const logoutBtn = document.getElementById("logoutBtn");

if (logoutBtn) {
    logoutBtn.addEventListener("click", logout);
}

updateNavbar();