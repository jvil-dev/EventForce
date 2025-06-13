//
//  WelcomeView.swift
//  EventForce-iOS
//
//  Created by Jorge Villeda on 5/24/25.
//

import SwiftUI

struct WelcomeView: View {
    @EnvironmentObject var viewModel: AuthenticationViewModel

    var body: some View {
        NavigationStack {
            ZStack {
                Color("AppBackground")
                    .ignoresSafeArea()

                VStack(spacing: 24) {
                    Image("welcome_image")
                        .resizable()
                        .scaledToFit()
                        .frame(height: 160)
                        .padding(.top, 32)
                        .accessibilityLabel("Welcome Image")

                    Text("Welcome to EventForce")
                        .font(.largeTitle)
                        .fontWeight(.semibold)
                        .multilineTextAlignment(.center)
                        .foregroundColor(.primary)
                    
                    LoginFormView(viewModel: viewModel)
                        .transition(.move(edge: .bottom))

                    Spacer()
                }
                .animation(.easeInOut, value: viewModel.isLoggedIn)
            }
            .navigationDestination(isPresented: $viewModel.isLoggedIn) {
                EventListView()
            }
        }
    }
    
    struct WelcomeView_Previews: PreviewProvider {
        static var previews: some View {
            WelcomeView()
                .environmentObject(AuthenticationViewModel())
        }
    }
}
