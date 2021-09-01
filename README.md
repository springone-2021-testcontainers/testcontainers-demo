# testcontainers-demo

This repo is intended for demonstrating and exporing Testcontainers functionality.
Run tests individually and examine their behavior.

To run a given test, edit the following command as appropriate:
```shell
./mvnw -Dtest=Demo1_* test
```

For `Demo4_DockerfileTest`, run the following command before executing the test:
```shell
git clone https://github.com/testcontainers/helloworld.git temp/helloworld
```

