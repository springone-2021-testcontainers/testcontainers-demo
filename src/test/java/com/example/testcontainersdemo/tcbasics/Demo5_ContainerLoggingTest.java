package com.example.testcontainersdemo.tcbasics;

import com.example.testcontainersdemo.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.startupcheck.OneShotStartupCheckStrategy;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.testcontainers.containers.output.OutputFrame.OutputType.STDERR;
import static org.testcontainers.containers.output.OutputFrame.OutputType.STDOUT;

// This test shows how to access container logs (all or stdout/stderr separately).
// Streaming logs is also possible (see https://www.testcontainers.org/features/container_logs)

// This test also illustrates:
//   - Manual control of container create/start
//   - Setting/getting runtime environment variables
//   - Setting container start comman

@Testcontainers
@Slf4j
@SpringBootTest
public class Demo5_ContainerLoggingTest {

    private static GenericContainer<?> shortLivedContainer() {
        return new GenericContainer<>(Constants.ALPINE_IMAGE)
                .withEnv("GREETING", "Hello, world!")
                .withCommand("/bin/sh", "-c",
                        "echo -e \">> Starting up with GREETING=$GREETING\" && " +
                        "echo -e \">> Oops! Something went wrong\" 1>&2")
                .withStartupCheckStrategy(new OneShotStartupCheckStrategy());
    }

    @Test
    @DisplayName("get_logs")
    public void test1() {
        try (GenericContainer<?> container = shortLivedContainer()) {
            container.start();

            log.info("Container logs (all):\n{}", container.getLogs());

            log.info("Container logs (stdout only):\n{}", container.getLogs(STDOUT));

            log.error("Container logs (stderr only):\n{}", container.getLogs(STDERR));

            Assertions.assertTrue(container.getEnvMap().get("GREETING").equalsIgnoreCase("Hello, world!"));

        }

    }
}
