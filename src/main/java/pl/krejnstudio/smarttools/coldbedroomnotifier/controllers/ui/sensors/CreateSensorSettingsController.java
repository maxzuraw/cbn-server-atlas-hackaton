package pl.krejnstudio.smarttools.coldbedroomnotifier.controllers.ui.sensors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.SensorSettingsFormData;
import pl.krejnstudio.smarttools.coldbedroomnotifier.services.SensorSettingsService;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/admin/sensors/create")
public class CreateSensorSettingsController {

    private final SensorSettingsService service;

    @Autowired
    public CreateSensorSettingsController(SensorSettingsService service) {
        this.service = service;
    }

    @GetMapping()
    public String prepareSensorSettingsFormForCreation(Model model) {
        model.addAttribute("sensor", new SensorSettingsFormData());
        model.addAttribute("action", "Save");
        model.addAttribute("title", "New sensor");
        return "admin/editSensor";
    }

    @PostMapping()
    public String createSensorSettings(@Valid @ModelAttribute("sensor") SensorSettingsFormData formData, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "admin/editSensor";
        }
        service.createOrUpdate(formData);
        return "redirect:/sensors";
    }
}
