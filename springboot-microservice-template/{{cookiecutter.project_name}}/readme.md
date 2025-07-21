# {{ cookiecutter.project_name }}

{{ cookiecutter.project_description }}

## Requirements

- Java {{ cookiecutter.java_version }}
- {{ cookiecutter.maven_or_gradle|capitalize }}

## Getting Started

To build and run the application:

```sh
# Using Maven
./mvnw spring-boot:run

# Using Gradle
./gradlew bootRun
```

## License

This project is licensed under the {{ cookiecutter.license }} License.

## 🚀 Quick Start

### Prerequisites

- Java {{ cookiecutter.java_version }}+
- {% if cookiecutter.maven_or_gradle == "maven" %}Maven 3.6+{% else %}Gradle 7.0+{% endif %}
{% if cookiecutter.database_type == "postgresql" -%}
- PostgreSQL 12+
{%- elif cookiecutter.database_type == "mysql" -%}
- MySQL 8.0+
{%- endif %}
{% if cookiecutter.include_cache == "redis" -%}
- Redis 6.0+
{%- endif %}
{% if cookiecutter.include_docker == "yes" -%}
- Docker & Docker Compose (optional)
{%- endif %}

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd {{ cookiecutter.project_slug }}
   ```

2. **Configure Database**
   {% if cookiecutter.database_type == "postgresql" -%}
   ```sql
   -- PostgreSQL
   CREATE DATABASE {{ cookiecutter.project_slug.replace('-', '_') }};
   ```
   {%- elif cookiecutter.database_type == "mysql" -%}
   ```sql
   -- MySQL
   CREATE DATABASE {{ cookiecutter.project_slug.replace('-', '_') }};
   ```
   {%- elif cookiecutter.database_type == "h2" -%}
   ```
   # H2 Database (In-Memory) - No setup required
   ```
   {%- endif %}

3. **Environment Variables**
   ```bash
   # Database
   export DB_USERNAME=your_db_username
   export DB_PASSWORD=your_db_password
   
   {% if cookiecutter.include_security == "jwt" -%}
   # JWT Security
   export JWT_SECRET=your_jwt_secret_key
   {%- endif %}
   
   {% if cookiecutter.include_cache == "redis" -%}
   # Redis (if using Redis cache)
   export REDIS_HOST=localhost
   export REDIS_PORT=6379
   {%- endif %}
   ```

4. **Run the application**
   ```bash
   # Using Maven
   ./mvnw spring-boot:run
   
   # Or using Gradle
   ./gradlew bootRun
   ```

{% if cookiecutter.include_docker == "yes" -%}
### 🐳 Docker Deployment

1. **Build and run with Docker Compose**
   ```bash
   docker-compose up --build
   ```

2. **Run individual containers**
   ```bash
   # Build the image
   docker build -t {{ cookiecutter.project_slug }} .
   
   # Run the container
   docker run -p 8080:8080 {{ cookiecutter.project_slug }}
   ```
{%- endif %}

## 📋 API Documentation

{% if cookiecutter.include_swagger == "yes" -%}
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Docs**: http://localhost:8080/api-docs
{%- endif %}

### Available Endpoints

- **Health Check**: `GET /actuator/health`
{% if cookiecutter.include_security != "none" -%}
- **Authentication**: `POST /api/v1/auth/login`
{%- endif %}
- **API Base URL**: `http://localhost:8080/api/v1`

## 🏗️ Architecture

### Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── {{ cookiecutter.package_path }}/
│   │       ├── Application.java
│   │       ├── config/
│   │       │   └── SecurityConfig.java
│   │       ├── controller/
│   │       │   └── BaseController.java
│   │       ├── dto/
│   │       │   ├── ApiResponse.java
│   │       │   └── ErrorResponse.java
│   │       ├── entity/
│   │       │   └── BaseEntity.java
│   │       ├── exception/
│   │       │   ├── BusinessException.java
│   │       │   └── ResourceNotFoundException.java
│   │       ├── repository/
│   │       │   └── BaseRepository.java
│   │       ├── service/
│   │       └── security/
│   └── resources/
│       ├── application.yml
│       └── db/migration/
└── test/
    └── java/
```

### Key Features

- ✅ **RESTful API** with proper HTTP status codes
- ✅ **Exception Handling** with global error handling
- ✅ **Data Validation** using Bean Validation
- ✅ **Database Integration** with JPA/Hibernate
- ✅ **Database Migration** with Flyway
{% if cookiecutter.include_security != "none" -%}
- ✅ **Security** with Spring Security
{%- endif %}
{% if cookiecutter.include_security == "jwt" -%}
- ✅ **JWT Authentication** for stateless security
{%- endif %}
{% if cookiecutter.include_cache != "none" -%}
- ✅ **Caching** with {% if cookiecutter.include_cache == "redis" %}Redis{% else %}Caffeine{% endif %}
{%- endif %}
{% if cookiecutter.include_swagger == "yes" -%}
- ✅ **API Documentation** with OpenAPI/Swagger
{%- endif %}
{% if cookiecutter.include_actuator == "yes" -%}
- ✅ **Monitoring** with Spring Boot Actuator
{%- endif %}
- ✅ **Logging** with Logback
- ✅ **Testing** with JUnit 5 and TestContainers
{% if cookiecutter.include_docker == "yes" -%}
- ✅ **Containerization** with Docker
{%- endif %}

## 🧪 Testing

```bash
# Run all tests
./mvnw test

# Run tests with coverage
./mvnw test jacoco:report

# Run integration tests
./mvnw test -Pintegration-tests
```

## 📊 Monitoring

{% if cookiecutter.include_actuator == "yes" -%}
### Actuator Endpoints

- **Health**: `/actuator/health`
- **Metrics**: `/actuator/metrics`
- **Info**: `/actuator/info`
- **Prometheus**: `/actuator/prometheus`
{%- endif %}

### Logging

- **Console**: Enabled for development
- **File**: `logs/{{ cookiecutter.project_slug }}.log`
- **Level**: Configurable via `logging.level.root`

## 🔧 Configuration

### Profiles

- **default**: Development profile
- **test**: Testing profile
