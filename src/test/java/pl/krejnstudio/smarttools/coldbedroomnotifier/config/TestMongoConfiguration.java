package pl.krejnstudio.smarttools.coldbedroomnotifier.config;

import static com.google.common.collect.Lists.newArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import java.time.ZonedDateTime;
import java.util.Optional;

import pl.krejnstudio.smarttools.coldbedroomnotifier.config.mongo.DateToZonedDateTimeConverter;
import pl.krejnstudio.smarttools.coldbedroomnotifier.config.mongo.ZonedDateTimeToDateConverter;

@Configuration
public class TestMongoConfiguration {

    public @Bean
    MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(newArrayList(new DateToZonedDateTimeConverter(), new ZonedDateTimeToDateConverter()));
    }

    public @Bean
    DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }
}
