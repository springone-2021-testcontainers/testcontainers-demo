package com.example.testcontainersdemo;

import org.testcontainers.utility.DockerImageName;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {

    public static final String EYE_CATCHER = "\n#####################################\n";

    // Local version of http://httpbin.org
    // docker run -p 80:80 kennethreitz/httpbin
    public static final DockerImageName HTTPBIN_IMAGE = DockerImageName.parse("kennethreitz/httpbin:latest");

    // This directory should contain a clone of http://github.com/ciberkleid/go-sample-app
    // docker run -p 8080:8080 -e environment=DEV hello-server
    public static final Path HELLO_SERVER_SOURCE = Paths.get("temp/hello-server");

}
