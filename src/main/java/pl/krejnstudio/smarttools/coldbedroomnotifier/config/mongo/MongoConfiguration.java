package pl.krejnstudio.smarttools.coldbedroomnotifier.config.mongo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.time.ZonedDateTime;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

@EnableMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
@Configuration
public class MongoConfiguration {

    @Value("${spring.data.mongodb.app-name}")
    private String mongoClientAppName;

    public @Bean MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(newArrayList(new DateToZonedDateTimeConverter(), new ZonedDateTimeToDateConverter()));
    }

    public @Bean DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }

    public @Bean MongoClientSettingsBuilderCustomizer propertiesCustomizer() {
        return (builder) -> {
            builder.applicationName(mongoClientAppName);
        };
    }
}
