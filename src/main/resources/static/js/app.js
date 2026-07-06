

const experiencesButton = document.getElementById("experiencesBtn");

if (experiencesButton) {
    experiencesButton.addEventListener("click", () => {

        const token = localStorage.getItem("token");

            if (!token) {
                localStorage.setItem("redirectAfterLogin", "/experiences");
                window.location.href = "/auth";
                return;
            }

        window.location.href = "/experiences";
    });
}

const questionsButton = document.getElementById("questionsBtn");

if (questionsButton) {
    questionsButton.addEventListener("click", () => {

        const token = localStorage.getItem("token");

                if (!token) {
                    localStorage.setItem("redirectAfterLogin", "/questions");
                    window.location.href = "/auth";
                    return;
                }

        window.location.href = "/questions";
    });
}

const submitExperienceButton = document.getElementById("submitExperienceBtn");

if (submitExperienceButton) {
    submitExperienceButton.addEventListener("click", () => {

        const token = localStorage.getItem("token");

        if (!token) {
            localStorage.setItem("redirectAfterLogin", "/submit-experience");
            window.location.href = "/auth";
            return;
        }

        window.location.href = "/submit-experience";
    });
}




