package com.example.testcontainersdemo.tcbasics;

import com.example.testcontainersdemo.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.startupcheck.OneShotStartupCheckStrategy;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.testcontainers.containers.output.OutputFrame.OutputType.STDERR;
import static org.testcontainers.containers.output.OutputFrame.OutputType.STDOUT;

// This test shows how to access container logs (all or stdout/stderr separately)
// either all at once or as a stream using Slf4jLogConsumer

// This test also illustrates:
//   - Manual control of container create/start
//   - Setting/getting runtime environment variables
//   - Setting container start command

@Testcontainers
@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class Demo5_ContainerLogsTest {

    private static GenericContainer<?> shortLivedContainer() {
        return new GenericContainer<>(Constants.ALPINE_IMAGE)
                .withEnv("GREETING", "Hello, world!")
                .withCommand("/bin/sh", "-c",
                        "echo -e \">> Starting up with GREETING=$GREETING\" && " +
                        "echo -e \">> Oops! Something went wrong\" 1>&2")
                .withStartupCheckStrategy(new OneShotStartupCheckStrategy());
    }

    @Test
    @DisplayName("get_logs_as_string")
    public void test1() {
        try (GenericContainer<?> container = shortLivedContainer()) {
            container.start();

            log.info("Container logs (all):\n{}", container.getLogs());

            log.info("Container logs (stdout only):\n{}", container.getLogs(STDOUT));

            log.error("Container logs (stderr only):\n{}", container.getLogs(STDERR));

            Assertions.assertTrue(container.getEnvMap().get("GREETING").equalsIgnoreCase("Hello, world!"));

        }
    }

    @Test
    @DisplayName("get_logs_as_stream")
    public void test2() {
        try (GenericContainer<?> container = shortLivedContainer()) {
            container.start();

            Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log).withSeparateOutputStreams();
            container.followOutput(logConsumer);

            Assertions.assertTrue(container.getEnvMap().get("GREETING").equalsIgnoreCase("Hello, world!"));

        }
    }
}
