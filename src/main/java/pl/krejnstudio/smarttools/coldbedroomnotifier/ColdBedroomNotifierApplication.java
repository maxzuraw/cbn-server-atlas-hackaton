package pl.krejnstudio.smarttools.coldbedroomnotifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ColdBedroomNotifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(ColdBedroomNotifierApplication.class, args);
	}

}
