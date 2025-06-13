package com.jvil723.eventforce;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jvil723.eventforce.R;

import java.util.ArrayList;
import java.util.List;

public class EventDisplay extends AppCompatActivity {

    private EventDatabase eventDatabase;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private String eventType;

    // Add Event Activity result launchers
    private final ActivityResultLauncher<Intent> addEventLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    displayEvents();
                }
            });

    // Edit Event Activity result launchers
    private final ActivityResultLauncher<Intent> editEventLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    displayEvents();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_display);

        eventDatabase = new EventDatabase(this);
        UserDatabase userDatabase = new UserDatabase(this);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        Button addEventButton = findViewById(R.id.add_event_button);
        Button logoutButton = findViewById(R.id.logout_button);

        eventType = getIntent().getStringExtra("EVENT_TYPE");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList, this, editEventLauncher);
        recyclerView.setAdapter(eventAdapter);

        displayEvents();

        // Check if phone number is set
        if (userDatabase.getPhoneNumber(eventType) == null) {
            Intent intent = new Intent(EventDisplay.this, SendSmsActivity.class);
            intent.putExtra("USER_TYPE", eventType);
            startActivity(intent);
        }

        // Add event button
        addEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(EventDisplay.this, AddEventActivity.class);
            intent.putExtra("EVENT_TYPE", eventType); // Pass the event type
            addEventLauncher.launch(intent);
        });

        // Logout button
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(EventDisplay.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Display events in recycler view
    private void displayEvents() {
        eventList.clear();
        Cursor cursor = eventDatabase.getAllEvents(eventType);
        if (cursor.moveToFirst()) {
            do {
                int eventId = cursor.getInt(cursor.getColumnIndex("EVENT_ID"));
                String eventName = cursor.getString(cursor.getColumnIndex("EVENT_NAME"));
                String eventDate = cursor.getString(cursor.getColumnIndex("EVENT_DATE"));
                String eventTime = cursor.getString(cursor.getColumnIndex("EVENT_TIME"));
                String eventDescription = cursor.getString(cursor.getColumnIndex("EVENT_DESCRIPTION"));

                Event event = new Event(eventId, eventName, eventDate, eventTime, eventDescription);
                eventList.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();
        eventAdapter.notifyDataSetChanged();
    }
}
