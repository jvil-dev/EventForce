//
//  EventForce_iOSApp.swift
//  EventForce-iOS
//
//  Created by Jorge Villeda on 5/19/25.
//

import SwiftUI
import Firebase

@main
struct EventForce_iOSApp: App {
    @StateObject private var authViewModel = AuthenticationViewModel()
    init() {
        FirebaseApp.configure()
    }
    
    var body: some Scene {
        WindowGroup {   
            WelcomeView()
                .environmentObject(authViewModel)
        }
    }
}
