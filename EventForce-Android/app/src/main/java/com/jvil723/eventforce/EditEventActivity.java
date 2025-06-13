package com.jvil723.eventforce;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.jvil723.eventforce.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditEventActivity extends AppCompatActivity {

    private EventDatabase eventDatabase;
    private int eventId;
    private EditText eventDateField, eventTimeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event_activity);

        // Initialize the EventDatabase
        eventDatabase = new EventDatabase(this);

        EditText eventNameField = findViewById(R.id.event_name);
        eventDateField = findViewById(R.id.event_date);
        eventTimeField = findViewById(R.id.event_time);
        EditText eventDescriptionField = findViewById(R.id.event_description);
        Button saveEventButton = findViewById(R.id.save_event_button);
        Button deleteEventButton = findViewById(R.id.delete_event_button);

        // Get the event details from the intent
        Intent intent = getIntent();
        eventId = intent.getIntExtra("EVENT_ID", -1);
        String eventName = intent.getStringExtra("EVENT_NAME");
        String eventDate = intent.getStringExtra("EVENT_DATE");
        String eventTime = intent.getStringExtra("EVENT_TIME");
        String eventDescription = intent.getStringExtra("EVENT_DESCRIPTION");

        // Set the event details in the UI
        eventNameField.setText(eventName);
        eventDateField.setText(eventDate);
        eventTimeField.setText(eventTime);
        eventDescriptionField.setText(eventDescription);

        eventDateField.setOnClickListener(v -> showDatePickerDialog());
        eventTimeField.setOnClickListener(v -> showTimePickerDialog());

        // Save the updated event details to the database
        saveEventButton.setOnClickListener(v -> {
            String updatedEventName = eventNameField.getText().toString();
            String updatedEventDate = eventDateField.getText().toString();
            String updatedEventTime = eventTimeField.getText().toString();
            String updatedEventDescription = eventDescriptionField.getText().toString();

            if (updatedEventName.isEmpty() || updatedEventDate.isEmpty() || updatedEventTime.isEmpty() || updatedEventDescription.isEmpty()) {
                Toast.makeText(EditEventActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                if (eventDatabase.updateEvent(eventId, updatedEventName, updatedEventDate, updatedEventTime, updatedEventDescription)) {
                    Toast.makeText(EditEventActivity.this, "Event updated successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(EditEventActivity.this, "Failed to update event", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Delete the event from the database
        deleteEventButton.setOnClickListener(v -> {
            if (eventDatabase.deleteEvent(eventId)) {
                Toast.makeText(EditEventActivity.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(EditEventActivity.this, "Failed to delete event", Toast.LENGTH_SHORT).show();
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
