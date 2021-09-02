package com.example.testcontainersdemo.messageboardservice;

import com.example.testcontainersdemo.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
//@SpringBootTest(properties = "spring.sql.init.mode=always", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.flyway.enabled=true",
                "spring.flyway.locations=classpath:db/migration,classpath:db/demo1",
                "logging.level.org.flywaydb=debug",
                "spring.jpa.hibernate.ddl-auto=none",
                "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL95Dialect",
                "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQL95Dialect"
        })
public class Demo1B_BitnamiPostgres_Integration_Tst {

    @Autowired
    private TestRestTemplate testRestTemplate;

//    @Container
//    static PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres");

    static DockerImageName bitnamiPostgres = DockerImageName.parse("bitnami/postgresql:latest")
            .asCompatibleSubstituteFor("postgres");
    @Container
    static GenericContainer<?> db = new GenericContainer<>(bitnamiPostgres)
            .withEnv("POSTGRESQL_USERNAME", "test")
            .withEnv("POSTGRESQL_PASSWORD", "test")
            .withEnv("POSTGRESQL_DATABASE","test")
            .withExposedPorts(5432);

    @Value("${spring.jpa.database-platform}")
    static String dbPlatform;

    @DynamicPropertySource
    static void registerPostgresqlProperties(DynamicPropertyRegistry registry) {
        String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s",db.getHost(),db.getFirstMappedPort(), "test");
        registry.add("spring.jpa.database-platform", () -> dbPlatform);
        registry.add("spring.datasource.url", () -> jdbcUrl);
        registry.add("spring.datasource.username", () -> "test");
        registry.add("spring.datasource.password", () -> "test");
    }

    @Test
    void getMessagesSucceeds() {

        var ptr = new ParameterizedTypeReference<List<Message>>() {
        };
        var exchange =
                this.testRestTemplate.exchange("/message", HttpMethod.GET, null, ptr);

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Message> messageList = exchange.getBody();
        Message message = messageList.get(0);
        assertThat(message.getUsername()).isEqualTo("Dani_Rojas");
        assertThat(message.getText()).isEqualTo("Football is life!");
    }

}
