package com.example.myapplication.calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtils {
    public static LocalDate selectedDate = LocalDate.now();

    public static String formattedDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        String current = date.format(formatter);
        current = current.replace("January", "Январь");
        current = current.replace("February", "Февраль");
        current = current.replace("March", "Март");
        current = current.replace("April", "Апрель");
        current = current.replace("May", "Май");
        current = current.replace("June", "Июнь");
        current = current.replace("July", "Июль");
        current = current.replace("August", "Август");
        current = current.replace("September", "Сентябрь");
        current = current.replace("October", "Октябрь");
        current = current.replace("November", "Ноябрь");
        current = current.replace("December", "Декабрь");
        return current;
    }

    public static String formattedTime(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return time.format(formatter);
    }

    public static String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        String current = date.format(formatter);
        current = current.replace("January", "Январь");
        current = current.replace("February", "Февраль");
        current = current.replace("March", "Март");
        current = current.replace("April", "Апрель");
        current = current.replace("May", "Май");
        current = current.replace("June", "Июнь");
        current = current.replace("July", "Июль");
        current = current.replace("August", "Август");
        current = current.replace("September", "Сентябрь");
        current = current.replace("October", "Октябрь");
        current = current.replace("November", "Ноябрь");
        current = current.replace("December", "Декабрь");
        return current;
    }

    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate) {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = sundayForDate(selectedDate);
        LocalDate endDate = current.plusWeeks(1);

        while (current.isBefore(endDate)) {
            days.add(current);
            current = current.plusDays(1);
        }
        return days;
    }

    private static LocalDate sundayForDate(LocalDate current) {
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while (current.isAfter(oneWeekAgo)) {
            if (current.getDayOfWeek() == DayOfWeek.SUNDAY)
                return current;

            current = current.minusDays(1);
        }

        return null;
    }


}
