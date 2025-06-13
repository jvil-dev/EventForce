//
//  EditEventView.swift
//  EventForce-iOS
//
//  Created by Jorge Villeda on 6/8/25.
//

import SwiftUI

struct EditEventView: View {
    @Environment(\.dismiss) var dismiss
    @ObservedObject var eventVM: EventViewModel
    
    @State private var event: Event
    @State private var isSaving = false
    
    init(event: Event, viewModel: EventViewModel) {
        _event = State(initialValue: event)
        self.eventVM = viewModel
    }
    
    var body: some View {
        Form {
            Section(header: Text("Event Info")) {
                TextField("Name", text: $event.name)
                TextField("Description", text: $event.description)
                DatePicker("Date", selection: $event.date, displayedComponents: [.date, .hourAndMinute])
            }
            
            Section(header: Text("Profile")) {
                Picker("Profile", selection: $event.type) {
                    Text("Personal").tag(UserType.personal)
                    Text("Student").tag(UserType.student)
                }
                .pickerStyle(.segmented)
            }
            
            Section {
                Button("Save") {
                    Task {
                        isSaving = true
                        do {
                            try await eventVM.updateEvent(event)
                            dismiss()
                        } catch {
                            print("Failed to update event: \(error.localizedDescription)")
                        }
                        isSaving = false
                    }
                }
                .disabled(event.name.isEmpty || event.description.isEmpty)
            }
        }
        .navigationTitle("Edit Event")
        .navigationBarTitleDisplayMode(.inline)
        .disabled(isSaving)
        .overlay {
            if isSaving {
                ZStack {
                    Color.black.opacity(0.2).ignoresSafeArea()
                    ProgressView("Saving...")
                        .padding()
                        .background(Color.white.clipShape(RoundedRectangle(cornerRadius: 10)))
                }
            }
        }
    }
}
