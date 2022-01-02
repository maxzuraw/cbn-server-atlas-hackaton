package pl.krejnstudio.smarttools.coldbedroomnotifier.controllers.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.PingResponse;

@RestController
@RequestMapping("/api/ping")
public class PingRestController {

    @GetMapping()
    public PingResponse alive() {
        return PingResponse.builder().checkTime(ZonedDateTime.now()).build();
    }
}
