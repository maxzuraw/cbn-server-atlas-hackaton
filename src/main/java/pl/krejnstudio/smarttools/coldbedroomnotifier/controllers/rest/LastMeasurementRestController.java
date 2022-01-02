package pl.krejnstudio.smarttools.coldbedroomnotifier.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.PathParam;
import java.util.Collections;
import java.util.List;

import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.MeasurementDTO;
import pl.krejnstudio.smarttools.coldbedroomnotifier.services.LastMeasurementsService;

@RestController
@RequestMapping("/api/lastMeasurements")
public class LastMeasurementRestController {

    private final LastMeasurementsService service;

    @Autowired
    public LastMeasurementRestController(LastMeasurementsService service) {
        this.service = service;
    }

    @GetMapping()
    public List<MeasurementDTO> findLastMeasurements() {
        return service.findLastMeasurements();
    }

    @GetMapping("/{sensorName}")
    public List<MeasurementDTO> findLast10MeasurementsForSensor(@PathVariable("sensorName") String sensorName) {
        List<MeasurementDTO> lastNMeasurementsForSensor = service.findLastNMeasurementsForSensor(sensorName, 10);
        Collections.reverse(lastNMeasurementsForSensor);
        return lastNMeasurementsForSensor;
    }
}
