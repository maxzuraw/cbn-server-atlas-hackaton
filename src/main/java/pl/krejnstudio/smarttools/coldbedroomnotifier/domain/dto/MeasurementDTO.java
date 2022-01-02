package pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.TemperatureLevel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeasurementDTO {
    private String sensor;
    private BigDecimal value;
    private ZonedDateTime createdOn;
    private TemperatureLevel level;
}
