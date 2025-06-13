//
//  EventListView.swift
//  EventForce-iOS
//
//  Created by Jorge Villeda on 5/26/25.
//

import SwiftUI

struct EventListView: View {
    @StateObject var eventVM = EventViewModel()
    @EnvironmentObject var authViewModel: AuthenticationViewModel

    @State private var searchText = ""
    @State private var showAddEvent = false

    private var events: [Event] {
        eventVM.filteredEvents(searchText: searchText)
    }

    var body: some View {
        NavigationStack {
            VStack {
                Picker("Profile", selection: $authViewModel.currentSelectedProfile) {
                    Text("Personal").tag(UserType.personal)
                    Text("Student").tag(UserType.student)
                }
                .pickerStyle(.segmented)
                .padding(.horizontal)
                .onChange(of: authViewModel.currentSelectedProfile) { oldValue, newValue in
                    guard let userId = authViewModel.currentUser?.id else { return }
                    eventVM.fetchEvents(for: newValue.rawValue, userId: userId)
                }

                List {
                    ForEach(events) { event in
                        NavigationLink(destination: EditEventView(event: event, viewModel: eventVM)) {
                            VStack(alignment: .leading) {
                                Text(event.name)
                                    .font(.headline)
                                Text(event.date.formatted(date: .abbreviated, time: .shortened))
                                    .font(.subheadline)
                                    .foregroundStyle(.gray)
                                Text(event.description)
                                    .font(.body)
                            }
                            .padding(.vertical, 4)
                        }
                    }
                    .onDelete(perform: deleteEvent)
                }
                .searchable(text: $searchText)
            }
            .navigationTitle("My Events")
            .navigationBarBackButtonHidden(true)
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button(action: {
                        authViewModel.logout()
                    }) {
                        HStack {
                            Image(systemName: "chevron.backward")
                            Text("Logout")
                        }
                    }
                    .accessibilityLabel("Logout Button")
                }
                ToolbarItem(placement: .topBarTrailing) {
                    HStack {
                        Button(action: { showAddEvent = true}) {
                            Image(systemName: "plus")
                        }
                        .accessibilityLabel("Add Event Name")

                        NavigationLink {
                            SettingsView()
                        } label: {
                            Image(systemName: "gearshape.fill")
                        }
                        .accessibilityLabel("Settings Button")
                    }
                }
            }
            .sheet(isPresented: $showAddEvent) {
                AddEventView(userType: authViewModel.currentSelectedProfile, onSave: {
                    guard let userId = authViewModel.currentUser?.id else { return }
                    eventVM.fetchEvents(for: authViewModel.currentSelectedProfile.rawValue, userId: userId)
                })
                .environmentObject(authViewModel)
            }
            .onAppear {
                guard let userId = authViewModel.currentUser?.id else { return }
                eventVM.fetchEvents(for: authViewModel.currentSelectedProfile.rawValue, userId: userId)
            }
            .alert("Error Loading Events", isPresented: Binding(
                get: { eventVM.errorMessage != nil },
                set: { _ in eventVM.errorMessage = nil }
            )) {
                Button("OK") {}
            } message: {
                Text(eventVM.errorMessage ?? "An unknown error occured.")
            }
        }
    }

    private func deleteEvent(at offsets: IndexSet) {
        let eventsToDelete = offsets.map { events[$0] }
        Task {
            for event in eventsToDelete {
                do {
                    try await eventVM.deleteEvent(event)
                } catch {
                    print("Failed to delete event: \(error.localizedDescription)")
                }
            }
        }
    }
}
