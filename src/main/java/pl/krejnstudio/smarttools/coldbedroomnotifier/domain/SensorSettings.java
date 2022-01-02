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

@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sensorSettings")
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SensorSettings {
    @Id
    private String _id;
    private String sensorName;
    private BigDecimal maxValue;
    private BigDecimal minValue;
    private Boolean alertEnabled;
}
