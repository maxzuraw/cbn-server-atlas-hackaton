package pl.krejnstudio.smarttools.coldbedroomnotifier.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.GlobalSettings;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.GlobalSettingsFormData;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.mappers.SensorsMapper;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.repositories.GlobalSettingsRepository;

import java.time.ZonedDateTime;

@Service
public class GlobalSettingsService {

    private final GlobalSettingsRepository repository;
    private final SensorsMapper sensorsMapper;

    @Autowired
    public GlobalSettingsService(GlobalSettingsRepository repository, SensorsMapper sensorsMapper) {
        this.repository = repository;
        this.sensorsMapper = sensorsMapper;
    }

    public GlobalSettingsFormData getGlobalSettings() {
        long count = repository.count();
        if(count == 0) {
            // NOTE: create one if not exists yet
            GlobalSettings globalSettings = GlobalSettings.builder().alertsEnabled(Boolean.TRUE).sensorsInactivityMonitorEnabled(Boolean.FALSE).build();
            return sensorsMapper.map(repository.insert(globalSettings), GlobalSettingsFormData.class);
        }
        GlobalSettings globalSettings = repository.findAll().get(0);
        return sensorsMapper.map(globalSettings, GlobalSettingsFormData.class);
    }

    public GlobalSettingsFormData update(GlobalSettingsFormData formData) {
        GlobalSettings globalSettings = repository.findAll().get(0);
        sensorsMapper.map(formData, globalSettings);
        globalSettings.setLastModifiedOn(ZonedDateTime.now());
        return sensorsMapper.map(repository.save(globalSettings), GlobalSettingsFormData.class);
    }
}
