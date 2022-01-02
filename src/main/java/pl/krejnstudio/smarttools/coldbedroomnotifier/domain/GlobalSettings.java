package pl.krejnstudio.smarttools.coldbedroomnotifier.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "globalSettings")
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GlobalSettings {
    @Id
    private String _id;
    private Boolean alertsEnabled; // NOTE: ignore alerts true -> send alerts, false -> don't send alerts
    private ZonedDateTime alertsDisabledFrom;
    private ZonedDateTime alertsDisabledTo; // NOTE: can be null, if alertsEnabled is false, and this is not set - alerts are disabled infinitely
    private Boolean sensorsInactivityMonitorEnabled; // NOTE: monitor of sensors inactivity on/off
    private Integer sensorsAllowedInactivityIntervalInMinutes; // NOTE: if last measurement from sensors - now() > this -> rise alert
    private ZonedDateTime createdOn;
    private ZonedDateTime lastModifiedOn;
}
