package com.jvil723.eventforce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.jvil723.eventforce.R;

public class PersonalLogin extends AppCompatActivity {

    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_login_screen);

        // Initialize database and UI elements
        userDatabase = new UserDatabase(this);

        EditText usernameField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);
        Button createAccountButton = findViewById(R.id.create_account_button);

        // Login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                // Check if both fields are filled
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(PersonalLogin.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (userDatabase.checkPersonalUser(username, password)) {
                        Toast.makeText(PersonalLogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PersonalLogin.this, EventDisplay.class);
                        intent.putExtra("EVENT_TYPE", "personal");
                        startActivity(intent);
                    } else {
                        Toast.makeText(PersonalLogin.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Create account button click listener
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalLogin.this, NewPersonalAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}
