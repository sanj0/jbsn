package de.sanj0.jbsn;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class Schedule {

    public static final List<String> MONDAY = new ArrayList<>();
    public static final List<String> TUESDAY = new ArrayList<>();
    public static final List<String> WEDNESDAY = new ArrayList<>();
    public static final List<String> THURSDAY = new ArrayList<>();
    public static final List<String> FRIDAY = new ArrayList<>();

    public static List<String> getScheduleByDayOfWeek(final DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return Schedule.MONDAY;
            case TUESDAY:
                return Schedule.TUESDAY;
            case WEDNESDAY:
                return Schedule.WEDNESDAY;
            case THURSDAY:
                return Schedule.THURSDAY;
            case FRIDAY:
                return Schedule.FRIDAY;
            case SATURDAY:
                System.err.println("Warning: Attempt to obtain non-existing school schedule for Saturday!");
                return new ArrayList<>();
            case SUNDAY:
                System.err.println("Warning: Attempt to obtain non-existing school schedule for Sunday!");
                return new ArrayList<>();
        }

        return null;
    }

    /**
     * clears the lists and adds all from the config file
     */
    public static void readFromConfig() {
        MONDAY.clear();
        MONDAY.addAll(readScheduleForDay(AppConfig.MONDAY_KEY));
        TUESDAY.clear();
        TUESDAY.addAll(readScheduleForDay(AppConfig.TUESDAY_KEY));
        WEDNESDAY.clear();
        WEDNESDAY.addAll(readScheduleForDay(AppConfig.WEDNESDAY_KEY));
        THURSDAY.clear();
        THURSDAY.addAll(readScheduleForDay(AppConfig.THURSDAY_KEY));
        FRIDAY.clear();
        FRIDAY.addAll(readScheduleForDay(AppConfig.FRIDAY_KEY));
    }

    /**
     * writes all day's schedules to the app config file
     */
    public static void write() {
        AppConfigManager.APP_CONFIG.setAttribute(AppConfig.MONDAY_KEY, String.join(",", MONDAY));
        AppConfigManager.APP_CONFIG.setAttribute(AppConfig.TUESDAY_KEY, String.join(",", TUESDAY));
        AppConfigManager.APP_CONFIG.setAttribute(AppConfig.WEDNESDAY_KEY, String.join(",", WEDNESDAY));
        AppConfigManager.APP_CONFIG.setAttribute(AppConfig.THURSDAY_KEY, String.join(",", THURSDAY));
        AppConfigManager.APP_CONFIG.setAttribute(AppConfig.FRIDAY_KEY, String.join(",", FRIDAY));
    }

    private static List<String> readScheduleForDay(final String day) {
        final String[] subjects = AppConfigManager.APP_CONFIG.getAttribute(day, "").split(",");
        final List<String> schedule = new ArrayList<>();

        for (String s : subjects) {
            schedule.add(Utils.getSubject(s));
        }
        return schedule;
    }
}
