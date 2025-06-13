//
//  LoginFormView.swift
//  EventForce-iOS
//
//  Created by Jorge Villeda on 5/24/25.
//

import SwiftUI

struct LoginFormView: View {
    @ObservedObject var viewModel: AuthenticationViewModel
    
    @State private var isRegistering = false
    
    @State private var firstName: String = ""
    @State private var lastName: String = ""
    
    
    var body: some View {
        VStack(spacing: 16) {
            Text(isRegistering ? "Register New Account" : "Login to EventForce")
                .font(.title2)
                .bold()
            
            if isRegistering {
                TextField("First Name", text: $firstName)
                    .textInputAutocapitalization(.words)
                    .textFieldStyle(.roundedBorder)
                    .accessibilityLabel("First Name")
                
                TextField("Last Name", text: $lastName)
                    .textInputAutocapitalization(.words)
                    .textFieldStyle(.roundedBorder)
                    .accessibilityLabel("Last Name")
            }
            
            TextField("Email", text: $viewModel.email)
                .keyboardType(.emailAddress)
                .autocapitalization(.none)
                .textFieldStyle(.roundedBorder)
            
            SecureField("Password", text: $viewModel.password)
                .textFieldStyle(.roundedBorder)
            
            if let error = viewModel.loginError {
                Text(error)
                    .foregroundColor(.red)
                    .multilineTextAlignment(.center)
            }
            
            Button(isRegistering ? "Register" : "Login") {
                if isRegistering {
                    viewModel.register(firstName: firstName, lastName: lastName)
                } else {
                    viewModel.login()
                }
            }
            .buttonStyle(.borderedProminent)
            
            Button(isRegistering ? "Already have an account? Log in" : "Don't have an account? Register") {
                isRegistering.toggle()
                if !isRegistering {
                    firstName = ""
                    lastName = ""
                    viewModel.email = ""
                    viewModel.password = ""
                    viewModel.loginError = nil
                }
            }
            .font(.footnote)
        }
        .onAppear {
            isRegistering = false
            firstName = ""
            lastName = ""
            viewModel.loginError = nil
        }
    }
}
