package com.jvil723.eventforce;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jvil723.eventforce.R;

public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_profile);

        Button personal_use_button = findViewById(R.id.personal_use_button);
        Button student_button = findViewById(R.id.student_button);

        personal_use_button.setOnClickListener(v -> {
            // Start the PersonalLogin
            Intent intent = new Intent(MainActivity.this, PersonalLogin.class);
            startActivity(intent);
        });

        student_button.setOnClickListener(v -> {
            // Start the StudentLogin
            Intent intent = new Intent(MainActivity.this, StudentLogin.class);
            startActivity(intent);
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            showPermissionDialog();
        }
    }

    // Show dialog to request SMS permission
    private void showPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("SMS Permission Required")
                .setMessage("This app needs permission to send SMS notifications for upcoming events.")
                .setPositiveButton("Grant SMS Permission", (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE))
                .setNegativeButton("Skip", null)
                .show();
    }

    // Handle SMS permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
            } else {
                // Permission was denied
            }
        }
    }
}