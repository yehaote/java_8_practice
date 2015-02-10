package mi.practice.java.eight.effective;

import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Locale;
import java.util.TimeZone;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.nextOrSame;

public class DateDemo {
    public static void main(String[] args) {
        //LocalDate, LocalTime, LocalDateTime, Instant, Duration, Period
        dateIsJustDate();
        timeIsJustTime();
        instantForMatchine();
        durationAndPeriod();
        manipulateLocalDate();
        temporal();
        dateFormatter();
        timeZone();
    }

    private static void timeZone() {
        ZoneId romeZone = ZoneId.of("Europe/Rome");
        ZoneId zoneId = TimeZone.getDefault().toZoneId();
        LocalDate date = LocalDate.of(2014, Month.MARCH, 18);
        ZonedDateTime zdt1 = date.atStartOfDay(romeZone);

        LocalDateTime dateTime = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45);
        ZonedDateTime zdt2 = dateTime.atZone(romeZone);

        Instant instant = Instant.now();
        ZonedDateTime zdt3 = instant.atZone(romeZone);

        LocalDateTime timeFromInstant = LocalDateTime.ofInstant(instant, romeZone);

        ZoneOffset newYorkOffset = ZoneOffset.of("-05:00");
        OffsetDateTime dateTimeInNewYork = OffsetDateTime.of(dateTime, newYorkOffset);

        JapaneseDate japaneseDate = JapaneseDate.from(date);

        Chronology japaneseChronology = Chronology.ofLocale(Locale.JAPAN);
        ChronoLocalDate now = japaneseChronology.dateNow();
    }

    private static void dateFormatter() {
        LocalDate date = LocalDate.of(2014, 3, 18);
        String s1 = date.format(DateTimeFormatter.BASIC_ISO_DATE);
        System.out.println(s1);
        String s2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.println(s2);

        LocalDate date1 = LocalDate.parse("20140318", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate date2 = LocalDate.parse("2014-03-18", DateTimeFormatter.ISO_LOCAL_DATE);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        date1 = LocalDate.of(2014, 3, 18);
        String formattedDate = date1.format(formatter);
        date2 = LocalDate.parse(formattedDate, formatter);

        DateTimeFormatter cnFormatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.CHINA);
        date1 = LocalDate.of(2014, 3, 18);
        String cnFormattedDate = date1.format(cnFormatter);
        System.out.println(cnFormattedDate);
        date2 = LocalDate.parse(cnFormattedDate, cnFormatter);

        DateTimeFormatter italianFormatter = new DateTimeFormatterBuilder()
                .appendText(ChronoField.DAY_OF_MONTH)
                .appendLiteral(".")
                .appendText(ChronoField.MONTH_OF_YEAR)
                .appendLiteral(" ")
                .appendText(ChronoField.YEAR)
                .parseCaseInsensitive()
                .toFormatter(Locale.ITALIAN);
    }

    private static void temporal() {
        LocalDate date1 = LocalDate.of(2014, 3, 18);
        LocalDate date2 = date1.with(nextOrSame(DayOfWeek.SUNDAY));
        LocalDate date3 = date1.with(lastDayOfMonth());

        LocalDate date = LocalDate.of(2015, 2, 13);
        date = date.with(new NextWorkingDay());
        System.out.println(date);
        date = date.with((temporal) -> {
            temporal = temporal.plus(1, ChronoUnit.DAYS);
            while (temporal.get(ChronoField.DAY_OF_WEEK) != DayOfWeek.SATURDAY.getValue()
                    && temporal.get(ChronoField.DAY_OF_WEEK) != DayOfWeek.SUNDAY.getValue()) {
                temporal = temporal.plus(1, ChronoUnit.DAYS);
            }
            return temporal;
        });
        System.out.println(date);
    }

    private static void manipulateLocalDate() {
        LocalDate date1 = LocalDate.of(2014, 3, 18);
        LocalDate date2 = date1.withYear(2011);
        LocalDate date3 = date2.withDayOfMonth(25);
        LocalDate date4 = date3.with(ChronoField.MONTH_OF_YEAR, 9);

        date2 = date1.plusWeeks(1);
        date3 = date2.minusYears(3);
        date4 = date3.plus(6, ChronoUnit.MONTHS);

        LocalDate date = LocalDate.of(2014, 3, 18);
        date = date.with(ChronoField.MONTH_OF_YEAR, 9);// 2014-9-18
        date = date.plusYears(2).minusDays(10);// 2016-9-8
        date.withYear(2011);// return a new 2011-9-8
        System.out.println(date);
    }

    private static void durationAndPeriod() {
        Period tenDays = Period.between(LocalDate.of(2014, 3, 8),
                LocalDate.of(2014, 3, 18));
        Duration threeMinutes = Duration.ofMinutes(3);
        threeMinutes = Duration.of(3, ChronoUnit.MINUTES);

        tenDays = Period.ofDays(10);
        Period threeWeeks = Period.ofWeeks(3);
        Period twoYearsSixMonthsOneDay = Period.of(2, 6, 1);
    }

    private static void instantForMatchine() {
        Instant instant1 = Instant.ofEpochSecond(3);
        Instant instant2 = Instant.ofEpochSecond(3, 0);
        Instant.ofEpochSecond(2, 1_000_000_000);
        Instant.ofEpochSecond(4, -1_000_000_000);
        Duration duration = Duration.between(instant1, instant2);
        // int day = Instant.now().get(ChronoField.DAY_OF_MONTH);
    }

    private static void timeIsJustTime() {
        LocalTime time = LocalTime.of(13, 45, 20);
        int hour = time.getHour();
        System.out.println(hour);
        int minute = time.getMinute();
        System.out.println(minute);
        int second = time.getSecond();
        System.out.println(second);

        LocalDate date = LocalDate.parse("2014-03-18");
        time = LocalTime.parse("13:45:20");

        LocalDateTime dt1 = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45, 20);
        LocalDateTime dt2 = LocalDateTime.of(date, time);
        LocalDateTime dt3 = date.atTime(13, 45, 20);
        LocalDateTime dt4 = date.atTime(time);
        LocalDateTime dt5 = time.atDate(date);

        LocalDate date1 = dt1.toLocalDate();
        LocalTime time1 = dt1.toLocalTime();
    }

    private static void dateIsJustDate() {
        // localDate is immutable
        LocalDate date = LocalDate.of(2014, 3, 18);
        int year = date.getYear();
        System.out.println(year);
        Month month = date.getMonth();
        System.out.println(month);
        int day = date.getDayOfMonth();
        System.out.println(day);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        System.out.println(dayOfWeek);
        int len = date.lengthOfMonth();
        System.out.println(len);
        boolean leap = date.isLeapYear();
        System.out.println(leap);

        LocalDate today = LocalDate.now();
        System.out.println(today);

        year = date.get(ChronoField.YEAR);
        int monthInt = date.get(ChronoField.MONTH_OF_YEAR);
        day = date.get(ChronoField.DAY_OF_MONTH);
    }
}

class NextWorkingDay implements TemporalAdjuster {
    @Override public Temporal adjustInto(Temporal temporal) {
        temporal = temporal.plus(1, ChronoUnit.DAYS);
        while (temporal.get(ChronoField.DAY_OF_WEEK) == DayOfWeek.SATURDAY.getValue()
                || temporal.get(ChronoField.DAY_OF_WEEK) == DayOfWeek.SUNDAY.getValue()) {
            temporal = temporal.plus(1, ChronoUnit.DAYS);
        }
        return temporal;
    }
}