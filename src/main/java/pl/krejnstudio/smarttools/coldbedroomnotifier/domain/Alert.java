package pl.krejnstudio.smarttools.coldbedroomnotifier.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "alerts")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Alert {
    @Id
    private String _id;
    private String sensorName;
    private TemperatureLevel level;
    private BigDecimal value;
    private ZonedDateTime createdOn;
    private Boolean extinguished;
}
