package pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardData {
    private String sensorName;
    private ZonedDateTime createdOn;
    private BigDecimal lastMeasurement;
    private BigDecimal maxValue;
    private BigDecimal minValue;
    private String ownChartTitle;
    private String ownChartId;
    private List<List<Object>> ownChartElements;
}
