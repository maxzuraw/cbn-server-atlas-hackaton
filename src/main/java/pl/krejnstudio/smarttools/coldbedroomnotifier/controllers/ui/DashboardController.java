package pl.krejnstudio.smarttools.coldbedroomnotifier.controllers.ui;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.SensorSettings;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.TempMeasurement;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.DashboardData;
import pl.krejnstudio.smarttools.coldbedroomnotifier.services.SensorSettingsService;
import pl.krejnstudio.smarttools.coldbedroomnotifier.services.TemperatureService;
import pl.krejnstudio.smarttools.coldbedroomnotifier.utils.RangeTitleBuilder;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.krejnstudio.smarttools.coldbedroomnotifier.utils.RangeTitleBuilder.DATE_AND_TIME_FORMATTER;

@Controller
@RequestMapping("/dashboard")
@Slf4j
public class DashboardController {

    private final SensorSettingsService sensorSettingsService;
    private final TemperatureService temperatureService;

    @Autowired
    public DashboardController(SensorSettingsService sensorSettingsService, TemperatureService temperatureService) {
        this.sensorSettingsService = sensorSettingsService;
        this.temperatureService = temperatureService;
    }

    @GetMapping()
    public String dashboard(Model model) {
        List<SensorSettings> allSensors = sensorSettingsService.findAllSensorSettings();
        List<String> sensorNames = allSensors.stream().map(SensorSettings::getSensorName).collect(Collectors.toList());
        List<DashboardData> dashboardData = new ArrayList<>();
        allSensors.forEach(s -> {
            TempMeasurement newestForSensor = temperatureService.findNewestForSensor(s.getSensorName());

            if(newestForSensor == null || newestForSensor.getCreatedOn() == null || newestForSensor.getValue() == null) {
                return;
            }

            List<TempMeasurement> lastNMeasurementsForSensor = temperatureService.findLastNMeasurementsForSensor(s.getSensorName(), 10);

            dashboardData.add(DashboardData.builder()
                            .sensorName(s.getSensorName())
                            .createdOn(newestForSensor.getCreatedOn())
                            .lastMeasurement(newestForSensor.getValue())
                            .maxValue(s.getMaxValue())
                            .minValue(s.getMinValue())
                            .ownChartTitle(getOwnChartTitle(s.getSensorName(), lastNMeasurementsForSensor))
                            .ownChartId(String.join("_", "sensor_line_chart_", s.getSensorName()))
                            .ownChartElements(getSpecificChartData(s.getSensorName(), lastNMeasurementsForSensor))
                            .build());
        });

        if(CollectionUtils.isEmpty(dashboardData)) {
            return "redirect:/sensors";
        }

        model.addAttribute("mainChartSubtitle", getSubtitle(dashboardData));
        model.addAttribute("mainChartData", getChartData(dashboardData));
        model.addAttribute("sensors", sensorNames);
        model.addAttribute("dashboardData", dashboardData);
        model.addAttribute("pageTitle", "Dashboard");
        return "dashboard";
    }

    private String getSubtitle(List<DashboardData> dashboardData) {
        List<ZonedDateTime> createdOnDates = dashboardData.stream()
                .map(DashboardData::getCreatedOn)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());

        List<String> dates = createdOnDates.stream().map(d -> d.format(RangeTitleBuilder.ONLY_DATE_FORMATTER)).collect(Collectors.toList());
        if(dates.size() > 1) {
            return String.join(" ", "Dates range:", String.join(" - ", dates.get(0), dates.get(dates.size() - 1)));
        }

        return String.join(" ", "Date:", dates.iterator().next());
    }

    private String getOwnChartTitle(String sensorName, List<TempMeasurement> lastNMeasurementsForSensor) {
        List<TempMeasurement> sorted = lastNMeasurementsForSensor.stream().sorted(Comparator.comparing(TempMeasurement::getCreatedOn)).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        sb.append(sensorName);
        sb.append(" ( ");
        sb.append(sorted.get(0).getCreatedOn().format(DATE_AND_TIME_FORMATTER));
        sb.append(" - ");
        sb.append(sorted.get(sorted.size() - 1).getCreatedOn().format(DATE_AND_TIME_FORMATTER));
        sb.append(")");
        return sb.toString();
    }

    private List<List<Object>> getSpecificChartData(String sensorName, List<TempMeasurement> lastNMeasurementsForSensor) {
        List<TempMeasurement> sorted = lastNMeasurementsForSensor.stream().sorted(Comparator.comparing(TempMeasurement::getCreatedOn)).collect(Collectors.toList());
        List<List<Object>> result = new ArrayList<>();
        result.add(List.of("Time", "°C"));
        sorted.forEach(t -> {
            result.add(List.of(t.getCreatedOn().format(RangeTitleBuilder.ONLY_TIME_FORMATTER), t.getValue()));
        });
        return result;
    }

    private List<List<Object>> getChartData(List<DashboardData> dashboardData) {
        if(CollectionUtils.isEmpty(dashboardData)) {
            return Collections.emptyList();
        }
        List<List<Object>> result = new ArrayList<>();
        Map<String, String> roleMap = new HashMap<>();
        roleMap.put("role", "style");
        result.add(List.of("Sensor ( HH:mm )", "°C", roleMap));
        dashboardData.forEach(d -> {
            result.add(List.of(
                    String.join(" ", d.getSensorName(), "\n(", d.getCreatedOn().format(DATE_AND_TIME_FORMATTER), ")"),
                    d.getLastMeasurement(), getColorForDashboardData(d)));
        });
        return result;
    }

    private String getColorForDashboardData(DashboardData dashboardData) {
        if(dashboardData.getLastMeasurement().compareTo(dashboardData.getMinValue()) < 0) {
            return "color: lightblue";
        }
        if(dashboardData.getLastMeasurement().compareTo(dashboardData.getMaxValue()) > 0) {
            return "color: red";
        }
        return "color: green";
    }

}
