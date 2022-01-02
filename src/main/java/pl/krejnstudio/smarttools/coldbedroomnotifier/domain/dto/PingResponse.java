package pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PingResponse {
    private ZonedDateTime checkTime;
}
