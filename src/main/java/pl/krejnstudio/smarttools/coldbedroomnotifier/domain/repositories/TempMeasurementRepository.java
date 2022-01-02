package pl.krejnstudio.smarttools.coldbedroomnotifier.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.TempMeasurement;

public interface TempMeasurementRepository extends MongoRepository<TempMeasurement, String> {

    @Query(value="{'sensor': ?0}")
    Page<TempMeasurement> findBySensorName(String sensor, Pageable pageable);

    TempMeasurement findTopBySensorOrderByCreatedOnDesc(String sensor);
}
