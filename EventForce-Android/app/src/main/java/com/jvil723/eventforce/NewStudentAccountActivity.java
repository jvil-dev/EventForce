package com.jvil723.eventforce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.jvil723.eventforce.R;

public class NewStudentAccountActivity extends AppCompatActivity {

    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_student_account);

        userDatabase = new UserDatabase(this);

        EditText usernameField = findViewById(R.id.username);
        EditText emailField = findViewById(R.id.email);
        EditText passwordField = findViewById(R.id.password);
        Button createButton = findViewById(R.id.create_account_button);
        Button returnToLoginButton = findViewById(R.id.return_to_login_button);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(NewStudentAccountActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (userDatabase.addStudentUser(username, password, email)) {
                        Toast.makeText(NewStudentAccountActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewStudentAccountActivity.this, StudentLogin.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(NewStudentAccountActivity.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        returnToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewStudentAccountActivity.this, StudentLogin.class);
                startActivity(intent);
            }
        });
    }
}
