package pl.krejnstudio.smarttools.coldbedroomnotifier.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.SensorSettings;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.SensorSettingsFormData;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.mappers.SensorsMapper;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.repositories.SensorSettingsRepository;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
public class SensorsRestController {

    private final SensorSettingsRepository sensorsRepository;
    private final SensorsMapper sensorsMapper;

    @Autowired
    public SensorsRestController(SensorSettingsRepository sensorsRepository, SensorsMapper sensorsMapper) {
        this.sensorsRepository = sensorsRepository;
        this.sensorsMapper = sensorsMapper;
    }

    @GetMapping()
    public List<SensorSettingsFormData> getSensors() {
        List<SensorSettings> allSensors = sensorsRepository.findAll();
        return sensorsMapper.mapList(allSensors, SensorSettingsFormData.class);
    }
}
