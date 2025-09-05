# Hibernate Second-Level Cache with Redis

This project demonstrates the implementation of **Hibernate's second-level cache with Redis** as covered in the comprehensive blog post: [Use Hibernate second level cache with redis](https://m8e.ir/blog/3384535739-use-hibernate-second-level-cache-with-redis.html).

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Performance Benefits](#performance-benefits)
- [Monitoring Cache Performance](#monitoring-cache-performance)
- [Troubleshooting](#troubleshooting)
- [Additional Resources](#additional-resources)

## 🎯 Overview

This Spring Boot application showcases how to implement Hibernate's second-level cache using Redis as the cache provider. The project includes:

- **Note Management System**: CRUD operations for managing notes
- **Hibernate Second-Level Cache**: Entity and query caching with Redis
- **Custom Cache Region Factory**: Custom implementation for Redis integration
- **Comprehensive Testing**: 93+ unit tests covering all components
- **Docker Support**: Easy setup with MySQL and Redis containers

### Key Benefits

- **Reduced Database Load**: Significantly fewer database queries for read operations
- **Improved Performance**: Faster response times for frequently accessed data
- **Scalable Caching**: Redis-based distributed caching for multi-instance deployments
- **Automatic Cache Management**: Hibernate handles cache eviction and consistency

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   REST Client   │───▶│  NoteController │───▶│   NoteService   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                        │
                                                        ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│      Redis      │◀───│   Hibernate     │◀───│ NoteRepository  │
│   (L2 Cache)    │    │  SessionFactory │    │     (JPA)       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                        │
                                                        ▼
                                               ┌─────────────────┐
                                               │      MySQL      │
                                               │   (Database)    │
                                               └─────────────────┘
```

## 📁 Project Structure

```
hibernate-cache/
├── src/
│   ├── main/
│   │   ├── java/me/artm2000/hibernatecache/
│   │   │   ├── common/
│   │   │   │   ├── config/
│   │   │   │   │   └── RedisConfig.java              # Redis client configuration
│   │   │   │   └── CustomizeRegionFactory.java       # Custom Hibernate cache factory
│   │   │   ├── controller/
│   │   │   │   └── NoteController.java               # REST API endpoints
│   │   │   ├── database/
│   │   │   │   ├── entity/
│   │   │   │   │   └── Note.java                     # JPA entity with cache annotations
│   │   │   │   └── repository/
│   │   │   │       └── NoteRepository.java           # JPA repository with query cache
│   │   │   ├── service/
│   │   │   │   ├── impl/
│   │   │   │   │   └── NoteServiceImpl.java          # Business logic implementation
│   │   │   │   └── NoteService.java                  # Service interface
│   │   │   └── HibernateCacheApplication.java        # Spring Boot main class
│   │   └── resources/
│   │       ├── application.properties                # Base configuration
│   │       └── application-local.properties          # Local profile configuration
│   └── test/
│       └── java/me/artm2000/hibernatecache/unit/
│           ├── config/
│           │   └── RedisConfigTest.java              # Redis config unit tests
│           ├── controller/
│           │   └── NoteControllerTest.java           # Controller unit tests
│           ├── database/
│           │   ├── entity/
│           │   │   └── NoteTest.java                 # Entity unit tests
│           │   └── repository/
│           │       └── NoteRepositoryTest.java       # Repository unit tests
│           └── service/
│               └── NoteServiceImplTest.java          # Service unit tests
├── build.gradle.kts                                  # Gradle build configuration
├── compose.yaml                                      # Docker Compose for MySQL & Redis
├── gradlew                                           # Gradle wrapper (Unix)
├── gradlew.bat                                       # Gradle wrapper (Windows)
└── README.md                                         # This file
```

## 🔧 Prerequisites

- **Java 21** or higher
- **Docker & Docker Compose** (for database and Redis)
- **Gradle** (wrapper included)

## 🚀 Quick Start

### 1. Clone and Setup

```bash
git clone <repository-url>
cd hibernate-cache
```

### 2. Start Infrastructure Services

Start MySQL and Redis using Docker Compose:

```bash
# Start services in background
docker-compose up -d

# Verify services are running
docker-compose ps
```

This will start:
- **MySQL 8.0** on `localhost:3306`
- **Redis 8** on `localhost:6379`

### 3. Run the Application

#### Option A: Using Gradle with Local Profile (Recommended)

```bash
# Run with local profile (uses application-local.properties)
./gradlew bootRun --args='--spring.profiles.active=local'
```

#### Option B: Using IDE

1. Import the project into your IDE (IntelliJ IDEA, Eclipse, etc.)
2. Set the active profile to `local` in your run configuration:
   - **IntelliJ IDEA**: Run Configuration → Environment Variables → `spring.profiles.active=local`
   - **Eclipse**: Run Configuration → Arguments → Program Arguments: `--spring.profiles.active=local`
3. Run `HibernateCacheApplication.java`

#### Option C: Using JAR

```bash
# Build the application
./gradlew build

# Run the JAR with local profile
java -jar build/libs/hibernate-cache-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

### 4. Verify Application is Running

```bash
# Check application health
curl http://localhost:8080/actuator/health

# Expected response:
# {"status":"UP"}
```

## ⚙️ Configuration

### Application Profiles

The application supports different profiles for different environments:

#### Base Configuration (`application.properties`)
```properties
spring.application.name=hibernate-cache
server.port=8080

# Enable SQL logging to see database hits
spring.jpa.show-sql=true

# Hibernate second-level cache configuration
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.use_query_cache=true
spring.jpa.properties.hibernate.cache.region.factory_class=me.artm2000.hibernatecache.common.CustomizeRegionFactory
spring.jpa.properties.hibernate.cache.use_minimal_puts=true
```

#### Local Profile (`application-local.properties`)
```properties
# Database configuration
spring.datasource.url=jdbc:mysql://localhost:3306/hibernate_cache_db
spring.datasource.username=user
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update

# Redis configuration
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.password=
spring.data.redis.database=0
```

### Cache Configuration

The project uses a **custom cache region factory** (`CustomizeRegionFactory`) that:
- Integrates Hibernate with Redis via Redisson client
- Supports profile-based configuration loading
- Handles cache regions for entities and queries
- Provides automatic cache eviction and consistency

## 🌐 API Endpoints

### Note Management API

| Method | Endpoint | Description | Cache Behavior |
|--------|----------|-------------|----------------|
| `POST` | `/v1/notes` | Create a new note | Evicts related query cache |
| `GET` | `/v1/notes` | Get all non-archived notes | Uses query cache |
| `GET` | `/v1/notes/all` | Get all notes (including archived) | Uses query cache |
| `GET` | `/v1/notes/{id}` | Get note by ID | Uses entity cache |
| `GET` | `/v1/notes/search?title={title}` | Get note by title | Uses query cache |
| `PUT` | `/v1/notes/{id}` | Update note by ID | Updates entity cache |
| `PATCH` | `/v1/notes/{id}/archive` | Archive note by ID | Updates entity cache |
| `DELETE` | `/v1/notes/{id}` | Delete note by ID | Evicts from cache |

### Example Usage

#### Create a Note
```bash
curl -X POST "http://localhost:8080/v1/notes" \
  -H 'Content-Type: application/json' \
  -d '{"title":"My First Note", "content":"This is the content of my first note"}'
```

#### Get All Notes (First call - hits database)
```bash
curl "http://localhost:8080/v1/notes"
```

**Console Output (first call):**
```
Getting all non-archived notes
Hibernate: select n1_0.id,n1_0.archived,n1_0.content,n1_0.title from notes n1_0 where n1_0.archived=?
```

#### Get All Notes (Second call - from cache)
```bash
curl "http://localhost:8080/v1/notes"
```

**Console Output (second call):**
```
Getting all non-archived notes
# No Hibernate SQL query - served from cache! 🚀
```

#### Get Note by ID
```bash
curl "http://localhost:8080/v1/notes/1"
```

#### Update Note
```bash
curl -X PUT "http://localhost:8080/v1/notes/1" \
  -H 'Content-Type: application/json' \
  -d '{"title":"Updated Note", "content":"Updated content", "archived":false}'
```

#### Archive Note
```bash
curl -X PATCH "http://localhost:8080/v1/notes/1/archive"
```

#### Delete Note
```bash
curl -X DELETE "http://localhost:8080/v1/notes/1"
```

## 🧪 Testing

The project includes **93 comprehensive unit tests** covering all components:

### Run All Tests
```bash
./gradlew test
```

### Run Only Unit Tests
```bash
./gradlew test --tests "me.artm2000.hibernatecache.unit.*"
```

### Test Categories

| Test Class | Tests | Coverage |
|------------|-------|----------|
| `NoteTest` | 11 | Entity validation, getters/setters, equals/hashCode |
| `NoteServiceImplTest` | 23 | Business logic, null handling, edge cases |
| `NoteControllerTest` | 24 | REST endpoints, request/response handling |
| `NoteRepositoryTest` | 23 | Repository methods, query behavior |
| `RedisConfigTest` | 12 | Redis configuration, client creation |

### View Test Reports
```bash
# Open test report in browser (macOS)
open build/reports/tests/test/index.html

# Linux
xdg-open build/reports/tests/test/index.html

# Windows
start build/reports/tests/test/index.html
```

## 📈 Performance Benefits

### Cache Hit Demonstration

The application demonstrates significant performance improvements:

1. **First Request**: Database query executed
   ```
   Hibernate: select n1_0.id,n1_0.archived,n1_0.content,n1_0.title from notes n1_0 where n1_0.archived=?
   ```

2. **Subsequent Requests**: Served from Redis cache
   - No database query
   - Significantly faster response time
   - Reduced database load

### Cache Regions

The application uses different cache regions:

- **Entity Cache**: `entity.notes` - Caches individual Note entities
- **Query Cache**: 
  - `query.findNotesByTitle` - Caches title-based queries
  - `query.findAllNotes` - Caches all notes queries
  - `query.findAllNotesByArchived` - Caches archived status queries

## 📊 Monitoring Cache Performance

### Enable Hibernate Statistics
Add to your configuration:
```properties
spring.jpa.properties.hibernate.generate_statistics=true
```

### Redis Monitoring
```bash
# Connect to Redis CLI
docker exec -it redis_container redis-cli

# Monitor Redis commands in real-time
MONITOR

# Check cache keys
KEYS *

# Get cache statistics
INFO memory
```

### Application Metrics
Access Spring Boot Actuator endpoints:
```bash
# Application health
curl http://localhost:8080/actuator/health

# Application metrics
curl http://localhost:8080/actuator/metrics
```

## 🔍 Troubleshooting

### Common Issues

#### 1. Connection Refused Errors
**Problem**: Can't connect to MySQL or Redis
```
java.net.ConnectException: Connection refused
```

**Solution**: Ensure Docker services are running
```bash
docker-compose up -d
docker-compose ps
```

#### 2. Cache Not Working
**Problem**: Still seeing database queries on repeated requests

**Solution**: Check cache configuration and Redis connection
```bash
# Verify Redis is accessible
docker exec -it redis_container redis-cli ping
# Expected: PONG

# Check cache keys in Redis
docker exec -it redis_container redis-cli
> KEYS *
```

#### 3. Profile Not Loading
**Problem**: Application using wrong configuration

**Solution**: Ensure profile is set correctly
```bash
# Check active profile in logs
./gradlew bootRun --args='--spring.profiles.active=local'

# Look for log line:
# The following 1 profile is active: local
```

#### 4. Build Failures
**Problem**: Gradle build fails

**Solution**: Clean and rebuild
```bash
./gradlew clean build
```

### Debug Mode

Enable debug logging for cache operations:
```properties
logging.level.org.hibernate.cache=DEBUG
logging.level.me.artm2000.hibernatecache=DEBUG
```

## 🛠️ Development Commands

### Gradle Tasks
```bash
# Build the application
./gradlew build

# Run tests
./gradlew test

# Clean build artifacts
./gradlew clean

# Run with specific profile
./gradlew bootRun --args='--spring.profiles.active=local'

# Build without tests
./gradlew build -x test
```

### Docker Commands
```bash
# Start services
docker-compose up -d

# Stop services
docker-compose down

# View logs
docker-compose logs mysql
docker-compose logs redis

# Remove volumes (reset data)
docker-compose down -v
```

### Database Operations
```bash
# Connect to MySQL
docker exec -it mysql_container mysql -u user -p hibernate_cache_db

# Connect to Redis
docker exec -it redis_container redis-cli

# Backup MySQL data
docker exec mysql_container mysqldump -u user -p hibernate_cache_db > backup.sql
```

## 📚 Additional Resources

- **Blog Post**: [Use Hibernate second level cache with redis](https://m8e.ir/blog/3384535739-use-hibernate-second-level-cache-with-redis.html) - Comprehensive guide covering the implementation
- **Hibernate Documentation**: [Second-level cache](https://docs.jboss.org/hibernate/orm/6.0/userguide/html_single/Hibernate_User_Guide.html#caching)
- **Redisson Documentation**: [Hibernate Cache](https://redisson.org/glossary/hibernate-cache.html)
- **Spring Boot Caching**: [Caching Data with Spring](https://spring.io/guides/gs/caching/)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

**Happy Caching! 🚀**

For questions or issues, create an issue in the repository.
