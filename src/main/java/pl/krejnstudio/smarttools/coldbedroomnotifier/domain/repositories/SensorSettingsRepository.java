package pl.krejnstudio.smarttools.coldbedroomnotifier.domain.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.SensorSettings;

import java.util.List;

public interface SensorSettingsRepository extends MongoRepository<SensorSettings, String> {

    SensorSettings findBySensorName(String sensorName);

    List<SensorSettings> findAll();

    SensorSettings findBy_id(String id);

    @Query(value="{}", fields="{sensorName: 1, _id: 0}")
    List<SensorSettings> findSensorExcludeOthers();
}
