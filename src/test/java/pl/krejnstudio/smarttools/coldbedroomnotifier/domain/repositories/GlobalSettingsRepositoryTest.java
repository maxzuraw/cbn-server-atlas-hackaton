package pl.krejnstudio.smarttools.coldbedroomnotifier.domain.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import pl.krejnstudio.smarttools.coldbedroomnotifier.BaseMongoIntegrationTests;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.GlobalSettingsFormData;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.mappers.SensorsMapper;
import pl.krejnstudio.smarttools.coldbedroomnotifier.services.GlobalSettingsService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@EnableAutoConfiguration
@ContextConfiguration(classes = {GlobalSettingsRepository .class, SensorsMapper.class})
public class GlobalSettingsRepositoryTest extends BaseMongoIntegrationTests {

    @Autowired
    private GlobalSettingsRepository repository;

    @Autowired
    private SensorsMapper sensorsMapper;

    private GlobalSettingsService service;

    @Test
    public void itShouldUpdateSameGlobalSettings() {

        // Arrange
        service = new GlobalSettingsService(repository, sensorsMapper);
        long count = repository.count();
        assertThat(count, equalTo(0L));

        // Act
        GlobalSettingsFormData globalSettings = service.getGlobalSettings();

        // Assert

        count = repository.count();
        assertThat(count, equalTo(1L));
        assertThat(globalSettings, notNullValue());
        assertThat(globalSettings.getAlertsEnabled(), equalTo(Boolean.TRUE));
        assertThat(globalSettings.getSensorsInactivityMonitorEnabled(), equalTo(Boolean.FALSE));

        globalSettings.setAlertsEnabled(Boolean.FALSE);
        GlobalSettingsFormData afterUpdate = service.update(globalSettings);
        assertThat(afterUpdate.getAlertsEnabled(), equalTo(Boolean.FALSE));

        count = repository.count();
        assertThat(count, equalTo(1L));
    }
}
