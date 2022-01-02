package pl.krejnstudio.smarttools.coldbedroomnotifier.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.Alert;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.TempMeasurement;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.TemperatureLevel;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.SensorSettings;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.MeasurementDTO;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.TemperatureData;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.repositories.AlertsRepository;
import pl.krejnstudio.smarttools.coldbedroomnotifier.utils.BigDecimalUtils;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class TemperatureLevelService {

    private final AlertsRepository alertsRepository;

    @Autowired
    public TemperatureLevelService(AlertsRepository alertsRepository) {
        this.alertsRepository = alertsRepository;
    }

    public MeasurementDTO createTemperatureMeasurementDTO(SensorSettings sensorSettings, BigDecimal value) {
        Boolean alertEnabled = sensorSettings.getAlertEnabled();
        MeasurementDTO measurementDTO = createMeasurementDTOOutline(sensorSettings.getSensorName(), value);
        measurementDTO.setLevel(TemperatureLevel.NORMAL);
        if(alertEnabled) {
            BigDecimal maxValue = sensorSettings.getMaxValue();
            BigDecimal minValue = sensorSettings.getMinValue();
            BigDecimalUtils bigDecimalUtils = new BigDecimalUtils();

            // NOTE: sensor temperature > maxValue
            if (bigDecimalUtils.compare(maxValue, measurementDTO.getValue()) < 0) {
                measurementDTO.setLevel(TemperatureLevel.HIGH);
            }
            if (bigDecimalUtils.compare(measurementDTO.getValue(), minValue) < 0) {
                measurementDTO.setLevel(TemperatureLevel.LOW);
            }
        }
        return measurementDTO;
    }

    public MeasurementDTO createTemperatureMeasurementDTO(SensorSettings sensorSettings, TempMeasurement tempMeasurement) {
        MeasurementDTO temperatureMeasurementDTO = createTemperatureMeasurementDTO(sensorSettings, tempMeasurement.getValue());
        temperatureMeasurementDTO.setCreatedOn(tempMeasurement.getCreatedOn());
        return temperatureMeasurementDTO;
    }

    private MeasurementDTO createMeasurementDTOOutline(String sensorName, BigDecimal value) {
        ZonedDateTime now = ZonedDateTime.now();
        return MeasurementDTO.builder()
            .sensor(sensorName)
            .value(value)
            .createdOn(now)
            .build();
    }
}
