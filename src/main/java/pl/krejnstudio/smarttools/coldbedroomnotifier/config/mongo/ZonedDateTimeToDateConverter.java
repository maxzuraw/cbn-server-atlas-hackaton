package pl.krejnstudio.smarttools.coldbedroomnotifier.config.mongo;

import org.springframework.core.convert.converter.Converter;

import java.time.ZonedDateTime;
import java.util.Date;

public class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {

    @Override
    public Date convert(ZonedDateTime date) {
        if (date == null) {
            return null;
        }
        return Date.from(date.toInstant());
    }
}
