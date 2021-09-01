package com.example.testcontainersdemo;

import org.testcontainers.utility.DockerImageName;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {

    public static final String EYE_CATCHER = "\n#####################################\n";

    // Local version of http://httpbin.org
    // docker run -p 80:80 kennethreitz/httpbin
    public static final DockerImageName HTTPBIN_IMAGE = DockerImageName.parse("kennethreitz/httpbin:latest");

    // The specified directory should contain a clone of https://github.com/testcontainers/helloworld
    // docker run -p 8080:8080 -p 8081:8081 -e DELAY_START_MSEC=2000 testcontainers/helloworld
    public static final Path HELLOWORLD_SOURCE = Paths.get("temp/helloworld");

}
