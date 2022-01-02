package pl.krejnstudio.smarttools.coldbedroomnotifier.controllers.ui.sensors;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.SensorSettingsFormData;
import pl.krejnstudio.smarttools.coldbedroomnotifier.services.SensorSettingsService;

import javax.validation.Valid;

import static java.lang.String.join;

@Slf4j
@Controller
@RequestMapping("/admin/sensors/edit/{id}")
public class EditSensorSettingsController {

    private final SensorSettingsService service;

    @Autowired
    public EditSensorSettingsController(SensorSettingsService service) {
        this.service = service;
    }

    @GetMapping()
    public String getEditSensorSettingsPage(@PathVariable() String id, Model model) {
        if(StringUtils.isBlank(id)) {
            return "redirect:/sensors";
        }
        SensorSettingsFormData formData = service.findById(id);
        if(formData == null) {
            return "redirect:/sensors";
        }
        model.addAttribute("sensor", formData);
        String title = join(" ", "Editing sensor", formData.getSensorName());
        model.addAttribute("title", title);
        model.addAttribute("action", "Update");
        return "admin/editSensor";
    }

    @PostMapping()
    public String updateSensor(@Valid @ModelAttribute("sensor") SensorSettingsFormData formData, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "admin/editSensor";
        }
        service.createOrUpdate(formData);
        return "redirect:/sensors";
    }
}
