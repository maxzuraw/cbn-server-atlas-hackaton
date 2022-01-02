package pl.krejnstudio.smarttools.coldbedroomnotifier.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.SensorSettings;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.TemperatureData;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.repositories.SensorSettingsRepository;
import pl.krejnstudio.smarttools.coldbedroomnotifier.events.EventsPublisher;
import pl.krejnstudio.smarttools.coldbedroomnotifier.services.TemperatureService;

@RestController
@RequestMapping("/api/temperature")
public class TemperatureController {

    private final TemperatureService temperatureService;
    private final SensorSettingsRepository sensorSettingsRepository;
    private final EventsPublisher eventsPublisher;

    @Autowired
    public TemperatureController(TemperatureService temperatureService,
                                 SensorSettingsRepository sensorSettingsRepository,
                                 EventsPublisher eventsPublisher) {
        this.temperatureService = temperatureService;
        this.sensorSettingsRepository = sensorSettingsRepository;
        this.eventsPublisher = eventsPublisher;
    }

    @PostMapping()
    public void dataImport(@RequestBody TemperatureData temperatureData) throws NotFoundException, JsonProcessingException {
        Assert.notNull(temperatureData, "temperatureData cannot be null");
        Assert.notNull(temperatureData.getSensor(), "sensor name cannot be null");
        SensorSettings sensorSettings = sensorSettingsRepository.findBySensorName(temperatureData.getSensor());
        if(sensorSettings == null){
            throw new NotFoundException("sensor not found");
        }
        temperatureService.insertTemperature(temperatureData);
        eventsPublisher.publishTemperature(sensorSettings, temperatureData);
    }
}
