//
//  AddEventView.swift
//  EventForce-iOS
//
//  Created by Jorge Villeda on 5/26/25.
//

import SwiftUI

struct AddEventView: View {
    @Environment(\.dismiss) var dismiss
    @EnvironmentObject var authViewModel: AuthenticationViewModel
    
    let userType: UserType
    var onSave: () -> Void
    
    @State private var name = ""
    @State private var description = ""
    @State private var date = Date()
    @State private var isSaving = false
    @State private var showError = false
    @State private var errorMessage = ""
    
    var body: some View {
        NavigationStack {
            Form {
                Section(header: Text("Event Info")) {
                    TextField("Name", text: $name)
                        .accessibilityLabel("Event Name")
                    TextField("Description", text: $description)
                        .accessibilityLabel("Event Description")
                    DatePicker("Date", selection: $date, displayedComponents: [.date, .hourAndMinute])
                }
                
                Section {
                    Button("Save") {
                        Task {
                            guard let userId = authViewModel.currentUser?.id else {
                                errorMessage = "Cannot save event. User not found"
                                showError = true
                                return
                            }
                            
                            isSaving = true
                            let trimmedName = name.trimmingCharacters(in: .whitespacesAndNewlines)
                            let trimmedDescription = description.trimmingCharacters(in: .whitespacesAndNewlines)
                            
                            let newEvent = Event(
                                id: nil,
                                userId: userId,
                                name: trimmedName,
                                description: trimmedDescription,
                                date: date,
                                type: self.userType
                            )
                            do {
                                try await DatabaseService.shared.addEvent(newEvent)
                                onSave()
                                dismiss()
                            } catch {
                                errorMessage = error.localizedDescription
                                showError = true
                            }
                            isSaving = false
                        }
                    }
                    .accessibilityHint("Saves the new event and returns to the event list")
                    .disabled(name.isEmpty || description.isEmpty)
                }
            }
            .navigationTitle("Add Event")
            .alert("Error", isPresented: $showError) {
                Button("OK", role: .cancel) {}
            } message: {
                Text(errorMessage)
            }
            .disabled(isSaving)
            .overlay {
                if isSaving {
                    ZStack {
                        Color.black.opacity(0.2).ignoresSafeArea()
                        ProgressView("Saving...")
                            .padding()
                            .background(
                                Color.white
                                    .clipShape(RoundedRectangle(cornerRadius: 10))
                            )
                    }
                }
            }
        }
    }
}

struct AddEventView_Previews: PreviewProvider {
    static var previews: some View {
        AddEventView(userType: .personal, onSave: {})
            .environmentObject(AuthenticationViewModel())
    }
}
