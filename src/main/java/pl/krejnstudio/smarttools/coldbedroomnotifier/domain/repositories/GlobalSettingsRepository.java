package pl.krejnstudio.smarttools.coldbedroomnotifier.domain.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.krejnstudio.smarttools.coldbedroomnotifier.domain.GlobalSettings;

public interface GlobalSettingsRepository extends MongoRepository<GlobalSettings, String> {
}
