package pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SensorSettingsFormData {
    private String id;
    @NotBlank
    private String sensorName;
    @NotNull
    private BigDecimal maxValue;
    @NotNull
    private BigDecimal minValue;
    @NotNull
    private Boolean alertEnabled;
}
