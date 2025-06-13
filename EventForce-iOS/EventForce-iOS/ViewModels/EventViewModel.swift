//
//  EventViewModel.swift
//  EventForce-iOS
//
//  Created by Jorge Villeda on 5/24/25.
//

import Foundation

@MainActor
class EventViewModel: ObservableObject {
    @Published var events: [Event] = []
    @Published var errorMessage: String? = nil
    
    private let dbService = DatabaseService.shared
    
    func fetchEvents(for userType: String, userId: String) {
        Task {
            do {
                try await dbService.loadEvents(for: userType, userId: userId)
                self.events = dbService.events
            } catch {
                errorMessage = "Failed to load events: \(error.localizedDescription)"
            }
        }
    }
    
    func filteredEvents(searchText: String) -> [Event] {
        if searchText.isEmpty {
            return events.sorted { $0.date > $1.date }
        } else {
            return events
                .filter {
                    $0.name.localizedCaseInsensitiveContains(searchText) || $0.description.localizedCaseInsensitiveContains(searchText)
                }
                .sorted { $0.date < $1.date }
        }
    }
    
    func addEvent(_ event: Event) async throws {
        try await dbService.addEvent(event)
        events.append(event)
    }
    
    func updateEvent(_ event: Event) async throws {
        try await dbService.updateEvent(event)
        if let index = events.firstIndex(where: { $0.id == event.id }) {
            events[index] = event
        }
    }
    
    func deleteEvent(_ event: Event) async throws {
        try await dbService.deleteEvent(event)
        if let index = events.firstIndex(where: { $0.id == event.id }) {
            events.remove(at: index)
        }
    }
}
