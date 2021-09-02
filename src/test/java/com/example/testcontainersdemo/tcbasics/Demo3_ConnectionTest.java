package com.example.testcontainersdemo.tcbasics;

import com.example.testcontainersdemo.Constants;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

// This test exposes a port on the container and dynamically retrieves the
// port in the test method in order to communicate with the container.
// The first test with the hard-coded host and port is expected to fail
// for illustration purposes.

@Testcontainers
@Slf4j
@SpringBootTest
public class Demo3_ConnectionTest {

    RestTemplate restTemplate = new RestTemplate();

    @Container
    static GenericContainer container = new GenericContainer(Constants.HTTPBIN_IMAGE)
            .withExposedPorts(80);

    @Test
    @DisplayName("withFixedUrl")
    public void test1() {

        String url = "http://localhost:80";
        log.info("URL: {}", url);
        ResponseEntity<String> response
                = restTemplate.getForEntity(url + "/uuid", String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK, () -> "This is not okay.");
    }

    @Test
    @DisplayName("withDynamicUrl")
    public void test2() {

        String url = "http://" + container.getHost() + ":" + container.getFirstMappedPort();
        log.info("URL: {}", url);
        ResponseEntity<String> response
                = restTemplate.getForEntity(url + "/uuid", String.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK, () -> "This is not okay.");
    }

    @BeforeEach
    public void beforeEachMethod(TestInfo testInfo) {

        log.info("{}Test: {}\nClass instance: {}\nContainer id: {}\n", Constants.EYE_CATCHER, testInfo.getDisplayName(), this, container.getContainerId());
    }
}

