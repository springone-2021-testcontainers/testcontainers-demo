package com.example.testcontainersdemo.tcbasics;

import com.example.testcontainersdemo.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Slf4j
@SpringBootTest
public class DemoY_ContainerLoggingStreamTest {

    RestTemplate restTemplate = new RestTemplate();

    @Container
    GenericContainer container = new GenericContainer(Constants.HTTPBIN_IMAGE)
            .withExposedPorts(80);

    public DemoY_ContainerLoggingStreamTest() {

        log.info("{} In Constructor\nClass instance: {}\n", Constants.EYE_CATCHER, this);
    }

    @BeforeAll
    public static void beforeAllMethod() {

        log.info("{}In @BeforeAll\nStatic method\n", Constants.EYE_CATCHER);
    }

    @AfterAll
    public static void afterAllMethod() {

        log.info("{}In @AfterAll\nStatic method\n", Constants.EYE_CATCHER);
    }

    @Test
    public void test1() {

        log.info("{}In @Test 1\nClass instance: {}\n", Constants.EYE_CATCHER, this);

        String url = "http://" + container.getHost() + ":" + container.getFirstMappedPort();
        ResponseEntity<String> response
                = restTemplate.getForEntity(url + "/uuid", String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK, () -> "This is not okay.");
    }

    @Disabled
    @Test
    public void test2() {

        log.info("{}In @Test 2\nClass instance: {}\n", Constants.EYE_CATCHER, this);

        String url = "http://" + container.getHost() + ":" + container.getFirstMappedPort();
        log.info("URL: {}", url);
        ResponseEntity<String> response
                = restTemplate.getForEntity(url + "/uuid", String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK, () -> "This is not okay.");
    }

    @BeforeEach
    public void beforeEachMethod() {

        log.info("{}In @BeforeEach\nClass instance: {}\nContainer id: {}\n", Constants.EYE_CATCHER, this, container.getContainerId());

        // Create seperate SLF4J logger instance to pass to the consumer?
        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log).withSeparateOutputStreams();
        container.followOutput(logConsumer);
    }

    @AfterEach
    public void afterEachMethod() {

        log.info("{}In @AfterEach\nClass instance: {}\n", Constants.EYE_CATCHER, this);
    }
}
