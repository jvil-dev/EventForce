//
//  User.swift
//  EventForce-iOS
//
//  Created by Jorge Villeda on 5/19/25.
//

import Foundation
import FirebaseFirestore

struct User: Identifiable, Codable {
    var id: String
    var firstName: String
    var lastName: String
    var email: String
    var userType: UserType
    
    var name: String {
        "\(firstName) \(lastName)"
    }
}
