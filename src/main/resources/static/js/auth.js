const loginTab =
    document.getElementById("loginTab");

const registerTab =
    document.getElementById("registerTab");

const loginContainer=
    document.getElementById("loginForm");

const registerContainer =
    document.getElementById("registerForm");



// when you open login/register page this code automatically runs
// Load departments and batches when page loads i.e. when register page is opened
document.addEventListener("DOMContentLoaded", async () => {
       const token = localStorage.getItem("token");  // Get the token from browser storage (if it exists)

           if (token) { // If a token exists, the user is already logged in
               window.location.href = "/"; // redirects them to the home page
               return; // now stop running remaining function, bcz No point showing a login page to someone who's already logged in!
           }
    try {

        // Load departments by  Sending a GET request to backend
        const deptResponse = await fetch("/api/departments");
        const departments = await deptResponse.json(); // converts the response to a JavaScript object(or array) and store in departments

        const departmentSelect = document.getElementById("departmentSelect");  // find "Select Department" dropdown in the Register form

        departments.forEach(dept => {  // Loop through each department in the array
            const option = document.createElement("option");  // Create a new <option> element (dropdown item)
            option.value = dept.id; //  set the internal value (sent to backend)
            option.textContent = dept.departmentName; // set what the user sees
            departmentSelect.appendChild(option); // add this option to the dropdown
        });

        // Load batches
        const batchResponse = await fetch("/api/batches");
        const batches = await batchResponse.json();

        // Find the batch dropdown and store in batchSelect
       const batchSelect = document.getElementById("batchSelect");

        //Loop through each batch and add it as an option to the dropdown
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
    registerContainer.classList.add("active"); // registration form becomes visible
    loginContainer.classList.remove("active"); // login form hides
    registerTab.classList.add("active");  // Register button becomes highlighted
    loginTab.classList.remove("active"); // Login button becomes white (not highlighted)
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

// When  user submits the Login form, execute this function.
loginFormElement.addEventListener("submit", async (e) => {
    e.preventDefault();

    //  grab all the input values from the form automatically
    const formData = new FormData(loginFormElement);

    // Extract the values from formData and create a JavaScript  User object
    const user = {
        rollNumber: formData.get("rollNumber"),
        password: formData.get("password")
    };

    try {
        const response = await fetch("/api/auth/login", {  // Send a POST request to your Spring Boot backend and store server response
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)  // Converts the JavaScript object to JSON text:
        });

        const data = await response.json(); // Convert that response from JSON text into a JavaScript object and store in data

        if(!response.ok){  // if the response is NOT ok, show the error message and exist this function
            alert(data.message);
            return;
        }

       if (data.success) { // if login was successful
           alert("Login successful!");  // Show "Login successful!" alert
           localStorage.setItem("token", data.token);  // saves the token in browser local storage

            // checks if there's a saved redirect URL and store in redirect
            const redirect =
                   localStorage.getItem("redirectAfterLogin") || "/";

               localStorage.removeItem("redirectAfterLogin"); // deletes the saved redirect URL (we don't need it anymore)

               window.location.href = redirect;  // navigates to that URL
       } else {
           alert("Login failed: " + data.message); // if login fails, show error message
       }
    } catch (error) { // handles unexpected errors (network issues, parsing errors, etc.)
        alert("Error: " + error.message);
        console.error(error);
    }
});



// Handling showPassword checkbox, when Show Password is checked then show password
const showPassCheckbox = document.getElementById("showPass");
const passwordInput = document.getElementById("password");

showPassCheckbox.addEventListener("change", (e) => {
    if (e.target.checked) { // if checked box is checked then change password type to text, so that it becomes visible
        passwordInput.type = "text";    // Show password
    } else {
        passwordInput.type = "password"; // Hide password
    }
});



// When user clicks on signup from navbar, it opens registration form
// window.location.search gets everything after the ? in the URL
// URLSearchParams is a tool that parses (breaks down) this string into readable pieces
// params now contains all the URL parameters
const params = new URLSearchParams(window.location.search);

if (params.get("tab") === "signup") { // extract the value of the tab parameter from the URL and check if its equal to "signup"
    registerTab.click(); // here we are doing click event for register button,so automatically opens registration form
}