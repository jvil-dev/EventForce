package com.jvil723.eventforce;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.jvil723.eventforce.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {

    private EventDatabase eventDatabase;
    private EditText eventDateField, eventTimeField;
    private String eventType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_activity);

        eventDatabase = new EventDatabase(this);

        eventType = getIntent().getStringExtra("EVENT_TYPE");

        EditText eventNameField = findViewById(R.id.event_name);
        eventDateField = findViewById(R.id.event_date);
        eventTimeField = findViewById(R.id.event_time);
        EditText eventDescriptionField = findViewById(R.id.event_description);
        Button saveEventButton = findViewById(R.id.save_event_button);

        eventDateField.setOnClickListener(v -> showDatePickerDialog());
        eventTimeField.setOnClickListener(v -> showTimePickerDialog());

        // Save event to database
        saveEventButton.setOnClickListener(v -> {
            String eventName = eventNameField.getText().toString();
            String eventDate = eventDateField.getText().toString();
            String eventTime = eventTimeField.getText().toString();
            String eventDescription = eventDescriptionField.getText().toString();

            if (eventName.isEmpty() || eventDate.isEmpty() || eventTime.isEmpty() || eventDescription.isEmpty()) {
                Toast.makeText(AddEventActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                if (eventDatabase.addEvent(eventName, eventDate, eventTime, eventDescription, eventType)) {
                    Toast.makeText(AddEventActivity.this, "Event added successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(AddEventActivity.this, "Failed to add event", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Show date picker dialog
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year1, monthOfYear, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yy");
                    eventDateField.setText(dateFormat.format(selectedDate.getTime()));
                },
                year, month, day);
        datePickerDialog.show();
    }

    // Show time picker dialog
    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minuteOfHour) -> {
                    Calendar selectedTime = Calendar.getInstance();
                    selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedTime.set(Calendar.MINUTE, minuteOfHour);
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                    eventTimeField.setText(timeFormat.format(selectedTime.getTime()));
                },
                hour, minute, false);
        timePickerDialog.show();
    }
}
