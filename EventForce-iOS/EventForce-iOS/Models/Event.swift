//
//  Event.swift
//  EventForce-iOS
//
//  Created by Jorge Villeda on 5/19/25.
//

import Foundation
import FirebaseFirestore

struct Event: Identifiable, Codable {
    @DocumentID var id: String?
    var userId: String
    var name: String
    var description: String
    var date: Date
    var type: UserType // "Personal" or "Student"
}
