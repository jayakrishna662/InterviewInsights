

const loginTab =
    document.getElementById("loginTab");

const registerTab =
    document.getElementById("registerTab");

const loginContainer=
    document.getElementById("loginForm");

const registerContainer =
    document.getElementById("registerForm");


// Load departments and batches when page loads
document.addEventListener("DOMContentLoaded", async () => {
       const token = localStorage.getItem("token");

           console.log("Auth page token:", token);

           if (token) {
               console.log("Redirecting...");
               window.location.href = "/";
               return;
           }
    try {

        // Load departments
        const deptResponse = await fetch("/api/departments");
        const departments = await deptResponse.json();

        const departmentSelect = document.getElementById("departmentSelect");

        departments.forEach(dept => {
            const option = document.createElement("option");
            option.value = dept.id;
            option.textContent = dept.departmentName;
            departmentSelect.appendChild(option);
        });

        // Load batches
        const batchResponse = await fetch("/api/batches");
        const batches = await batchResponse.json();

        const batchSelect = document.querySelector("select[name='batchId']");

        batches.forEach(batch => {
            const option = document.createElement("option");
            option.value = batch.id;
            option.textContent = batch.batchName;
            batchSelect.appendChild(option);
        });

    } catch (error) {
        console.error("Error loading data:", error);
    }
});





// When  Login button is clicked
loginTab.addEventListener("click", () => {
    loginContainer.classList.add("active");     // Add another class called active. so it becomes "form-container active" hence form becomes visible
    registerContainer.classList.remove("active"); // Remove active class, so register form becomes "form-container" hence register form is hidden
    loginTab.classList.add("active"); // Add active , so login button becomes ".tab-button.active" , hence login button is highlighted with blue
    registerTab.classList.remove("active"); // Remove active, so register button becomes white (not highlighted)
});

// When Register button is clicked
registerTab.addEventListener("click", () => {
    registerContainer.classList.add("active");
    loginContainer.classList.remove("active");
    registerTab.classList.add("active");
    loginTab.classList.remove("active");
});

// Handle Register Form Submission
const registerFormElement = registerContainer.querySelector("form"); // Find the form inside the register container

// when Register button is clicked, submit the form
registerFormElement.addEventListener("submit", async (e) => {
    e.preventDefault(); // prevent page immediate reload

    const formData = new FormData(registerFormElement);  // Creating FormData object and Store user entered form details into formData object as key-value pairs

    // Convert formData into Js object (formData is a special object not normal js object)
    // keys should match the fields in spring
    const user = {
        name: formData.get("name"),
        email: formData.get("email"),
        rollNumber: formData.get("rollNumber"),
        departmentId: formData.get("departmentId"),
        batchId: parseInt(formData.get("batchId")), // convert BatchId into integer, because html returns it as string, but spring boot has batch id as int
        password: formData.get("password"),
        confirmPassword: formData.get("confirmPassword")
    };

    try {
        // await pauses this async function until response(response object) comes from server comes
        const response = await fetch("/api/auth/register", { // send an HTTP request to the /api/auth/register endpoint in Spring Boot application.
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user) // convert js object to JSON string
        });

        // here again await pauses async function and Waits for the browser to read the response body(JSON format) and parse the JSON into a JavaScript object.
        const data = await response.json();

        // if {success:"false"} in response body, show response message and stop the function
        if (!response.ok) {
            alert(data.message);
            return;
        }


        if (data.success) {  // if success in response object is true, (success: "true")
            alert("Registration successful! Please login."); // pop ups this message
            registerFormElement.reset(); // All input fields in registration form becomes empty after you click on Register button
            loginTab.click(); // Automatically switch to login page (here JS automatically executes loginTab click event)
        } else {
            alert("Registration failed: " + data.message);
        }
    } catch (error) { // // Error object is passed by browser, if something goes wrong like -> error{ message: "failed to fetch"}
        alert("Error: " + error.message);
        console.error(error); // // prints complete error object in browsers console
    }
});

// Handle Login Form Submission
const loginFormElement = loginContainer.querySelector("form"); // Find the form inside the loginContainer container

// When the user submits the Login form, execute this function.
loginFormElement.addEventListener("submit", async (e) => {
    e.preventDefault();

    const formData = new FormData(loginFormElement);

    // Convert formData into Js object
    const user = {
        rollNumber: formData.get("rollNumber"),
        password: formData.get("password")
    };

    try {
        const response = await fetch("/api/auth/login", {  // send login data to spring boot
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        });

        const data = await response.json();

        if(!response.ok){
            alert(data.message);
            return;
        }

       if (data.success) {
           alert("Login successful!");
           localStorage.setItem("token", data.token);
            const redirect =
                   localStorage.getItem("redirectAfterLogin") || "/";

               localStorage.removeItem("redirectAfterLogin");

               window.location.href = redirect;
       } else {
           alert("Login failed: " + data.message);
       }
    } catch (error) {
        alert("Error: " + error.message);
        console.error(error);
    }
});


const showPassCheckbox = document.getElementById("showPass");
const passwordInput = document.getElementById("password");

showPassCheckbox.addEventListener("change", (e) => {
    if (e.target.checked) {
        passwordInput.type = "text";    // Show password
    } else {
        passwordInput.type = "password"; // Hide password
    }
});

const params = new URLSearchParams(window.location.search);

if (params.get("tab") === "signup") {
    registerTab.click();
}