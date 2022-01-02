package pl.krejnstudio.smarttools.coldbedroomnotifier.controllers.ui.sensors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.SensorSettings;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.SensorSettingsFormData;
import pl.krejnstudio.smarttools.coldbedroomnotifier.services.SensorSettingsService;

import java.util.List;

@Slf4j
@Controller
public class SensorsSettingsController {

    private final SensorSettingsService service;

    @Autowired
    public SensorsSettingsController(SensorSettingsService service) {
        this.service = service;
    }

    @GetMapping("/sensors")
    public String getAdminSensorsPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String title = "Known sensors";
        if(auth != null && auth.getAuthorities().stream().anyMatch(e -> e.getAuthority().equals("ROLE_ADMIN"))) {
            title = "Known sensors (Admin mode)";
        }
        List<SensorSettings> sensorSettings = service.findAllSensorSettings();
        model.addAttribute("sensorsSettings", sensorSettings);
        model.addAttribute("sensorsPageTitle", title);
        return "sensors";
    }

    @GetMapping("/admin/sensors/delete/{id}")
    public String prepareDeleteSensorPage(@PathVariable("id") String id, Model model) {
        SensorSettingsFormData formData = service.findById(id);
        model.addAttribute("sensor", formData);
        model.addAttribute("confirmation", "Do you really want to delete sensor: " + formData.getSensorName());
        model.addAttribute("title", "Deleting sensor?");
        return "admin/sensorsDeleteConfirmation";
    }

    @PostMapping("/admin/sensors/delete/{id}")
    public String deleteSensor(@PathVariable("id") String id) {
        service.deleteSensorSettings(id);
        return "redirect:/sensors";
    }
}
