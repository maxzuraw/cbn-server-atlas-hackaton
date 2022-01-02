package pl.krejnstudio.smarttools.coldbedroomnotifier.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "temperatureMeasurement")
@Builder
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TempMeasurement {
    @Id
    private String _id;
    private ZonedDateTime createdOn;
    private String sensor;
    private BigDecimal value;
}
