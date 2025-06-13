//
//  ChangePasswordView.swift
//  EventForce-iOS
//
//  Created by Jorge Villeda on 6/8/25.
//

import SwiftUI

struct ChangePasswordView: View {
    @Environment(\.dismiss) var dismiss
    @EnvironmentObject var authViewModel: AuthenticationViewModel
    
    @State private var currentPassword = ""
    @State private var newPassword = ""
    @State private var confirmNewPassword = ""
    @State private var errorMessage: String? = nil
    @State private var successMessage: String? = nil
    @State private var isLoading = false
    
    var body: some View {
        Form {
            Section("Current Password") {
                SecureField("Current Password", text: $currentPassword)
            }
            
            Section("New Password") {
                SecureField("New Password", text: $newPassword)
                SecureField("Confirm New Password", text: $confirmNewPassword)
            }
            
            if let errorMessage = errorMessage {
                Text(errorMessage)
                    .foregroundColor(.red)
            }
            
            if let successMessage = successMessage {
                Text(successMessage)
                    .foregroundColor(.green)
            }
            
            Button("Change Password") {
                Task {
                    isLoading = true
                    errorMessage = nil
                    successMessage = nil
                    
                    guard newPassword == confirmNewPassword else {
                        errorMessage = "New passwords do not match."
                        isLoading = false
                        return
                    }
                    guard !newPassword.isEmpty && newPassword.count >= 6 else {
                        errorMessage = "New password must be at least 6 characters long."
                        isLoading = false
                        return
                    }
                    
                    do {
                        try await authViewModel.changePassword(currentPassword: currentPassword, newPassword: newPassword)
                        successMessage = "Password changed successfully!"
                        currentPassword = ""
                        newPassword = ""
                        confirmNewPassword = ""
                        dismiss()
                    } catch {
                        errorMessage = error.localizedDescription
                    }
                    isLoading = false
                }
            }
            .disabled(isLoading || currentPassword.isEmpty || newPassword.isEmpty || confirmNewPassword.isEmpty)
        }
        .navigationTitle("Change Password")
        .overlay {
            if isLoading {
                ProgressView("Changing password...")
                    .padding()
                    .background(Color.white.opacity(0.8))
                    .cornerRadius(10)
            }
        }
    }
}

struct ChangePasswordView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationStack {
            ChangePasswordView()
                .environmentObject(AuthenticationViewModel())
        }
    }
}
