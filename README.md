**1. Briefly describe the artifact. What is it? When was it created?**

This artifact is the database and authentication backend for the **EventForce** iOS application. This enhancement represents the complete migration from the original Android version's insecure, local SQLite database to a modern, secure, and cloud-based infrastructure using Google Firebase.

**2. Justify the inclusion of the artifact in your ePortfolio. Why did you select this item? What specific components of the artifact showcase your skills and abilities in software development? How did the enhancement improve the artifact? What specific skills did you demonstrate in the enhancement?**

I selected this artifact becuase it's a good example of migrating a legacy system to a secure, scalable, and cloud-native architecture. The original Android app was limited by its local database, which stored user passwords in plaintext. This presented a major security vulnerability.

The original database checked for a user by comparing the provided plaintext password directly against the plaintext password stored in the database.

#### UserDatabase.java (Android Insecure Authentication):
```
// Check if personal user exists in database
public boolean checkPersonalUser(String personalUsername, String personalPassword) {
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE PERSONAL_USERNAME = ? AND PERSONAL_PASSWORD = ?", new String[]{personalUsername, personalPassword});
    if (cursor.getCount() <= 0) {
        cursor.close();
        return false;
    }
    cursor.close();
    return true;
}
```

The iOS enhancement replaces this entire system with Firebase, which handles all aspects of secire authentication and data storage. This dedicated service uses the Firebase SDK to handle user login. Firebase automatically manages secure password hashing, token management, and session persistence, completely removing the security flaw that is in the Android version

#### AuthenticationService.swift (iOS Secure Authentication)
```
import FirebaseAuth

class AuthenticationService {
    func login(email: String, password: String) async throws -> String {
        do {
            // Secure sign-in handled by Firebase
            let authResult = try await Auth.auth().signIn(withEmail: email, password: password)
            return authResult.user.uid
        } catch let error as NSError {
            throw try mapAuthError(error)
        }
    }
}
```

The old **EventDatabase.java** with its raw SQL queries was replaced by a clean service that interacts with Cloud Firestore. This not only modernizes the code, but also enables data to be synced across devices in real-time

#### DatabaseServie.swift (iOS Cloud Database)
```
import FirebaseFirestore

@MainActor
class DatabaseService: ObservableObject {
    private let db = Firestore.firestore()

    func addEvent(_ event: Event) async throws {
        do {
            // Uses the Firebase SDK to add a Codable object directly
            _ = try db.collection("events").addDocument(from: event)
        } catch {
            throw DatabaseError.failedToSave
        }
    }
}
```
This migration from a local, insecure database to a cloud-based solution demonstrates my ability to design and implement robust, modern backend systems, a crucial skill in today's development landscape.

**3. Reflect on the process of enhancing the artifact. What did you learn as you were creating it and improving it? What challenges did you face? How did you incorporate feedback as you made changes to the artifact? How was the artifact improved? Which course outcomes did you partially or fully meet with your enhancements? Which do you feel were not met?**

The most significant learning experience during this phase was understanding the importance of backend security rules. I initially faced persistent "Missing or insufficient permissions" errors from Firestore, which forced me to learn how to write and debug security rules to ensure that users could only access their own data. This was a challenging but invaluable lesson in data security.

This process directly reflects my initial plan to migrate from SQLite to a cloud database. While my plan mentioned "Firebase Realtime Database", I chose to implement Cloud Firestore after further research revealed it was better suited for the complex queries I planned for Category Two. This shows my ability to adapt a plan based on new technical information.

This enhancement fully met the course outcome: “Demonstrate an ability to use well-founded and innovative techniques, skills, and tools in computing practices.”. I utilized a modern, industry-standard toolset (Firebase) to solve concrete problems of security, scalability, and functionality that existed in the original artifact.
