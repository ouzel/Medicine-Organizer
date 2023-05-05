package com.example.myapplication.calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class EventModel {
    public static ArrayList<EventModel> eventsList = new ArrayList<>();

    public static ArrayList<EventModel> eventsForDate(LocalDate date) {
        ArrayList<EventModel> events = new ArrayList<>();

        for (EventModel event : eventsList) {
            if (event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }

    private String name;
    private LocalDate date;
    private LocalTime time;

    public EventModel(String name, LocalDate date, LocalTime time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }
}
