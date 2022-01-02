package pl.krejnstudio.smarttools.coldbedroomnotifier.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CbnTestConfiguration {

    static {
        System.setProperty("REGULAR_USER", "user");
        System.setProperty("REGULAR_USER_PASSWORD", "user");
        System.setProperty("SENSOR_USER", "sensor");
        System.setProperty("SENSOR_USER_PASSWORD", "sensor");
        System.setProperty("ADMIN_USER", "admin");
        System.setProperty("ADMIN_USER_PASSWORD", "admin");
    }
}
