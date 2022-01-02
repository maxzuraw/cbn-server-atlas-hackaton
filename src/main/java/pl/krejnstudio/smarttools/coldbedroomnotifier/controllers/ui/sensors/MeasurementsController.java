package pl.krejnstudio.smarttools.coldbedroomnotifier.controllers.ui.sensors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.TempMeasurement;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.TemperatureFormData;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.mappers.SensorsMapper;
import pl.krejnstudio.smarttools.coldbedroomnotifier.services.TemperatureService;
import pl.krejnstudio.smarttools.coldbedroomnotifier.utils.RangeTitleBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
@RequestMapping("/sensors/measurements")
public class MeasurementsController {

    private final TemperatureService service;
    private final SensorsMapper mapper;

    @Autowired
    public MeasurementsController(TemperatureService service, SensorsMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/{sensorName}")
    public String getMeasurements(@PathVariable("sensorName") String sensorName, Model model, @SortDefault.SortDefaults({@SortDefault(value="createdOn", direction = Sort.Direction.DESC)}) Pageable pageable) {
        Page<TempMeasurement> measurementsForSensor = service.getMeasurementsForSensor(sensorName, pageable);
        List<TemperatureFormData> measurements = measurementsForSensor.getContent().stream().map(t -> mapper.map(t, TemperatureFormData.class)).collect(Collectors.toList());
        model.addAttribute("measurementsPage", measurementsForSensor);
        model.addAttribute("measurements", measurements);
        model.addAttribute("sensorName", sensorName);
        model.addAttribute("currentPosition", String.join(" / ", String.valueOf(measurementsForSensor.getNumber() + 1), String.valueOf(measurementsForSensor.getTotalPages())));
        int totalPages = measurementsForSensor.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        if(measurementsForSensor.hasContent()) {
            List<TempMeasurement> content = measurementsForSensor.getContent();
            List<TempMeasurement> tempMeasurements = reverseList(content);
            model.addAttribute("rangeData", formatRangeData(tempMeasurements));
            model.addAttribute("chartData", getChartData(tempMeasurements));
        }
        model.addAttribute("sensorsMeasurementsTitle", String.join(" ", "Temperatures for sensor: ", sensorName));
        return "sensorMeasurements";
    }

    private String formatRangeData(List<TempMeasurement> tempMeasurements) {
        return RangeTitleBuilder.builder()
                .startDate(tempMeasurements.get(0).getCreatedOn())
                .endDate(tempMeasurements.get(tempMeasurements.size() - 1).getCreatedOn())
                .build().formatRangeData();
    }

    private List<List<Object>> getChartData(List<TempMeasurement> tempMeasurements) {
        if(CollectionUtils.isEmpty(tempMeasurements)) {
            return Collections.emptyList();
        }
        List<List<Object>> result = new ArrayList<>();
        result.add(List.of("Time", "°C"));
        tempMeasurements.forEach(m -> {
            result.add(List.of(m.getCreatedOn().format(RangeTitleBuilder.ONLY_TIME_FORMATTER), m.getValue()));
        });
        return result;
    }

    public static<T> List<T> reverseList(List<T> list)
    {
        List<T> reverse = new ArrayList<>(list);
        Collections.reverse(reverse);
        return reverse;
    }
}
