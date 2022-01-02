package pl.krejnstudio.smarttools.coldbedroomnotifier.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.SensorSettings;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.TempMeasurement;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.MeasurementDTO;

@Service
public class LastMeasurementsService {

    private final TemperatureService temperatureService;
    private final SensorSettingsService sensorService;
    private final TemperatureLevelService temperatureLevelService;

    @Autowired
    public LastMeasurementsService(TemperatureService temperatureService, SensorSettingsService sensorService, TemperatureLevelService temperatureLevelService) {
        this.temperatureService = temperatureService;
        this.sensorService = sensorService;
        this.temperatureLevelService = temperatureLevelService;
    }

    public List<MeasurementDTO> findLastMeasurements() {
        List<SensorSettings> allSensors = sensorService.findAllSensorSettings();
        List<MeasurementDTO> result = new ArrayList<>();
        allSensors.forEach(s -> {
            List<TempMeasurement> lastMeasurements = temperatureService.findLastNMeasurementsForSensor(s.getSensorName(), 1);
            if(lastMeasurements.isEmpty()) {
                return;
            }
            MeasurementDTO measurementDTO = temperatureLevelService.createTemperatureMeasurementDTO(s, lastMeasurements.get(0));
            result.add(measurementDTO);
        });
        return result;
    }

    public List<MeasurementDTO> findLastNMeasurementsForSensor(String sensorName, int elementsAmount) {
        List<SensorSettings> allSensorSettings = sensorService.findAllSensorSettings();
        SensorSettings sensorSettings = allSensorSettings.stream().filter(s -> s.getSensorName().equalsIgnoreCase(sensorName)).findFirst()
            .orElse(null);
        if(sensorSettings == null) {
            return Collections.emptyList();
        }
        List<TempMeasurement> lastMeasurements = temperatureService.findLastNMeasurementsForSensor(sensorName, elementsAmount);
        List<MeasurementDTO> result = new ArrayList<>();
        lastMeasurements.forEach(l -> {
            MeasurementDTO measurementDTO = temperatureLevelService.createTemperatureMeasurementDTO(sensorSettings, l);
            result.add(measurementDTO);
        });
        return result;
    }
}
