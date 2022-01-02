package pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GlobalSettingsFormData {
    private Boolean alertsEnabled;
    private ZonedDateTime alertsDisabledFrom;
    private ZonedDateTime alertsDisabledTo;
    private Boolean sensorsInactivityMonitorEnabled;
    private Integer sensorsAllowedInactivityIntervalInMinutes;
}
