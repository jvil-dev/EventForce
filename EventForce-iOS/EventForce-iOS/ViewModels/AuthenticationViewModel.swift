//
//  AuthenticationViewModel.swift
//  EventForce-iOS
//
//  Created by Jorge Villeda on 5/23/25.
//

import Foundation
import Combine
import SwiftUI
import FirebaseAuth

@MainActor
class AuthenticationViewModel: ObservableObject {
    @Published var email = ""
    @Published var password = ""
    @Published var isLoggedIn = false
    @Published var userType: UserType? = nil
    @Published var loginError: String? = nil
    @Published var currentUser: User? = nil

    @Published var currentSelectedProfile: UserType = .personal
    @AppStorage("defaultUserType") var defaultUserType: UserType = .personal

    private let authService = AuthenticationService()

    func login() {
        Task {
            do {
                let uid = try await authService.login(email: email, password: password)
                print("AuthViewModel: User logged in with UID: \(uid)") 
                self.currentUser = try await DatabaseService.shared.getUser(userId: uid)
                
                if let user = self.currentUser {
                    print("AuthViewModel: Current User data fetched: Name: \(user.name), Email: \(user.email), UserType: \(user.userType.rawValue)")
                } else {
                    print("AuthViewModel: Failed to fetch Current User data from DatabaseService.")
                }

                self.userType = currentUser?.userType
                self.currentSelectedProfile = defaultUserType
                isLoggedIn = true
            } catch {
                loginError = "Login failed: \(error.localizedDescription)"
                print("AuthViewModel: Login error: \(error.localizedDescription)") 
            }
        }
    }

    func register(firstName: String, lastName: String) {
        Task {
            do {
                let registrationUserType = self.userType ?? .personal
                let uid = try await authService.register(email: email, password: password)
                print("AuthViewModel: User registered with UID: \(uid)") 

                let newUser = User(id: uid, firstName: firstName, lastName: lastName, email: email, userType: registrationUserType)
                try await DatabaseService.shared.addUser(newUser)
                print("AuthViewModel: New user added to Firestore: \(newUser.name) (\(newUser.email))") 

                currentUser = newUser
                self.userType = newUser.userType
                self.currentSelectedProfile = newUser.userType
                isLoggedIn = true
            } catch {
                loginError = "Registration failed: \(error.localizedDescription)"
                print("AuthViewModel: Registration error: \(error.localizedDescription)") 
            }
        }
    }

    func logout() {
        do {
            try authService.signOut()
            isLoggedIn = false
            email = ""
            password = ""
            currentUser = nil
            currentSelectedProfile = defaultUserType
            print("AuthViewModel: User logged out.") 
        } catch {
            loginError = "Logout failed: \(error.localizedDescription)"
            print("AuthViewModel: Logout error: \(error.localizedDescription)") 
        }
    }

    func updateDefaultUserType(to newType: UserType) {
        defaultUserType = newType
        print("AuthViewModel: Default user type updated to: \(newType.rawValue)") 
    }

    func changePassword(currentPassword: String, newPassword: String) async throws {
        guard let user = Auth.auth().currentUser else {
            throw AuthenticationError.unknown(NSError(domain: "Auth", code: 0, userInfo: [NSLocalizedDescriptionKey: "No authenticated user."]))
        }
        print("AuthViewModel: Attempting to change password for user: \(user.email ?? "N/A")") 

        let credential = EmailAuthProvider.credential(withEmail: user.email ?? "", password: currentPassword)
        _ = try await user.reauthenticate(with: credential)
        print("AuthViewModel: User reauthenticated successfully.") 

        try await user.updatePassword(to: newPassword)
        print("AuthViewModel: Password updated successfully for user: \(user.email ?? "N/A")") 
    }
}
