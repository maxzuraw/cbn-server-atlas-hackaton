package pl.krejnstudio.smarttools.coldbedroomnotifier.domain.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import pl.krejnstudio.smarttools.coldbedroomnotifier.BaseMongoIntegrationTests;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.SensorSettings;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@EnableAutoConfiguration
@ContextConfiguration(classes = {SensorSettingsRepository .class})
public class SensorSettingsRepositoryTest extends BaseMongoIntegrationTests {

    @Autowired
    private SensorSettingsRepository repository;

    @Test
    public void itShouldFindBedroomSensorSettings() {
        // given
        List<SensorSettings> settings = buildDummyData();
        settings.forEach(s -> {
            repository.save(s);
        });

        // when
        SensorSettings result = repository.findBySensorName("bedroom");

        // then
        assertThat(result, notNullValue());
        assertThat(result.getMaxValue().compareTo(BigDecimal.TEN), equalTo(0));
        assertThat(result.getMinValue().compareTo(BigDecimal.ONE), equalTo(0));
        assertThat(result.getAlertEnabled(), equalTo(true));

        // when
        result = repository.findBySensorName("bathroom");
        assertThat(result, notNullValue());
        assertThat(result.getMaxValue().compareTo(new BigDecimal("25.99")), equalTo(0));
        assertThat(result.getMinValue().compareTo(BigDecimal.ZERO), equalTo(0));
        assertThat(result.getAlertEnabled(), nullValue());
    }

    @Test
    public void itShouldNotFindUnknownSensorSettings() {
        // given & when
        SensorSettings result = repository.findBySensorName("łazienka");

        // then
        assertThat(result, nullValue());
    }

    @Test
    public void itShouldNotFindBedroomWithoutInitiailzation() {
        // given & when
        SensorSettings result = repository.findBySensorName("bedroom");

        // then
        assertThat(result, nullValue());
    }

    @Test
    public void itShouldOnlyGrabNamesOfTheSensors() {

        // Arrange

        List<SensorSettings> sensors = buildDummyData();
        sensors.forEach(s -> {
            repository.save(s);
        });

        // Act

        List<SensorSettings> result = repository.findSensorExcludeOthers();

        // Assert

        assertThat(result.size(), equalTo(2));
        assertThat(result.get(0).getMaxValue(), nullValue());
        assertThat(result.get(0).getMinValue(), nullValue());
        assertThat(result.get(0).getAlertEnabled(), nullValue());

        List<String> sensorNames = result.stream().map(SensorSettings::getSensorName).collect(Collectors.toList());
        assertThat(sensorNames.containsAll(List.of("bedroom", "bathroom")), equalTo(true));
    }

    @AfterEach
    public void afterEach() {
        repository.deleteAll();
    }

    private List<SensorSettings> buildDummyData() {
        return List.of(
            SensorSettings.builder().sensorName("bedroom").maxValue(BigDecimal.TEN).minValue(BigDecimal.ONE).alertEnabled(true).build(),
            SensorSettings.builder().sensorName("bathroom").maxValue(new BigDecimal("25.99")).minValue(BigDecimal.ZERO).build()
            );
    }
}
