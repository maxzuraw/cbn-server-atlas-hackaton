package pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemperatureData {
    private String sensor;
    private BigDecimal value;

    @Override
    public String toString() {
        return "TemperatureData{" +
                "sensor='" + sensor + '\'' +
                ", value=" + value +
                '}';
    }
}
