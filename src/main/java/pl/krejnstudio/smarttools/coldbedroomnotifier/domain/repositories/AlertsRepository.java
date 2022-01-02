package pl.krejnstudio.smarttools.coldbedroomnotifier.domain.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.Alert;

import java.util.List;

public interface AlertsRepository extends MongoRepository<Alert, String> {

    @Query("{ 'extinguished' : ?0}")
    List<Alert> findByExtinguishedSorted(Boolean extinguished, Sort sort);
}
