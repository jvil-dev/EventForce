package com.jvil723.eventforce;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jvil723.eventforce.R;

import java.util.List;

// Adapter class for the RecyclerView in the EventDisplay activity
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final List<Event> eventList;
    private final Context context;
    private final ActivityResultLauncher<Intent> editEventLauncher;

    // Constructor for the EventAdapter class
    public EventAdapter(List<Event> eventList, Context context, ActivityResultLauncher<Intent> editEventLauncher) {
        this.eventList = eventList;
        this.context = context;
        this.editEventLauncher = editEventLauncher;
    }

    // Create a new view holder when there are no existing view holders that can be reused
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    // Bind the data to the views in the RecyclerView
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.eventName.setText(event.getEventName());
        holder.eventTime.setText(event.getEventTime());
        holder.eventDescription.setText(event.getEventDescription());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditEventActivity.class);
            intent.putExtra("EVENT_ID", event.getEventId());
            intent.putExtra("EVENT_NAME", event.getEventName());
            intent.putExtra("EVENT_DATE", event.getEventDate());
            intent.putExtra("EVENT_TIME", event.getEventTime());
            intent.putExtra("EVENT_DESCRIPTION", event.getEventDescription());
            editEventLauncher.launch(intent);
        });
    }

    // Return the number of items in the RecyclerView
    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventTime, eventDescription;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventTime = itemView.findViewById(R.id.event_time);
            eventDescription = itemView.findViewById(R.id.event_description);
        }
    }
}
