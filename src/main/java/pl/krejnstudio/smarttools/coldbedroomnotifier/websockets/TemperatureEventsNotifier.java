package pl.krejnstudio.smarttools.coldbedroomnotifier.websockets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.ZonedDateTime;

import lombok.extern.slf4j.Slf4j;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.Alert;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.MeasurementDTO;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.TemperatureData;
import pl.krejnstudio.smarttools.coldbedroomnotifier.utils.CbnJsonConverter;

@Slf4j
@Component
@MessageMapping("/cold-room-notifier")
public class TemperatureEventsNotifier {

    private final SimpMessagingTemplate simpTemplate;
    private final CbnJsonConverter converter;

    @Autowired
    public TemperatureEventsNotifier(SimpMessagingTemplate simpMessagingTemplate, CbnJsonConverter converter) {
        simpTemplate = simpMessagingTemplate;
        simpTemplate.setMessageConverter(new MappingJackson2MessageConverter());
        this.converter = converter;
    }

    @Async
    @EventListener
    @CrossOrigin
    public void handleTemperature(MeasurementDTO measurementDTO) {
        String serialized = converter.toJson(measurementDTO);
        log.info("sending temperature data: {}", serialized);
        simpTemplate.convertAndSend("/temperatures", serialized);
    }
}
