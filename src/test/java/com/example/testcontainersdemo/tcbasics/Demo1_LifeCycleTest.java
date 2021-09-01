package com.example.testcontainersdemo.tcbasics;

import com.example.testcontainersdemo.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

// This test helps understand how Testcontainers manages the container lifecycle
// in relation to the JUnit lifecycle.

@Testcontainers
@Slf4j
public class Demo1_LifeCycleTest {

    @Container
    GenericContainer container = new GenericContainer(Constants.HTTPBIN_IMAGE);

    public Demo1_LifeCycleTest() {

        log.info("{} In Constructor. Instance: {}\n", Constants.EYE_CATCHER, this);
    }

    @BeforeAll
    public static void beforeAllMethod() {

        log.info("{} In @BeforeAll. Instance: {}\n", Constants.EYE_CATCHER, "<static method>");
    }

    @AfterAll
    public static void afterAllMethod() {

        log.info("{} In @AfterAll. Instance: {}\n", Constants.EYE_CATCHER, "<static method>");
    }

    @Test
    public void test1() {

        log.info("{} In @Test 1. Instance: {}\n", Constants.EYE_CATCHER, this);
    }

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

