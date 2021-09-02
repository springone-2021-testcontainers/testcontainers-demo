package com.example.testcontainersdemo.messageboardservice;

import com.example.testcontainersdemo.Message;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import org.junit.jupiter.api.Order;
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
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.ToxiproxyContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
//@SpringBootTest(properties = "spring.sql.init.mode=always", webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(properties = { "logging.level.org.flywaydb=debug",
        "spring.flyway.enabled=true",
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.flyway.locations=classpath:db/migration/,classpath:db/integration/"
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class Demo2_Toxiproxy_Tst {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Value("${spring.jpa.database-platform}")
    static String dbPlatform;

    // Create a common docker network so that containers can communicate
    public static Network network = Network.newNetwork();

    @Container
    static PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres")
            .withExposedPorts(5432)
            .withNetwork(network);

    private static final String TOXIPROXY_NETWORK_ALIAS = "toxiproxy";
    private static final DockerImageName TOXIPROXY_IMAGE = DockerImageName.parse("shopify/toxiproxy:2.1.0");

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
        final String[] split = db.getJdbcUrl().split("/");
        final String database = split[split.length - 1];
        registry.add("spring.jpa.database-platform", () -> dbPlatform);

        String jdbcurl = String.format("jdbc:postgresql://%s:%d/%s", ipAddressViaToxiproxy, portViaToxiproxy, database);
        registry.add("spring.datasource.url", () -> jdbcurl);
        registry.add("spring.datasource.username", () -> db.getUsername());
        registry.add("spring.datasource.password", () -> db.getPassword());
    }

    @Test
    @Order(1)
    void getMessagesSucceeds() {

        proxy.setConnectionCut(false);
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
    @Order(2)
    void getMessagesSucceedsWithLatency() throws IOException {

        proxy.toxics()
                .latency("latency", ToxicDirection.DOWNSTREAM, 1000);

        var ptr = new ParameterizedTypeReference<List<Message>>() {
        };

        Instant start = Instant.now();
        var exchange =
                this.testRestTemplate.exchange("/message", HttpMethod.GET, null, ptr);

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Message> messageList = exchange.getBody();
        Message message = messageList.get(0);
        assertThat(message.getUsername()).isEqualTo("Dani_Rojas");
        assertThat(message.getText()).isEqualTo("Football is life!");
    }

    @Test
    @Order(3)
    void getMessagesWhenConnectionHasBeenCut() {
        // Connection Pool is already in place let's wait to make sure
        // that all open sockets are closed otherwise the timeout is minutes long!!
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        proxy.setConnectionCut(true);
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