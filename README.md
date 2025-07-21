# Todo API

A simple task management REST API built with **Spring Boot** and **Gradle**, offering categorized todos with priority levels, completion tracking, and statistical insights.

## Features

* Create, read, update, and delete todos
* Categorize todos by user-defined categories
* Assign priorities and track completion
* View statistical summaries:
    * Todos per category
    * Average time open per category
    * Longest open todos
    * Todos completed today
    * Open todos grouped by priority
    * Min, max, and average time open

## Technologies

* Java 17+
* Spring Boot 3
* Spring Data JPA
* PostgreSQL (configurable)
* Swagger/OpenAPI (springdoc)
* Gradle

## Getting Started

### Prerequisites

* Java 17+
* Gradle
* Docker

### Setup

1. Clone the repository:

```bash
git clone https://github.com/Alalilacias/todoapi.git
cd todoapi
```

2. Start database (optional):

```bash
docker-compose up -d
```

3. Run the application:

```bash
./gradlew bootRun
```

The API will be available at `http://localhost:8080`.

## API Documentation

Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

## Sample Requests

### Create a Category

```http
POST /api/categories
Content-Type: application/json

{
  "name": "Work"
}
```

### Create a Todo

```http
POST /api/todos
Content-Type: application/json

{
  "title": "Submit report",
  "description": "End-of-quarter summary",
  "category": "Work",
  "priority": "HIGH"
}
```

### Update a Todo

```http
PUT /api/todos/1
Content-Type: application/json

{
  "title": "Submit final report",
  "description": "With new revenue data",
  "completed": true,
  "category": "Work",
  "priority": "MEDIUM"
}
```

## Running Tests

```bash
./gradlew test
```

> You can use the provided Postman collection (`todoapi-postman-collection.json`) to run manual or automated request sequences.

