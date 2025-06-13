//
//  AuthenticationError.swift
//  EventForce-iOS
//
//  Created by Jorge Villeda on 5/19/25.
//

import Foundation

enum AuthenticationError: Error, LocalizedError {
    case userNotFound
    case wrongPassword
    case emailAlreadyInUse
    case weakPassword
    case unknown(Error)

    var errorDescription: String? {
        switch self {
        case .userNotFound:
            return "No account found with this email."
        case .wrongPassword:
            return "Incorrect password. Please try again."
        case .emailAlreadyInUse:
            return "This email is already associated with another account."
        case .weakPassword:
            return "The password is too weak. Please use a stronger password."
        case .unknown(let error):
            return error.localizedDescription
        }
    }
}
