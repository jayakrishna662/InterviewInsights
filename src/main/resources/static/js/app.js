//Find the "Browse Experiences" button and Listen for when it's clicked
const experiencesButton = document.getElementById("experiencesBtn");

if (experiencesButton) {
    experiencesButton.addEventListener("click", () => {

        const token = localStorage.getItem("token");

            // if no token exists
            if (!token) {
                localStorage.setItem("redirectAfterLogin", "/experiences");  // remember "user wanted to go to /experiences"
                window.location.href = "/auth";  // redirect to login page
                return;
            }
        // if token exists, go directly to experiences page
        window.location.href = "/experiences";
    });
}


//Find the "Explore Questions" button and Listen for when it's clicked
const questionsButton = document.getElementById("questionsBtn");

if (questionsButton) {
    questionsButton.addEventListener("click", () => {

        const token = localStorage.getItem("token");

                // if no token exists
                if (!token) {
                    localStorage.setItem("redirectAfterLogin", "/questions");  // remember "user wanted to go to /questions"
                    window.location.href = "/auth";
                    return;
                }
        // if token exists, go directly to questions page
        window.location.href = "/questions";
    });
}



const submitExperienceButton = document.getElementById("submitExperienceBtn");

if (submitExperienceButton) {
    submitExperienceButton.addEventListener("click", () => {

        const token = localStorage.getItem("token");

        // if no token exists
        if (!token) {
            localStorage.setItem("redirectAfterLogin", "/submit-experience"); // // remember "user wanted to go to /submit-experience"
            window.location.href = "/auth";
            return;
        }
        // if token exists, go directly to /submit-experience page
        window.location.href = "/submit-experience";
    });
}




