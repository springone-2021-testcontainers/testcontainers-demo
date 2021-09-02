package com.example.testcontainersdemo.messageboardservice;

import com.example.testcontainersdemo.Message;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.ToxiproxyContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
//@SpringBootTest(properties = "spring.sql.init.mode=always", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(properties = { "logging.level.org.flywaydb=debug",
        "spring.flyway.enabled=true",
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.flyway.locations=classpath:db/migration/,classpath:db/integration/"
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Demo2_BitnamiPostgresql_Tst {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static final String dbPlatform="org.hibernate.dialect.PostgreSQL95Dialect";
    private static final String username="test";
    private static final String password="test";
    private static final String databseName="test";

    private static final String TOXIPROXY_NETWORK_ALIAS = "toxiproxy";

    // Create a common docker network so that containers can communicate
    public static Network network = Network.newNetwork();


    static DockerImageName bitnamiPostgres = DockerImageName.parse("bitnami/postgresql:latest")
            .asCompatibleSubstituteFor("postgres");
    @Container
    static GenericContainer<?> db = new GenericContainer<>(bitnamiPostgres)
            .withEnv("POSTGRESQL_USERNAME", username)
            .withEnv("POSTGRESQL_PASSWORD", password)
            .withEnv("POSTGRESQL_DATABASE",databseName)
            .withExposedPorts(5432)
            .withNetwork(network);

    private static final DockerImageName TOXIPROXY_IMAGE = DockerImageName.parse("shopify/toxiproxy:2.1.0");
    // Toxiproxy container, which will be used as a TCP proxy

    @Container
    static ToxiproxyContainer toxiproxy = new ToxiproxyContainer(TOXIPROXY_IMAGE)
            .withNetwork(network)
            .withNetworkAliases(TOXIPROXY_NETWORK_ALIAS);

    static ToxiproxyContainer.ContainerProxy proxy;

    @DynamicPropertySource
    static void registerPostgresqlProperties(DynamicPropertyRegistry registry) {
        proxy = toxiproxy.getProxy(db, 5432);

        final String ipAddressViaToxiproxy = proxy.getContainerIpAddress();
        final int portViaToxiproxy = proxy.getProxyPort();
        registry.add("spring.jpa.database-platform", () -> dbPlatform);
        String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s",ipAddressViaToxiproxy,portViaToxiproxy, databseName);

        registry.add("spring.datasource.url", () -> jdbcUrl);
        registry.add("spring.datasource.username", () -> username);
        registry.add("spring.datasource.password", () -> password);
    }

    @Test
    void getMessagesSucceeds() {

        proxy.setConnectionCut(false);
        // Dynamically create type for List<Message> - required to properly
        // parse the response for a composite class in an HTTP exchange
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


    @Test
    void getMessagesSucceedsWithLatency() throws IOException {

        proxy.toxics()
                .latency("latency", ToxicDirection.DOWNSTREAM, 100)
                .setJitter(100);

        // Dynamically create type for List<Message> - required to properly
        // parse the response for a composite class in an HTTP exchange
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

    @Test
    void getMessagesWhenConnectionHasBeenCut() {

        proxy.setConnectionCut(true);
        // Connection Pool is already in place let's wait to make sure
        // that all open sockets are closed otherwise the timeout is minutes long!!
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
