package com.jvil723.eventforce;

public class Event {
    private int eventId;
    private String eventName;
    private String eventDate;
    private String eventTime;
    private String eventDescription;

    public Event(int eventId, String eventName, String eventDate, String eventTime, String eventDescription) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventDescription = eventDescription;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {

        return eventName;
    }

    public void setEventName(String eventName) {

        this.eventName = eventName;
    }

    public String getEventDate() {

        return eventDate;
    }

    public void setEventDate(String eventDate) {

        this.eventDate = eventDate;
    }

    public String getEventTime() {

        return eventTime;
    }

    public void setEventTime(String eventTime) {

        this.eventTime = eventTime;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}
