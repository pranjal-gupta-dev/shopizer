# Java 21 Migration Plan 2: Aggressive Modernization Approach

**Strategy**: Fast migration with modernization, leveraging Java 21 features and latest dependencies.

**Timeline**: 1-2 weeks  
**Risk Level**: Medium-High  
**Rollback Difficulty**: Moderate

---

## Phase 1: Rapid Environment Setup (Day 1)

### 1.1 Install Java 21
```bash
brew install openjdk@21
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
java -version
```

### 1.2 Create Migration Branch
```bash
git checkout -b feature/java-21-aggressive
```

### 1.3 Analyze Update Scope
```bash
mvnw versions:display-dependency-updates > dependency-updates.txt
mvnw versions:display-plugin-updates > plugin-updates.txt
```

---

## Phase 2: Bulk Configuration Updates (Days 2-3)

### 2.1 Update Root pom.xml - All at Once

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.shopizer</groupId>
    <artifactId>shopizer</artifactId>
    <packaging>pom</packaging>
    <version>3.2.5</version>

    <name>shopizer</name>
    <url>http://www.shopizer.com</url>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.0</version>
    </parent>

    <modules>
        <module>sm-core-model</module>
        <module>sm-core-modules</module>
        <module>sm-core</module>
        <module>sm-shop-model</module>
        <module>sm-shop</module>
    </modules>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <java.version>21</java.version>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <maven.compiler.release>21</maven.compiler.release>

        <!-- Updated dependencies -->
        <elasticsearch.version>8.13.0</elasticsearch.version>
        <guava.version>33.1.0-jre</guava.version>
        <commons-lang.version>3.14.0</commons-lang.version>
        <commons-io.version>2.16.0</commons-io.version>
        <commons-collections4.version>4.4</commons-collections4.version>
        <commons-validator.version>1.8.0</commons-validator.version>
        <commons-fileupload>1.5</commons-fileupload>
        <org.mapstruct.version>1.5.5.Final</org.mapstruct.version>

        <org.apache.httpcomponent.version>5.3.1</org.apache.httpcomponent.version>
        <jakarta.inject.version>2.0.1</jakarta.inject.version>
        <jakarta.el.version>5.0.0</jakarta.el.version>
        <jakarta.servlet-api-version>6.0.0</jakarta.servlet-api-version>
        <jakarta.annotation.version>2.1.1</jakarta.annotation.version>
        
        <infinispan.version>15.0.0.Final</infinispan.version>
        <mysql-jdbc-version>8.3.0</mysql-jdbc-version>
        <oracle.version>23.3.0.23.09</oracle.version>
        <postgresql.version>42.7.3</postgresql.version>
        
        <jackson-version>2.17.0</jackson-version>
        <jackson-version-databind>2.17.0</jackson-version-databind>
        <geoip2.version>4.2.0</geoip2.version>
        <drools.version>9.44.0.Final</drools.version>
        <jwt.version>0.12.5</jwt.version>

        <!-- SpringDoc OpenAPI (replaces Swagger) -->
        <springdoc.version>2.5.0</springdoc.version>

        <!-- Plugin versions -->
        <maven-compiler-plugin.version>3.13.0</maven-compiler-plugin.version>
        <jacoco.version>0.8.12</jacoco.version>
        <spotbugs.version>4.8.3.1</spotbugs.version>
        <dependency-check.version>9.1.0</dependency-check.version>
        <checkstyle.version>3.3.1</checkstyle.version>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven-compiler-plugin.version}</version>
                <configuration>
                    <source>21</source>
                    <target>21</target>
                    <release>21</release>
                    <compilerArgs>
                        <arg>--enable-preview</arg>
                    </compilerArgs>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### 2.2 Update Maven Wrapper
```bash
mvnw wrapper:wrapper -Dmaven=3.9.6
```

---

## Phase 3: Mass Code Migration (Days 4-6)

### 3.1 Automated Namespace Migration
```bash
# Use OpenRewrite for automated migration
# Add to root pom.xml temporarily:
```

```xml
<plugin>
    <groupId>org.openrewrite.maven</groupId>
    <artifactId>rewrite-maven-plugin</artifactId>
    <version>5.30.0</version>
    <configuration>
        <activeRecipes>
            <recipe>org.openrewrite.java.migrate.JavaVersion21</recipe>
            <recipe>org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_3</recipe>
        </activeRecipes>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>org.openrewrite.recipe</groupId>
            <artifactId>rewrite-migrate-java</artifactId>
            <version>2.12.0</version>
        </dependency>
        <dependency>
            <groupId>org.openrewrite.recipe</groupId>
            <artifactId>rewrite-spring</artifactId>
            <version>5.9.0</version>
        </dependency>
    </dependencies>
</plugin>
```

```bash
# Run automated migration
mvnw rewrite:run

# Review changes
git diff
```

### 3.2 Manual javax → jakarta Migration
```bash
# Find and replace across all Java files
find . -name "*.java" -type f -exec sed -i '' 's/import javax\.persistence/import jakarta.persistence/g' {} +
find . -name "*.java" -type f -exec sed -i '' 's/import javax\.validation/import jakarta.validation/g' {} +
find . -name "*.java" -type f -exec sed -i '' 's/import javax\.servlet/import jakarta.servlet/g' {} +
find . -name "*.java" -type f -exec sed -i '' 's/import javax\.annotation/import jakarta.annotation/g' {} +
find . -name "*.java" -type f -exec sed -i '' 's/import javax\.inject/import jakarta.inject/g' {} +
find . -name "*.java" -type f -exec sed -i '' 's/import javax\.transaction/import jakarta.transaction/g' {} +
```

### 3.3 Update Swagger to SpringDoc OpenAPI

Remove old Swagger annotations and imports:
```bash
# Find Swagger usage
grep -r "@Api\|@ApiOperation\|@ApiModel" --include="*.java" .
```

Replace with SpringDoc equivalents:
- `@Api` → `@Tag`
- `@ApiOperation` → `@Operation`
- `@ApiModel` → `@Schema`
- `@ApiModelProperty` → `@Schema`
- `@ApiParam` → `@Parameter`

Update configuration class:
```java
// Remove Swagger2 config
// SpringDoc auto-configures, just add properties to application.properties:
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## Phase 4: Modernize with Java 21 Features (Days 7-8)

### 4.1 Introduce Record Classes for DTOs
Example transformation:
```java
// Before
public class ProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    // getters, setters, equals, hashCode, toString
}

// After
public record ProductDTO(Long id, String name, BigDecimal price) {}
```

### 4.2 Use Pattern Matching
```java
// Before
if (obj instanceof String) {
    String str = (String) obj;
    return str.length();
}

// After
if (obj instanceof String str) {
    return str.length();
}
```

### 4.3 Switch Expressions
```java
// Before
String result;
switch (status) {
    case PENDING:
        result = "Processing";
        break;
    case COMPLETED:
        result = "Done";
        break;
    default:
        result = "Unknown";
}

// After
String result = switch (status) {
    case PENDING -> "Processing";
    case COMPLETED -> "Done";
    default -> "Unknown";
};
```

### 4.4 Virtual Threads (Optional - Experimental)
Update application.properties:
```properties
spring.threads.virtual.enabled=true
```

Or configure explicitly:
```java
@Configuration
public class VirtualThreadConfig {
    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> {
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        };
    }
}
```

---

## Phase 5: Update Spring Boot 3.x Configurations (Day 9)

### 5.1 Security Configuration
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
```

### 5.2 Update application.properties
```properties
# Spring Boot 3.x property changes
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Logging
logging.level.org.springframework=INFO
logging.level.com.shopizer=DEBUG

# SpringDoc OpenAPI
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
```

---

## Phase 6: Build & Fix Compilation Errors (Day 10)

### 6.1 Iterative Compilation
```bash
# Compile each module
mvnw clean compile -pl sm-core-model
mvnw clean compile -pl sm-core-modules
mvnw clean compile -pl sm-core
mvnw clean compile -pl sm-shop-model
mvnw clean compile -pl sm-shop

# Full build
mvnw clean install
```

### 6.2 Common Issues & Fixes
- **HttpClient changes**: Update to HttpClient 5.x API
- **Hibernate 6.x changes**: Update entity mappings
- **Spring Security 6.x**: Update filter chain configuration
- **Removed APIs**: Replace SecurityManager, finalization

---

## Phase 7: Docker & CI/CD (Day 11)

### 7.1 Update Dockerfile
```dockerfile
FROM eclipse-temurin:21-jre-alpine
RUN mkdir /opt/app && mkdir /files
COPY target/shopizer.jar /opt/app/
COPY SALESMANAGER.h2.db /
COPY ./files /files
EXPOSE 8080
CMD ["java", "-XX:+UseZGC", "-XX:+UseStringDeduplication", "-jar", "/opt/app/shopizer.jar"]
```

### 7.2 Build Multi-Architecture Images
```bash
docker buildx create --use
docker buildx build --platform linux/amd64,linux/arm64 -t shopizer:java21 .
```

### 7.3 Update CI/CD
```yaml
# .circleci/config.yml
version: 2.1
jobs:
  build:
    docker:
      - image: cimg/openjdk:21.0
    steps:
      - checkout
      - run: ./mvnw clean verify
```

---

## Phase 8: Testing & Performance Validation (Days 12-13)

### 8.1 Automated Testing
```bash
mvnw clean verify
mvnw test -Dtest=**/*Test
```

### 8.2 Performance Benchmarking
```bash
# Startup time
time mvnw spring-boot:run

# Load testing with Apache Bench
ab -n 1000 -c 10 http://localhost:8080/api/products

# Memory profiling
java -XX:+PrintFlagsFinal -jar target/shopizer.jar | grep -i heap
```

### 8.3 API Testing
- Test all endpoints via Swagger UI: http://localhost:8080/swagger-ui.html
- Run Postman/Newman test collections
- Verify database operations

---

## Phase 9: Documentation & Deployment (Day 14)

### 9.1 Update Documentation
```markdown
# README.md updates
- Java 21 requirement
- New Swagger UI path
- Updated build commands
- Docker instructions
```

### 9.2 Deploy
```bash
mvnw clean package -DskipTests
docker build -t shopizer:java21-prod .
docker push shopizer:java21-prod
```

---

## Success Criteria

- [ ] All modules compile with Java 21
- [ ] Spring Boot 3.3.x running
- [ ] All tests passing
- [ ] SpringDoc OpenAPI working
- [ ] Docker image builds
- [ ] Performance equal or better than Java 17
- [ ] Virtual threads enabled (optional)
- [ ] Modern Java 21 features utilized

---

## Rollback Strategy

```bash
git revert <migration-commits>
# Or
git checkout main
mvnw clean package
```

---

## Benefits of This Approach

1. **Latest features**: Java 21 LTS, Spring Boot 3.3.x
2. **Performance**: Virtual threads, ZGC improvements
3. **Modern code**: Records, pattern matching, switch expressions
4. **Security**: Latest security patches
5. **Future-proof**: Built on latest stable versions
