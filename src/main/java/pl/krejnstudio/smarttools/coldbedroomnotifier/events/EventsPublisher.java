package pl.krejnstudio.smarttools.coldbedroomnotifier.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;

import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.Alert;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.SensorSettings;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.MeasurementDTO;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.TemperatureData;
import pl.krejnstudio.smarttools.coldbedroomnotifier.services.TemperatureLevelService;

@Component
public class EventsPublisher {

    private final ApplicationEventPublisher eventPublisher;
    private final TemperatureLevelService temperatureLevelService;

    @Autowired
    public EventsPublisher(ApplicationEventPublisher eventPublisher, TemperatureLevelService temperatureLevelService) {
        this.eventPublisher = eventPublisher;
        this.temperatureLevelService = temperatureLevelService;
    }

    @Async
    public void publishTemperature(SensorSettings sensorSettings, TemperatureData temperatureData) {
        MeasurementDTO measurementDTO = temperatureLevelService.createTemperatureMeasurementDTO(sensorSettings, temperatureData.getValue());
        eventPublisher.publishEvent(measurementDTO);
    }
}
