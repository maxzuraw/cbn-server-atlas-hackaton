package pl.krejnstudio.smarttools.coldbedroomnotifier.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateUtils {

    private LocalDateUtils() {}

    public static LocalDate from(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date from(LocalDate date) {
        if (date == null) {
            return null;
        }
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
