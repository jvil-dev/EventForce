**1. Briefly describe the artifact. What is it? When was it created?**

This artifact is the event filtering and data modeling component of the **EventForce** iOS application. Building on the architectural foundation from the first enhancement, this phase focuses on improving the complexity and efficiency of the app's data structures and the algorithms used to retrieve and display events.

**2. Justify the inclusion of the artifact in your ePortfolio. Why did you select this item? What specific components of the artifact showcase your skills and abilities in software development? How did the enhancement improve the artifact? What specific skills did you demonstrate in the enhancement?**

I selected this artifact to demonstrate my ability to apply algorithmic principles and design efficient data structures in a real-world mobile application. The enhancement moves beyond basic data retrieval to implement more complex, user-centric features like keyword searching and optimized data handling.

In the original Android version, the Event was a simple Java class, and data filtering was a basic SQL query:

#### Event.java (Android Data Structure):
```
public class Event {
    private int eventId;
    private String eventName;
    private String eventDate;
    private String eventTime;
    private String eventDescription;

    public Event(int eventId, String eventName, String eventDate, String eventTime, String eventDescription) {
        // ...constructor logic...
    }

    // ...getters and setters...
}
```
For Data Retrieval, filtering was limited to a simple **WHERE** clause on the event type:

#### EventDatabase.java (Android Data Retrieval):
```
// Read
public Cursor getAllEvents(String eventType) {
    SQLiteDatabase db = this.getWritableDatabase();
    return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL6 + " = ?", new String[]{eventType});
}
```

The iOS version introduces a more robust data model and a more advanced, client-side filtering algorithm

The Swift struct is lean and powerful, conforming to Codable which allows for automatic, error-free mapping from Firestore documents to native Swift objects:

#### Event.swift (iOS Data Structure):
```
import FirebaseFirestore

struct Event: Identifiable, Codable {
    @DocumentID var id: String?
    var userId: String
    var name: String
    var description: String
    var date: Date
    var type: UserType
}
```

Instead of repeatedly querying the database, the iOS app fetches a list of events once and performs filtering in-memory. This client-side algorithm provides a much more responsive user experience and demonstrates an understanding of performance trade-offs:

#### EventViewModel.swift (iOS Filtering Algorithm):
```
@MainActor
class EventViewModel: ObservableObject {
    @Published var events: [Event] = []

    func filteredEvents(searchText: String) -> [Event] {
        if searchText.isEmpty {
            return events.sorted { $0.date > $1.date }
        } else {
            return events
                .filter {
                    // Keyword search on name OR description
                    $0.name.localizedCaseInsensitiveContains(searchText) ||
                    $0.description.localizedCaseInsensitiveContains(searchText)
                }
                .sorted { $0.date < $1.date }
        }
    }
}
```
This enhancement showcases my skills in data modeling, algorithm design (search and sort), and performance optimization, transforming a basic list into an interactive and responsive feature.

**3. Reflect on the process of enhancing the artifact. What did you learn as you were creating it and improving it? What challenges did you face? How did you incorporate feedback as you made changes to the artifact? How was the artifact improved? Which course outcomes did you partially or fully meet with your enhancements? Which do you feel were not met?**

This milestone deepened my understanding of Swift’s data handling and how algorithms can be cleanly integrated into UI-driven apps. The biggest challenge was correctly mapping Firestore's timestamp data into a Swift Date object and ensuring the Codable conformance worked seamlessly. This enhancement directly met the course outcome: "Design and evaluate computing solutions that solve a given problem using algorithmic principles and computer science practices". I successfully designed a filtering algorithm and chose appropriate data structures to solve the problem of finding specific events. No planned outcomes for this category were left unmet.
