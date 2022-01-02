package pl.krejnstudio.smarttools.coldbedroomnotifier.domain.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import pl.krejnstudio.smarttools.coldbedroomnotifier.BaseMongoIntegrationTests;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.TempMeasurement;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@EnableAutoConfiguration
@ContextConfiguration(classes = {TempMeasurementRepository .class})
public class TempMeasurementRepositoryTest extends BaseMongoIntegrationTests {

    private static final String BEDROOM_SENSOR = "bedroom";
    private static final String LIVINGROOM_SENSOR = "livingroom";

    @Autowired
    private TempMeasurementRepository repository;

    @Test
    public void itShouldPageOnlyOverBedroomMeasurements() throws InterruptedException {
        // given
        saveTestData(60, LIVINGROOM_SENSOR);
        saveTestData(8, BEDROOM_SENSOR);
        Sort sort = Sort.by(Sort.Direction.ASC, "createdOn");

        // when

        Page<TempMeasurement> results = repository.findBySensorName(BEDROOM_SENSOR, PageRequest.of(0, 5, sort));

        // then

        assertThat(results.getContent().size(), equalTo(5));
        Set<String> sensorNames = results.stream().map(TempMeasurement::getSensor).collect(Collectors.toSet());
        assertThat(sensorNames.size(), equalTo(1));
        assertThat(sensorNames.contains(BEDROOM_SENSOR), equalTo(true));

        // when
        results = repository.findBySensorName(BEDROOM_SENSOR, PageRequest.of(1, 5, sort));
        assertThat(results.getContent().size(), equalTo(3));
        sensorNames = results.stream().map(TempMeasurement::getSensor).collect(Collectors.toSet());
        assertThat(sensorNames.size(), equalTo(1));
        assertThat(sensorNames.contains(BEDROOM_SENSOR), equalTo(true));
    }

    @Test
    public void itShouldPageOverTemperatureMeasurements() throws InterruptedException {
        // given
        saveTestData(22, BEDROOM_SENSOR);
        Sort sort = Sort.by(Sort.Direction.ASC, "createdOn");

        // when

        Page<TempMeasurement> results = repository.findAll(PageRequest.of(0, 5, sort));

        // then
        assertThat(results.getContent().size(), equalTo(5));
        List<BigDecimal> values = results.getContent().stream().map(TempMeasurement::getValue).collect(Collectors.toList());
        assertThat(values.get(0).compareTo(BigDecimal.TEN), equalTo(0));
        assertThat(values.get(1).compareTo(new BigDecimal("10.01")), equalTo(0));
        assertThat(values.get(2).compareTo(new BigDecimal("10.02")), equalTo(0));
        assertThat(values.get(3).compareTo(new BigDecimal("10.03")), equalTo(0));
        assertThat(values.get(4).compareTo(new BigDecimal("10.04")), equalTo(0));


        // when
        results = repository.findAll(PageRequest.of(1, 5, sort));

        // then
        assertThat(results.getContent().size(), equalTo(5));
        values = results.getContent().stream().map(TempMeasurement::getValue).collect(Collectors.toList());
        assertThat(values.get(0).compareTo(new BigDecimal("10.05")), equalTo(0));
        assertThat(values.get(1).compareTo(new BigDecimal("10.06")), equalTo(0));
        assertThat(values.get(2).compareTo(new BigDecimal("10.07")), equalTo(0));
        assertThat(values.get(3).compareTo(new BigDecimal("10.08")), equalTo(0));
        assertThat(values.get(4).compareTo(new BigDecimal("10.09")), equalTo(0));

        // when
        results = repository.findAll(PageRequest.of(2, 5, sort));

        // then
        assertThat(results.getContent().size(), equalTo(5));
        values = results.getContent().stream().map(TempMeasurement::getValue).collect(Collectors.toList());
        assertThat(values.get(0).compareTo(new BigDecimal("10.10")), equalTo(0));
        assertThat(values.get(1).compareTo(new BigDecimal("10.11")), equalTo(0));
        assertThat(values.get(2).compareTo(new BigDecimal("10.12")), equalTo(0));
        assertThat(values.get(3).compareTo(new BigDecimal("10.13")), equalTo(0));
        assertThat(values.get(4).compareTo(new BigDecimal("10.14")), equalTo(0));

        // when
        results = repository.findAll(PageRequest.of(3, 5, sort));

        // then
        assertThat(results.getContent().size(), equalTo(5));
        values = results.getContent().stream().map(TempMeasurement::getValue).collect(Collectors.toList());
        assertThat(values.get(0).compareTo(new BigDecimal("10.15")), equalTo(0));
        assertThat(values.get(1).compareTo(new BigDecimal("10.16")), equalTo(0));
        assertThat(values.get(2).compareTo(new BigDecimal("10.17")), equalTo(0));
        assertThat(values.get(3).compareTo(new BigDecimal("10.18")), equalTo(0));
        assertThat(values.get(4).compareTo(new BigDecimal("10.19")), equalTo(0));

        // when
        results = repository.findAll(PageRequest.of(4, 5, sort));

        // then
        assertThat(results.getContent().size(), equalTo(2));
        values = results.getContent().stream().map(TempMeasurement::getValue).collect(Collectors.toList());
        assertThat(values.get(0).compareTo(new BigDecimal("10.20")), equalTo(0));
        assertThat(values.get(1).compareTo(new BigDecimal("10.21")), equalTo(0));


    }

    @Test
    public void itShouldFindNewestForBedroomSensor() throws InterruptedException {

        // Arrange

        saveTestData(10, "bedroom");
        saveTestData(1000, "childrensroom");

        // Act

        TempMeasurement bedroomLastEntry = repository.findTopBySensorOrderByCreatedOnDesc("bedroom");
        TempMeasurement childrensroomLastEntry = repository.findTopBySensorOrderByCreatedOnDesc("childrensroom");

        // Assert

        assertThat(childrensroomLastEntry.getCreatedOn().isAfter(bedroomLastEntry.getCreatedOn()), equalTo(true));
    }

    @AfterEach
    public void after() {
        repository.deleteAll();
    }

    private void saveTestData(int amount, String sensorName) throws InterruptedException {
        BigDecimal temperature = BigDecimal.TEN;
        for(int i=0; i < amount; i++) {
            TempMeasurement tempMeasurement = TempMeasurement.builder().value(temperature).sensor(sensorName).createdOn(ZonedDateTime.now()).build();
            temperature = temperature.add(new BigDecimal("0.0" + 1));
            repository.save(tempMeasurement);
            Thread.sleep(1);
        }
    }
}
