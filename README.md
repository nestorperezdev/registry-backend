# Spring registry backend with GraphQL

[![Project Build](https://github.com/nestorperezdev/registry-backend/actions/workflows/build.yaml/badge.svg?branch=master)](https://github.com/nestorperezdev/registry-backend/actions/workflows/build.yaml)
[![Coverage Status](https://coveralls.io/repos/github/codecentric/springboot-sample-app/badge.svg?branch=master)](https://coveralls.io/github/codecentric/springboot-sample-app?branch=master)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

This application is a wrapper around the docker registry API (v2)

The user can use his own registry docker server (url, port password and username) to connect to his registry and consume the data in a graphql form.

This app makes use of [ExpediaGroup](https://github.com/ExpediaGroup/graphql-kotlin) open source graphql kotlin library.

## Requirements

For building and running the application you need:

- [JDK 11](https://www.azul.com/downloads/?version=java-11-lts&package=jdk)
- [Gradle 7.5.1](https://gradle.org/releases/)

## Building the application
 Build and test this app:
```shell
./gradlew assemble
```

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `dev.nestorperez.registrybackend` class from your IDE.

Alternatively you can use the gradlew command:

```shell
./gradlew bootRun
```

## Testing application
```shell
./gradlew clean test
```

## Contributing
TBD

## Usage
After running the app you can visit:

* http://localhost:8080/playground: Inspect the schema and run queries.
* http://localhost:8080/graphql: Download schema (WIP)


## Copyright

Released under the Apache License 2.0. See the [LICENSE](https://github.com/codecentric/springboot-sample-app/blob/master/LICENSE) file.