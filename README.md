**Category 1: Software Design and Engineering**

**1. Briefly describe the artifact. What is it? When was it created?**

The artifact is the iOS version of **EventForce**, an event-tracking application I originally developed as an Android app. I began the new Swift-based development on May 19, 2025. This enhancement focuses on the software design and engineering required to port the application to a new platform, including building a new architectural foundation, creating new data models, and designing a modern UI in SwiftUI.


**2. Justify the inclusion of the artifact in your ePortfolio. Why did you select this item? What specific components of the artifact showcase your skills and abilities in software development? How did the enhancement improve the artifact? What specific skills did you demonstrate in the enhancement?**

I chose this artifact because it demonstrates my ability to re-engineer an application for a new platform, improving its architecture and aligning it with modern, scalable development practices. A good example is the enhancement of the user login component.

In the Android app, the UI layour was defined in an XML file, and the logic was within an Activity class. Here's a code snippet of the _Activity_ class that manually attached listeners to each UI element, mixing display and authentication logic:

```
// In onCreate method...
EditText usernameField = findViewById(R.id.username);
EditText passwordField = findViewById(R.id.password);
Button loginButton = findViewById(R.id.login_button);

// Login button click listener
loginButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        // ...login logic continues here...
    }
});
```

For the iOS conversion, I implemented a modern **Model-View-View-Model (MVVM)** architecture. The UI is now declared directly and declaratively in Swift using SwiftUI, and its logic is handled by a separate _ViewModel_. The SwiftUI _View_ describes the UI's appearance and binds its control directly to data properties from the _ViewModel_. This results in cleaner code:

```
// In the body of LoginFormView...
TextField("Email", text: $viewModel.email)
    .keyboardType(.emailAddress)
    .textFieldStyle(.roundedBorder)

SecureField("Password", text: $viewModel.password)
    .textFieldStyle(.roundedBorder)

Button("Login") {
    viewModel.login()
}
.buttonStyle(.borderedProminent)
```

All the logic for authentication now resides in the _AuthenticationViewModel_. The view calls the _login()_ function, completely decoupling the UI from the backend services. THis makes the code more modular and easier to maintain:

```
@MainActor
class AuthenticationViewModel: ObservableObject {
    @Published var email = ""
    @Published var password = ""
    @Published var isLoggedIn = false
    // ...

    func login() {
        Task {
            do {
                // ...await call to AuthenticationService...
                isLoggedIn = true
            } catch {
                // ...handle error...
            }
        }
    }
}
```
This architectural shift from a coupled _Activity_ to a decoupled MVVM pattern is the core improvement, demonstrating proficiency in modern software design, code refactoring and platform-specific best practices.

**3. Reflect on the process of enhancing the artifact. What did you learn as you were creating it and improving it? What challenges did you face? How did you incorporate feedback as you made changes to the artifact? How was the artifact improved? Which course outcomes did you partially or fully meet with your enhancements? Which do you feel were not met?**

One of the most valuable parts of this enhancement was learning how to translate Android app architecture to SwiftUI while modernizing the underlying data and services layers. I focused on establishing a clean MVVM architecture from the start. This decision helped me avoid doing simple line-by-line translation, but rather write code that's scalable. This enhancement fully met the course outcome of **"designing and developing computing solutions using software engineering practices".**
