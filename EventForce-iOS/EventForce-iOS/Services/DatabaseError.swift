//
//  DatabaseError.swift
//  EventForce-iOS
//
//  Created by Jorge Villeda on 5/24/25.
//

enum DatabaseError: Error {
    case failedToSave
    case notFound
    case unknown(Error)
}

