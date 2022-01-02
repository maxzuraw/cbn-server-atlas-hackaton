package pl.krejnstudio.smarttools.coldbedroomnotifier.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.TemperatureFormData;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RangeTitleBuilder {

    public static final DateTimeFormatter ONLY_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.of(TemperatureFormData.EUROPE_WARSAW));
    public static final DateTimeFormatter ONLY_HOUR_AND_MINUTE_FORMATTER = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.of(TemperatureFormData.EUROPE_WARSAW));
    public static final DateTimeFormatter ONLY_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of(TemperatureFormData.EUROPE_WARSAW));
    public static final DateTimeFormatter DATE_AND_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of(TemperatureFormData.EUROPE_WARSAW));

    ZonedDateTime startDate;
    ZonedDateTime endDate;

    public String formatRangeData() {
        String rangeDateStart = startDate.format(ONLY_DATE_FORMATTER);
        String rangeDateEnd = endDate.format(ONLY_DATE_FORMATTER);
        String rangeTimeStart = startDate.format(ONLY_TIME_FORMATTER);
        String rangeTimeEnd = endDate.format(ONLY_TIME_FORMATTER);
        StringBuilder sb = new StringBuilder();
        if(rangeDateStart.equals(rangeDateEnd)) {
            sb.append(rangeDateStart);
            sb.append(" (");
            sb.append(rangeTimeStart);
            sb.append(" - ");
            sb.append(rangeTimeEnd);
            sb.append(")");
        }else {
            sb.append(rangeDateStart);
            sb.append(" ");
            sb.append(rangeTimeStart);
            sb.append(" - ");
            sb.append(rangeDateEnd);
            sb.append(" ");
            sb.append(rangeTimeEnd);
        }
        return sb.toString();
    }
}
