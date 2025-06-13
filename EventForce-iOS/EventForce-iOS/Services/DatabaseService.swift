//
//  DatabaseService.swift
//  EventForce-iOS
//
//  Created by Jorge Villeda on 5/24/25.
//

import Foundation
import FirebaseFirestore

@MainActor
class DatabaseService: ObservableObject {
    static let shared = DatabaseService()
    @Published private(set) var events: [Event] = []
    @Published private(set) var users: [User] = []
    private let db = Firestore.firestore()

    func loadUsers() async throws {
        print("DatabaseService: loadUsers() called.")
        do {
            let snapshot = try await db.collection("users").getDocuments()
            self.users = try snapshot.documents.map { doc in
                print("DatabaseService: Raw user document data: \(doc.documentID) -> \(doc.data())")
                return try doc.data(as: User.self)
            }
            print("DatabaseService: Successfully loaded \(self.users.count) users.")
        } catch {
            print("DatabaseService: Error loading users: \(error.localizedDescription)")
            throw DatabaseError.unknown(error)
        }
    }

    func addUser(_ user: User) async throws {
        do {
            try db.collection("users").document(user.id).setData(from: user)
            print("DatabaseService: User \(user.email) added to Firestore with ID: \(user.id)")
        } catch {
            print("DatabaseService: Error adding user \(user.email): \(error.localizedDescription)")
            throw DatabaseError.failedToSave
        }
    }

    func updateUser(_ user: User) async throws {
        guard !user.id.isEmpty else { throw DatabaseError.notFound }
        do {
            try db.collection("users").document(user.id).setData(from: user)
            print("DatabaseService: User \(user.email) updated in Firestore.")
        } catch {
            print("DatabaseService: Error updating user \(user.email): \(error.localizedDescription)")
            throw DatabaseError.unknown(error)
        }
    }

    func deleteUser(_ user: User) async throws {
        guard !user.id.isEmpty else { throw DatabaseError.notFound }
        do {
            try await db.collection("users").document(user.id).delete()
            print("DatabaseService: User \(user.email) deleted from Firestore.")
        } catch {
            print("DatabaseService: Error deleting user \(user.email): \(error.localizedDescription)")
            throw DatabaseError.unknown(error)
        }
    }

    func getUser(userId: String) async throws -> User {
        print("DatabaseService: Attempting to get user with ID: \(userId)")
        do {
            let snapshot = try await db.collection("users").document(userId).getDocument()
            if snapshot.exists {
                print("DatabaseService: Raw user document data for getUser: \(snapshot.documentID) -> \(snapshot.data() ?? [:])")
                let user = try snapshot.data(as: User.self)
                print("DatabaseService: Successfully got user \(user.email) with ID: \(userId)")
                return user
            } else {
                print("DatabaseService: User with ID \(userId) not found in Firestore.")
                throw DatabaseError.notFound
            }
        } catch {
            print("DatabaseService: Error getting user with ID \(userId): \(error.localizedDescription)")
            throw DatabaseError.unknown(error)
        }
    }

    func loadEvents(for userType: String, userId: String) async throws {
        guard !userId.isEmpty else {
            self.events = []
            return
        }
        
        print("DatabaseService: Attempting to load events for userType: '\(userType)' and userId: '\(userId)'")
        do {
            let snapshot = try await db.collection("events")
                .whereField("userId", isEqualTo: userId)
                .whereField("type", isEqualTo: userType)
                .getDocuments()

            self.events = try snapshot.documents
                .map { doc in
                    print("DatabaseService: Raw event document data for loadEvents: \(doc.documentID) -> \(doc.data())")
                    return try doc.data(as: Event.self)
                }
                .sorted { $0.date < $1.date }

            print("DatabaseService: Successfully loaded \(self.events.count) events for userType: '\(userType)'")

        } catch {
            print("DatabaseService: Error loading events for userType '\(userType)': \(error.localizedDescription)")
            throw DatabaseError.unknown(error)
        }
    }

    func addEvent(_ event: Event) async throws {
        do {
            _ = try db.collection("events").addDocument(from: event)
            print("DatabaseService: Event '\(event.name)' added successfully.")
        } catch {
            print("DatabaseService: Error adding event '\(event.name)': \(error.localizedDescription)")
            throw DatabaseError.failedToSave
        }
    }

    func updateEvent(_ event: Event) async throws {
        guard let eventId = event.id, !eventId.isEmpty else { throw DatabaseError.notFound }
        do {
            try db.collection("events").document(eventId).setData(from: event)
            print("DatabaseService: Event '\(event.name)' updated successfully.")
        } catch {
            print("DatabaseService: Error updating event '\(event.name)': \(error.localizedDescription)")
            throw DatabaseError.unknown(error)
        }
    }

    func deleteEvent(_ event: Event) async throws {
        guard let eventId = event.id, !eventId.isEmpty else { throw DatabaseError.notFound }
        do {
            try await db.collection("events").document(eventId).delete()
            print("DatabaseService: Event '\(event.name)' deleted successfully.")
        } catch {
            print("DatabaseService: Error deleting event '\(event.name)': \(error.localizedDescription)")
            throw DatabaseError.unknown(error)
        }
    }
}
