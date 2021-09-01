package com.example.testcontainersdemo.tcbasics;

import com.example.testcontainersdemo.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

// This test shows how a container can be reuse across tests.
// The container is declared as 'static' so it is share by all class instances
// (aka by all test-level method executions) rather than being instantiated when
// the constructor runs.
// Consider in this case if it is necessary to add cleanup logic to @BeforeEach
// to ensure subsequent tests have predictable starting conditions.
// For this non-SpringBootTest, logging is controlled through logback xml file

@Testcontainers
@Slf4j
public class Demo2_ReuseContainerTest {


    @Container
    static GenericContainer container = new GenericContainer(Constants.HTTPBIN_IMAGE);

    public Demo2_ReuseContainerTest() {

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
    }

    @Test
    public void test2() {

        log.info("{}In @Test 2\nClass instance: {}\n", Constants.EYE_CATCHER, this);
    }

    @BeforeEach
    public void beforeEachMethod() {

        log.info("{}In @BeforeEach\nClass instance: {}\nContainer id: {}\n", Constants.EYE_CATCHER, this, container.getContainerId());
    }

    @AfterEach
    public void afterEachMethod() {

        log.info("{}In @AfterEach\nClass instance: {}\n", Constants.EYE_CATCHER, this);
    }
}

