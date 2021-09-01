package com.example.testcontainersdemo.tcbasics;

import com.example.testcontainersdemo.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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
@SpringBootTest
public class Demo4_DockerfileTest {

    RestTemplate restTemplate = new RestTemplate();

    // ImageFromDockerfile is a Future - Docker image will be created asynchronously
    // Testcontainers waits up to 60 seconds for first mapped port to start listening
    // Can also use waiting strategies to make sure container is ready
    // e.g. based on HTTP response, or certain log output, etc...
    @Container
    public GenericContainer container = new GenericContainer(
//            new ImageFromDockerfile()
            new ImageFromDockerfile("testcontainers/hello-server", false)
                    .withFileFromPath(".", Constants.HELLO_SERVER_SOURCE)
                    .withBuildArg("SOME_KEY", "some-value"))
            .withExposedPorts(8080)
            .waitingFor(Wait.forHttp("/"));

    public Demo4_DockerfileTest() {

        log.info("{} In Constructor. Instance: {}\n", Constants.EYE_CATCHER, this);
    }

    @BeforeAll
    public static void beforeAllMethod() {

        log.info("{} In @BeforeAll. Instance: {}\n", Constants.EYE_CATCHER, "<static method>");

        log.info("Source path for docker build: {}", Constants.HELLO_SERVER_SOURCE.toAbsolutePath());
    }

    @AfterAll
    public static void afterAllMethod() {

        log.info("{} In @AfterAll. Instance: {}\n", Constants.EYE_CATCHER, "<static method>");
    }

    @Test
    public void test1() {

        log.info("{} In @Test 1. Instance: {}\n", Constants.EYE_CATCHER, this);

        String url = "http://" + container.getHost() + ":" + container.getFirstMappedPort();
        log.info("URL: {}", url);
        ResponseEntity<String> response
                = restTemplate.getForEntity(url + "/", String.class);
        log.info("Response is:\n\n{}\n", response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK, () -> "This is not okay.");
    }

    @Disabled
    @Test
    public void test2() {

        log.info("{} In @Test 2. Instance: {}\n", Constants.EYE_CATCHER, this);
    }

    @BeforeEach
    public void beforeEachMethod() {

        log.info("{} In @BeforeEach. Instance: {}\n", Constants.EYE_CATCHER, this);
    }

    @AfterEach
    public void afterEachMethod() {

        log.info("{} In @AfterEach. Instance: {}\n", Constants.EYE_CATCHER, this);
    }
}

