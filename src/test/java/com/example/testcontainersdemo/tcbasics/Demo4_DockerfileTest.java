package com.example.testcontainersdemo.tcbasics;

import com.example.testcontainersdemo.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// This test dynamically builds a container from a Dockerfile.
// This is useful, for example, if the system under test is the image itself,
// or when the image must be configured differently for different tests.
// The source an Dockerfile can also be read from the classpath, String, or
// assembled using a Docker DSL.

// This test also shows that the dynamically built container can be persisted
// by providing a name and a 'false' boolean to "new ImageFromDockerfile(...)"
// This makes subsequent tests faster.

@Testcontainers
@Slf4j
public class Demo4_DockerfileTest {

    RestTemplate restTemplate = new RestTemplate();

    // ImageFromDockerfile is a Future - Docker image will be created asynchronously
    // Testcontainers waits up to 60 seconds for first mapped port to start listening
    // Can also use waiting strategies to make sure container is ready
    // e.g. based on HTTP response, or certain log output, etc...
    @Container
    public static GenericContainer container = new GenericContainer(
//            new ImageFromDockerfile()
            new ImageFromDockerfile("testcontainers/helloworld", false)
                    .withFileFromPath(".", Constants.HELLOWORLD_SOURCE)
                    .withBuildArg("SOME_BUILD_VAR", "some-value"))
            .withEnv("DELAY_START_MSEC", "2000")
            .withExposedPorts(8080, 8081)
            .waitingFor(Wait.forHttp("/"));

    @Test
    @DisplayName("get_ping_from_port1")
    public void test1() {

        String url = "http://" + container.getHost()
                + ":"
                + container.getFirstMappedPort()
                + "/ping";
        log.info("Request: GET {}", url);
        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class);
        log.info("Response: {}", response.getBody());
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK, () -> "This is not okay.");
    }

    @Test
    @DisplayName("get_uuid_from_port2")
    public void test2() {

        String url = "http://" + container.getHost()
                + ":"
                + container.getMappedPort(8081)
                + "/uuid";
        log.info("Request: GET {}", url);
        ResponseEntity<String> response
                = restTemplate.getForEntity(url, String.class);
        log.info("Response: {}", response.getBody());
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK, () -> "This is not okay.");
    }

    @BeforeEach
    public void beforeEachMethod(TestInfo testInfo) {

        log.info("{}Test: {}\nClass instance: {}\nContainer id: {}\n", Constants.EYE_CATCHER, testInfo.getDisplayName(), this, container.getContainerId());
    }
}
