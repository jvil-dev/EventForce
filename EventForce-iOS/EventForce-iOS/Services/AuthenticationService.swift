//
//  AuthenticationService.swift
//  EventForce-iOS
//
//  Created by Jorge Villeda on 5/19/25.
//

import Foundation
import FirebaseAuth

class AuthenticationService {
    func login(email: String, password: String) async throws -> String {
        do {
            let authResult = try await Auth.auth().signIn(withEmail: email, password: password)
            return authResult.user.uid
        } catch let error as NSError {
            throw try mapAuthError(error)
        }
    }
    
    func register(email: String, password: String) async throws -> String {
        do {
            let authResult = try await Auth.auth().createUser(withEmail: email, password: password)
            return authResult.user.uid
        } catch let error as NSError {
            throw try mapAuthError(error)
        }
    }
    
    func signOut() throws {
        try Auth.auth().signOut()
    }
    
    private func mapAuthError(_ error: NSError) throws -> AuthenticationError {
        switch AuthErrorCode(rawValue: error.code) {
        case.userNotFound:
            return.userNotFound
        case.wrongPassword:
            return.wrongPassword
        case.emailAlreadyInUse:
            return.emailAlreadyInUse
        default:
            return .unknown(error)
        }
    }
}
