# Knowledge Notes API

A small Spring Boot REST API for managing knowledge articles grouped by category. It is a learning project that covers
CRUD, validation, pagination, error handling and testing.

> **Status:** early stage. Only the project skeleton and a `/api/ping` endpoint exist. The CRUD features are planned
> in the [specification](docs/project-specification.en.md).

## Tech Stack

Java 17 · Spring Boot 4.1.2 · Spring Web MVC · Bean Validation · Actuator · Maven

## Prerequisites

- JDK 17 or newer (`java -version`)
- No separate Maven install is needed; the Maven wrapper (`mvnw`) is included

## Run

```bash
./mvnw spring-boot:run
```

On Windows (PowerShell or CMD) use `.\mvnw spring-boot:run`.

The server starts at `http://localhost:8080`. Check it:

```bash
curl http://localhost:8080/api/ping
curl http://localhost:8080/actuator/health
```

## Test and Build

```bash
./mvnw test            # run tests
./mvnw verify          # build and run all tests
./mvnw clean package   # build the jar
java -jar target/knowledge-search-api-0.0.1-SNAPSHOT.jar
```

## Run from VS Code

Open `KnowledgeSearchApiApplication.java` and click **Run** above the `main` method
(requires the Extension Pack for Java).

## Endpoints

| Method | Path | Description |
|---|---|---|
| GET | `/api/ping` | Returns `{"status":"ok","time":"..."}` |
| GET | `/actuator/health` | Application health |

## Documentation

- [Project specification (English)](docs/project-specification.en.md)
- [Đặc tả dự án (Tiếng Việt)](docs/project-specification.vi.md)

## Troubleshooting

- **Port 8080 already in use:** run with `./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081`.
- **`JAVA_HOME` not set or wrong version:** point it to a JDK 17+ installation.
