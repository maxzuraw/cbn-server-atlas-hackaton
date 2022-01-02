package pl.krejnstudio.smarttools.coldbedroomnotifier.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.TempMeasurement;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.dto.TemperatureData;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.repositories.TempMeasurementRepository;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class TemperatureService {

    private final TempMeasurementRepository temperatureRepository;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public TemperatureService(TempMeasurementRepository temperatureRepository, MongoTemplate mongoTemplate) {
        this.temperatureRepository = temperatureRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void insertTemperature(TemperatureData temperatureData) {
        TempMeasurement entity = TempMeasurement.builder()
                .sensor(temperatureData.getSensor())
                .value(temperatureData.getValue())
                .createdOn(ZonedDateTime.now()).build();
        temperatureRepository.insert(entity);
    }

    public Page<TempMeasurement> getMeasurementsForSensor(String sensorName, Pageable pageable) {
        return temperatureRepository.findBySensorName(sensorName, pageable);
    }

    public TempMeasurement findNewestForSensor(String sensorName) {
        return  temperatureRepository.findTopBySensorOrderByCreatedOnDesc(sensorName);
    }

    public List<TempMeasurement> findLastNMeasurementsForSensor(String sensorName, int elementsCount) {
        PageRequest request = PageRequest.of(0, elementsCount, Sort.by(Sort.Direction.DESC, "createdOn"));
        Page<TempMeasurement> page = temperatureRepository.findBySensorName(sensorName, request);
        if(!page.hasContent()) {
            return Collections.emptyList();
        }
        return page.getContent();
    }

    public void deleteAllBySensorName(String sensorName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("sensor").is(sensorName));
        mongoTemplate.remove(query, TempMeasurement.class);
    }
}
