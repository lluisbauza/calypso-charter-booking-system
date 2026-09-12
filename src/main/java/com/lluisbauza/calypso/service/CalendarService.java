package com.lluisbauza.calypso.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalendarService {

    public List<List<LocalDate>> generateMonth(int year, int month) {

        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDate firstDay = yearMonth.atDay(1);

        int daysInMonth = yearMonth.lengthOfMonth();

        int firstDayOfWeek = firstDay.getDayOfWeek().getValue();

        int nulls = firstDayOfWeek - 1;

        List<List<LocalDate>> weeks = new ArrayList<>();

        List<LocalDate> currentWeek = new ArrayList<>();

        for (int i = 0; i < nulls; i++) {
            currentWeek.add(null);
        }

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = yearMonth.atDay(day);
            currentWeek.add(date);

            if (currentWeek.size() == 7) {
                weeks.add(currentWeek);
                currentWeek = new ArrayList<>();
            }
        }

        if (!currentWeek.isEmpty()) {
            weeks.add(currentWeek);
        }

        return weeks;
    }
}
