//
//  SettingsView.swift
//  EventForce-iOS
//
//  Created by Jorge Villeda on 6/8/25.
//

import SwiftUI

struct SettingsView: View {
    @EnvironmentObject var authViewModel: AuthenticationViewModel
    
    var body: some View {
        Form {
            Section("User Information") {
                HStack {
                    Text("Name")
                    Spacer()
                    Text(authViewModel.currentUser?.name ?? "N/A")
                        .foregroundStyle(.gray)
                }
                HStack {
                    Text("Email")
                    Spacer()
                    Text(authViewModel.currentUser?.email ?? "N/A")
                        .foregroundStyle(.gray)
                }
                
                NavigationLink {
                    ChangePasswordView()
                } label: {
                    Text("Change Password")
                }
            }
            
            Section("Default Profile for Events") {
                Picker("Default Profile", selection: $authViewModel.defaultUserType) {
                    Text("Personal").tag(UserType.personal)
                    Text("Student").tag(UserType.student)
                }
                .pickerStyle(.segmented)
            }
            Text("This sets the default profile type when you log in or reset your selection")
                .font(.footnote)
                .foregroundStyle(.gray)
                .listRowBackground(Color.clear)
        }
        .navigationTitle("Settings")
    }
}

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationStack {
            SettingsView()
                .environmentObject(AuthenticationViewModel())
        }
    }
}
