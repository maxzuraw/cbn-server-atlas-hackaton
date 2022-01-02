package pl.krejnstudio.smarttools.coldbedroomnotifier.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class ZonedDateTimeUtils {

    private ZonedDateTimeUtils() {}

    public static ZonedDateTime from(Date date) {
        if (date == null) {
            return null;
        }
        return  ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date from(ZonedDateTime date) {
        if (date == null) {
            return null;
        }
        return Date.from(date.toInstant());
    }

}
