package com.example.myapplication.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.List;

public class EventAdapter extends ArrayAdapter<EventModel> {
    public EventAdapter(@NonNull Context context, List<EventModel> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EventModel event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);
        if (String.valueOf(event.getTime().getMinute()).length() == 1) {
            String eventTitle = event.getTime().getHour() + ":0" + event.getTime().getMinute() + " "
                    + event.getName() ;
            eventCellTV.setText(eventTitle);
        } else {
            String eventTitle = event.getTime().getHour() + ":" + event.getTime().getMinute() + " "
                    + event.getName() ;
            eventCellTV.setText(eventTitle);
        }
        return convertView;
    }
}
