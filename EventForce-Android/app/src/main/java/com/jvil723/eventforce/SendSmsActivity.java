package com.jvil723.eventforce;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.jvil723.eventforce.R;

public class SendSmsActivity extends AppCompatActivity {

    private UserDatabase userDatabase;
    private EditText phoneNumberField;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_sms_activity);

        // Initialize database and UI elements
        userDatabase = new UserDatabase(this);
        phoneNumberField = findViewById(R.id.Phone_Number);
        Button savePhoneNumberButton = findViewById(R.id.Save_Number);

        userType = getIntent().getStringExtra("USER_TYPE");

        // Save phone number to database
        savePhoneNumberButton.setOnClickListener(v -> {
            String phoneNumber = phoneNumberField.getText().toString();
            if (phoneNumber.isEmpty()) {
                Toast.makeText(SendSmsActivity.this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
            } else {
                if (userDatabase.savePhoneNumber(phoneNumber, userType)) {
                    Toast.makeText(SendSmsActivity.this, "Phone number saved successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(SendSmsActivity.this, "Failed to save phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}