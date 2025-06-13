package com.jvil723.eventforce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.jvil723.eventforce.R;

public class StudentLogin extends AppCompatActivity {

    private UserDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_login_screen);

        userDatabase = new UserDatabase(this);

        EditText usernameField = findViewById(R.id.username);
        EditText passwordField = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);
        Button createAccountButton = findViewById(R.id.create_account_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(StudentLogin.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (userDatabase.checkStudentUser(username, password)) {
                        Toast.makeText(StudentLogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(StudentLogin.this, EventDisplay.class);
                        intent.putExtra("EVENT_TYPE", "student");
                        startActivity(intent);
                    } else {
                        Toast.makeText(StudentLogin.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentLogin.this, NewStudentAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}
