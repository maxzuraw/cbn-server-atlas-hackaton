package pl.krejnstudio.smarttools.coldbedroomnotifier;

import org.junit.Before;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import lombok.extern.slf4j.Slf4j;
import pl.krejnstudio.smarttools.coldbedroomnotifier.config.CbnTestConfiguration;
import pl.krejnstudio.smarttools.coldbedroomnotifier.config.TestMongoConfiguration;

@Slf4j
@Testcontainers
@Import({ TestMongoConfiguration.class, CbnTestConfiguration.class })
@SpringBootTest
public abstract class BaseMongoIntegrationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:5.0.0-focal"));

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Before
    public void before(){
        if(!mongoDBContainer.isRunning()) {
            mongoDBContainer.start();
        }
    }

}
