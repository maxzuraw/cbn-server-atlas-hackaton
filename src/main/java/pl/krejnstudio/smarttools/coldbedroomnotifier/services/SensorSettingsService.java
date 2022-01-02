package pl.krejnstudio.smarttools.coldbedroomnotifier.services;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.SensorSettings;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.SensorSettingsFormData;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.mappers.SensorsMapper;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.repositories.SensorSettingsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SensorSettingsService {

    private final SensorSettingsRepository repository;
    private final SensorsMapper sensorsMapper;
    private final TemperatureService temperatureService;

    @Autowired
    public SensorSettingsService(SensorSettingsRepository repository, SensorsMapper sensorsMapper, TemperatureService temperatureService) {
        this.repository = repository;
        this.sensorsMapper = sensorsMapper;
        this.temperatureService = temperatureService;
    }

    public SensorSettingsFormData findById(String id) {
        SensorSettings sensorSettings = repository.findBy_id(id);
        return sensorsMapper.map(sensorSettings, SensorSettingsFormData.class);
    }

    public SensorSettings createOrUpdate(SensorSettingsFormData formData) {
        SensorSettings sensorSettings = sensorsMapper.map(formData, SensorSettings.class);
        if(StringUtils.isBlank(formData.getId())) {
            return repository.insert(sensorSettings);
        }
        return repository.save(sensorSettings);
    }

    public List<SensorSettings> findAllSensorSettings() {
        List<SensorSettings> sensorSettings = repository.findAll();
        return sensorSettings;
    }

    public void deleteSensorSettings(String id) {
        Optional<SensorSettings> sensorSettings = repository.findById(id);
        if(sensorSettings.isPresent()) {
            temperatureService.deleteAllBySensorName(sensorSettings.get().getSensorName());
            repository.delete(sensorSettings.get());
        }
    }
}
