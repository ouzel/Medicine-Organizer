package com.example.myapplication.calendar;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.TimePicker;
import com.example.myapplication.med.DatabaseHandler;
import com.example.myapplication.med.MedModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEventActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;

    private LocalTime time;
    private DatePickerDialog datePickerDialog1, datePickerDialog2;
    private String dateStart, dateFinish;
    private DatabaseHandler db;

    int yearFrom, yearTo, monthFrom, monthTo, dayFrom, dayTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        createNotificationChannel();
        initWidgets();
        time = LocalTime.now();
        eventDateTV.setText("Даты: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Время: " + CalendarUtils.formattedTime(time));
        db = new DatabaseHandler(this);
        db.openDatabase();
        initDatePicker();

    }

    private void initWidgets() {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
    }

    public void saveEventAction(View view) {
        String eventName = eventNameET.getText().toString();
        monthFrom += 1;
        monthTo += 1;
        LocalDate eventStart = LocalDate.of(yearFrom, monthFrom, dayFrom);
        while ((yearFrom < yearTo) || (yearFrom == yearTo && monthFrom < monthTo) ||
                (yearFrom == yearTo && monthFrom == monthTo && dayFrom <= dayTo)) {
            EventModel newEvent = new EventModel(eventName, eventStart, time);
            EventModel.eventsList.add(newEvent);
            ReminderBroadcast.notifName = eventName;
            addNotification(eventName, eventStart, time);

            eventStart = eventStart.plusDays(1);
            yearFrom = eventStart.getYear();
            monthFrom = eventStart.getMonthValue();
            dayFrom = eventStart.getDayOfMonth();
        }
        finish();
    }

    private void addNotification(String eventName, LocalDate eventStart, LocalTime time) {
        Toast.makeText(this, "Напоминание установлено!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(AddEventActivity.this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddEventActivity.this, 0,
                intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        LocalDateTime ldt = LocalDateTime.of(eventStart.getYear(), eventStart.getMonthValue(),
                eventStart.getDayOfMonth(), time.getHour(), time.getMinute(), time.getSecond());
        long timeNow = ldt.atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeNow + 1000, pendingIntent);


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MyReminderChannel";
            String description = "new channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notiff", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public List<String> getMedsNames() {
        List<MedModel> medsList = db.getAllMeds();
        List<String> medNamesList = new ArrayList<>();
        for (MedModel mm : medsList) {
            medNamesList.add(mm.getMed());
        }
        return medNamesList;
    }

    public void chooseMedName(View view) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, getMedsNames());
        new AlertDialog.Builder(this)
                .setTitle("Выберите медикамент")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        eventNameET.setText(getMedsNames().get(which));

                        dialog.dismiss();
                    }
                }).create().show();
    }

    public void chooseDateFrom(View view) {
        datePickerDialog1.show();
    }

    public void chooseDateTo(View view) {
        datePickerDialog2.show();
    }

    public void chooseTime(View view) {
        DialogFragment timePicker = new TimePicker();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date = day + "/" + (month + 1) + "/" + year;
                dateStart = date;
                yearFrom = year;
                monthFrom = month;
                dayFrom = day;
                eventDateTV.setText(dateStart + " - " + dateFinish);
            }
        };

        DatePickerDialog.OnDateSetListener dateListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date = day + "/" + (month + 1) + "/" + year;
                dateFinish = date;
                yearTo = year;
                monthTo = month;
                dayTo = day;
                eventDateTV.setText(dateStart + " - " + dateFinish);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        yearFrom = year;
        yearTo = year;
        monthFrom = month;
        monthTo = month;
        dayFrom = day;
        dayTo = day;
        String defaultDate = day + "/" + (month + 1) + "/" + year;
        dateStart = defaultDate;
        dateFinish = defaultDate;
        eventDateTV.setText(dateStart + " - " + dateFinish);
        datePickerDialog1 = new DatePickerDialog(this, dateListener1, year, month, day);
        datePickerDialog2 = new DatePickerDialog(this, dateListener2, year, month, day);
        eventDateTV.setText(dateStart + " - " + dateFinish);

    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day + "/" + month + "/" + year;
    }

    @Override
    public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
        eventTimeTV.setText("Time: " + i + ":" + i1);
        time = LocalTime.of(i, i1);
    }
}