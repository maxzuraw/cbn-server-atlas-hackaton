package pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TemperatureFormData {

    private final static DateTimeFormatter SIMPLE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final static DateTimeFormatter SIMPLE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final static DateTimeFormatter SIMPLE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");


    public static final String EUROPE_WARSAW = "Europe/Warsaw";

    private ZonedDateTime createdOn;
    private String sensor;
    private BigDecimal value;

    public String getCreatedOnDate() {
        return getFormattedCreatedOnDate(SIMPLE_DATE_FORMATTER);
    }

    public String getCreatedOnTime() {
        return getFormattedCreatedOnDate(SIMPLE_TIME_FORMATTER);
    }

    private String getFormattedCreatedOnDate(DateTimeFormatter dateTimeFormatter) {
        if(createdOn == null) {
            return "";
        }
        ZoneId zoneId = ZoneId.of(EUROPE_WARSAW);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(createdOn.toInstant(), zoneId);
        return localDateTime.format(dateTimeFormatter);
    }
}
