# Java 21 Migration Plan 3: Risk-Minimized Parallel Testing Approach

**Strategy**: Maintain parallel environments, incremental validation, zero-downtime migration capability.

**Timeline**: 4-6 weeks  
**Risk Level**: Very Low  
**Rollback Difficulty**: Very Easy

---

## Phase 1: Preparation & Baseline (Week 1)

### 1.1 Create Isolated Migration Environment
```bash
# Create feature branch
git checkout -b feature/java-21-parallel-testing

# Create migration tracking directory
mkdir -p migration-logs
mkdir -p migration-logs/baseline
mkdir -p migration-logs/java21
```

### 1.2 Establish Java 17 Baseline
```bash
# Document current state
mvnw clean test > migration-logs/baseline/test-results.txt
mvnw dependency:tree > migration-logs/baseline/dependencies.txt
mvnw clean package
java -jar sm-shop/target/shopizer.jar &
sleep 30

# Performance baseline
curl -w "@curl-format.txt" -o /dev/null -s http://localhost:8080/api/health
ab -n 100 -c 10 http://localhost:8080/api/products > migration-logs/baseline/performance.txt

# Stop baseline
pkill -f shopizer.jar
```

Create `curl-format.txt`:
```
time_namelookup:  %{time_namelookup}\n
time_connect:  %{time_connect}\n
time_appconnect:  %{time_appconnect}\n
time_pretransfer:  %{time_pretransfer}\n
time_redirect:  %{time_redirect}\n
time_starttransfer:  %{time_starttransfer}\n
time_total:  %{time_total}\n
```

### 1.3 Install Java 21 (Parallel to Java 17)
```bash
# Install Java 21
brew install openjdk@21

# Keep both versions available
/usr/libexec/java_home -V

# Create shell aliases for easy switching
echo 'alias java17="export JAVA_HOME=$(/usr/libexec/java_home -v 17)"' >> ~/.zshrc
echo 'alias java21="export JAVA_HOME=$(/usr/libexec/java_home -v 21)"' >> ~/.zshrc
source ~/.zshrc
```

### 1.4 Create Migration Checklist
```bash
cat > migration-logs/CHECKLIST.md << 'EOF'
# Java 21 Migration Checklist

## Module: sm-core-model
- [ ] Compiles with Java 21
- [ ] Tests pass
- [ ] No deprecated API warnings
- [ ] Performance validated

## Module: sm-core-modules
- [ ] Compiles with Java 21
- [ ] Tests pass
- [ ] No deprecated API warnings
- [ ] Performance validated

## Module: sm-core
- [ ] Compiles with Java 21
- [ ] Tests pass
- [ ] No deprecated API warnings
- [ ] Performance validated

## Module: sm-shop-model
- [ ] Compiles with Java 21
- [ ] Tests pass
- [ ] No deprecated API warnings
- [ ] Performance validated

## Module: sm-shop
- [ ] Compiles with Java 21
- [ ] Tests pass
- [ ] Application starts
- [ ] All APIs functional
- [ ] Performance validated
- [ ] Docker image builds
- [ ] Docker container runs

## Integration
- [ ] Full build successful
- [ ] All integration tests pass
- [ ] Load testing passed
- [ ] Security scan passed
- [ ] Documentation updated
EOF
```

---

## Phase 2: Minimal Version Bump (Week 2)

### 2.1 Update Only Java Version
```bash
# Switch to Java 21
java21
java -version
```

Update `pom.xml` (minimal change):
```xml
<!-- Line 37 - ONLY change this -->
<java.version>21</java.version>
```

### 2.2 Attempt Build with Existing Dependencies
```bash
# Try building with Java 21, Spring Boot 2.5.12
mvnw clean compile 2>&1 | tee migration-logs/java21/first-compile-attempt.txt
```

### 2.3 Document Compilation Errors
```bash
# Analyze errors
grep -i "error" migration-logs/java21/first-compile-attempt.txt > migration-logs/java21/errors-summary.txt

# Categorize errors
# - Dependency incompatibilities
# - API changes
# - Compilation failures
```

### 2.4 Create Migration Log Template
```bash
cat > migration-logs/MIGRATION_LOG.md << 'EOF'
# Migration Log

## Date: [DATE]
### Changes Made:
- 

### Build Status:
- [ ] Compiles
- [ ] Tests pass

### Issues Found:
- 

### Resolution:
- 

### Next Steps:
- 
EOF
```

---

## Phase 3: Incremental Dependency Updates (Week 3)

### 3.1 Update Spring Boot (Conservative Version)
```xml
<!-- Update parent to first Java 21 compatible version -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version>  <!-- First stable 3.x with Java 21 -->
</parent>
```

### 3.2 Module-by-Module Compilation

#### Step 1: sm-core-model
```bash
mvnw clean compile -pl sm-core-model 2>&1 | tee migration-logs/java21/sm-core-model-compile.txt

# If fails, update only necessary dependencies in sm-core-model/pom.xml
# Document each change in migration log
```

#### Step 2: sm-core-modules
```bash
mvnw clean compile -pl sm-core-modules 2>&1 | tee migration-logs/java21/sm-core-modules-compile.txt
```

#### Step 3: sm-core
```bash
mvnw clean compile -pl sm-core 2>&1 | tee migration-logs/java21/sm-core-compile.txt
```

#### Step 4: sm-shop-model
```bash
mvnw clean compile -pl sm-shop-model 2>&1 | tee migration-logs/java21/sm-shop-model-compile.txt
```

#### Step 5: sm-shop
```bash
mvnw clean compile -pl sm-shop 2>&1 | tee migration-logs/java21/sm-shop-compile.txt
```

### 3.3 Track Each Dependency Change
Create `migration-logs/DEPENDENCY_CHANGES.md`:
```markdown
# Dependency Changes

| Dependency | Old Version | New Version | Reason | Module | Date |
|------------|-------------|-------------|--------|--------|------|
| spring-boot | 2.5.12 | 3.2.0 | Java 21 support | root | 2026-03-31 |
```

---

## Phase 4: Code Migration (Week 4)

### 4.1 Namespace Migration (javax → jakarta)
```bash
# Create backup
git add -A
git commit -m "Pre-namespace migration checkpoint"

# Migrate one module at a time
# sm-core-model
find sm-core-model -name "*.java" -type f -exec sed -i '' 's/import javax\.persistence/import jakarta.persistence/g' {} +
mvnw clean test -pl sm-core-model

# sm-core-modules
find sm-core-modules -name "*.java" -type f -exec sed -i '' 's/import javax\./import jakarta./g' {} +
mvnw clean test -pl sm-core-modules

# Continue for each module...
```

### 4.2 Fix Compilation Errors Incrementally
```bash
# For each module:
# 1. Compile
# 2. Fix errors
# 3. Test
# 4. Commit
# 5. Move to next module

mvnw clean compile -pl sm-core-model
# Fix errors
mvnw clean test -pl sm-core-model
git add sm-core-model
git commit -m "Migrate sm-core-model to Java 21"
```

### 4.3 Update Configuration Classes
Create feature flags for new code:
```java
@Configuration
public class Java21Config {
    
    @Value("${app.java21.features.enabled:false}")
    private boolean java21FeaturesEnabled;
    
    // Conditional bean registration
    @Bean
    @ConditionalOnProperty(name = "app.java21.features.enabled", havingValue = "true")
    public VirtualThreadExecutor virtualThreadExecutor() {
        return new VirtualThreadExecutor();
    }
}
```

---

## Phase 5: Parallel Testing (Week 5)

### 5.1 Set Up Parallel Environments

#### Java 17 Environment
```bash
# Terminal 1
java17
cd /path/to/shopizer
git checkout main
mvnw clean package
java -jar sm-shop/target/shopizer.jar --server.port=8080
```

#### Java 21 Environment
```bash
# Terminal 2
java21
cd /path/to/shopizer
git checkout feature/java-21-parallel-testing
mvnw clean package
java -jar sm-shop/target/shopizer.jar --server.port=8081
```

### 5.2 Comparative Testing Script
```bash
cat > migration-logs/compare-environments.sh << 'EOF'
#!/bin/bash

echo "=== Comparative Testing: Java 17 vs Java 21 ==="

# Test endpoints
ENDPOINTS=(
    "/api/products"
    "/api/categories"
    "/api/cart"
    "/api/customer"
)

for endpoint in "${ENDPOINTS[@]}"; do
    echo "Testing: $endpoint"
    
    echo "Java 17 (port 8080):"
    curl -w "@curl-format.txt" -o /dev/null -s "http://localhost:8080$endpoint"
    
    echo "Java 21 (port 8081):"
    curl -w "@curl-format.txt" -o /dev/null -s "http://localhost:8081$endpoint"
    
    echo "---"
done
EOF

chmod +x migration-logs/compare-environments.sh
./migration-logs/compare-environments.sh > migration-logs/java21/comparison-results.txt
```

### 5.3 Automated Comparison Tests
```bash
# Create test suite that hits both environments
cat > migration-logs/parallel-test.sh << 'EOF'
#!/bin/bash

JAVA17_URL="http://localhost:8080"
JAVA21_URL="http://localhost:8081"

# Test all major APIs
echo "Testing Products API..."
diff <(curl -s "$JAVA17_URL/api/products" | jq -S .) \
     <(curl -s "$JAVA21_URL/api/products" | jq -S .)

echo "Testing Categories API..."
diff <(curl -s "$JAVA17_URL/api/categories" | jq -S .) \
     <(curl -s "$JAVA21_URL/api/categories" | jq -S .)

# Add more API tests...
EOF

chmod +x migration-logs/parallel-test.sh
```

### 5.4 Load Testing Comparison
```bash
# Java 17
ab -n 1000 -c 50 http://localhost:8080/api/products > migration-logs/baseline/load-test.txt

# Java 21
ab -n 1000 -c 50 http://localhost:8081/api/products > migration-logs/java21/load-test.txt

# Compare results
echo "=== Load Test Comparison ===" > migration-logs/java21/load-comparison.txt
echo "Java 17:" >> migration-logs/java21/load-comparison.txt
grep "Requests per second" migration-logs/baseline/load-test.txt >> migration-logs/java21/load-comparison.txt
echo "Java 21:" >> migration-logs/java21/load-comparison.txt
grep "Requests per second" migration-logs/java21/load-test.txt >> migration-logs/java21/load-comparison.txt
```

---

## Phase 6: Docker Parallel Testing (Week 5)

### 6.1 Build Both Docker Images
```bash
# Java 17 image
git checkout main
cd sm-shop
mvnw clean package
docker build -t shopizer:java17 .

# Java 21 image
git checkout feature/java-21-parallel-testing
cd sm-shop
mvnw clean package
docker build -t shopizer:java21 .
```

### 6.2 Run Containers Side-by-Side
```bash
# Java 17 container
docker run -d --name shopizer-java17 -p 8080:8080 shopizer:java17

# Java 21 container
docker run -d --name shopizer-java21 -p 8081:8080 shopizer:java21

# Monitor both
docker logs -f shopizer-java17 &
docker logs -f shopizer-java21 &
```

### 6.3 Container Health Checks
```bash
cat > migration-logs/docker-health-check.sh << 'EOF'
#!/bin/bash

echo "=== Docker Health Check ==="

echo "Java 17 Container:"
docker exec shopizer-java17 curl -s http://localhost:8080/actuator/health | jq .

echo "Java 21 Container:"
docker exec shopizer-java21 curl -s http://localhost:8080/actuator/health | jq .

echo "Memory Usage:"
docker stats --no-stream shopizer-java17 shopizer-java21
EOF

chmod +x migration-logs/docker-health-check.sh
```

---

## Phase 7: Staged Rollout (Week 6)

### 7.1 Deploy to Dev Environment
```bash
# Deploy Java 21 version to dev
mvnw clean package -DskipTests
scp sm-shop/target/shopizer.jar dev-server:/opt/shopizer/shopizer-java21.jar

# Keep Java 17 version running
# Start Java 21 on different port
ssh dev-server "java -jar /opt/shopizer/shopizer-java21.jar --server.port=8081 &"
```

### 7.2 Canary Deployment Strategy
```nginx
# nginx.conf - Route 10% traffic to Java 21
upstream shopizer_backend {
    server localhost:8080 weight=9;  # Java 17
    server localhost:8081 weight=1;  # Java 21
}
```

### 7.3 Gradual Traffic Shift
```bash
# Week 6 Day 1: 10% traffic
# Week 6 Day 2: 25% traffic
# Week 6 Day 3: 50% traffic
# Week 6 Day 4: 75% traffic
# Week 6 Day 5: 100% traffic

# Monitor error rates at each stage
# Rollback if error rate increases >5%
```

### 7.4 Monitoring & Alerting
```bash
# Set up monitoring
cat > migration-logs/monitor.sh << 'EOF'
#!/bin/bash

while true; do
    echo "=== $(date) ==="
    
    # Check both versions
    echo "Java 17 Status:"
    curl -s http://localhost:8080/actuator/health | jq .status
    
    echo "Java 21 Status:"
    curl -s http://localhost:8081/actuator/health | jq .status
    
    # Check error logs
    echo "Java 17 Errors (last 5 min):"
    docker logs --since 5m shopizer-java17 2>&1 | grep -i error | wc -l
    
    echo "Java 21 Errors (last 5 min):"
    docker logs --since 5m shopizer-java21 2>&1 | grep -i error | wc -l
    
    sleep 300  # Check every 5 minutes
done
EOF

chmod +x migration-logs/monitor.sh
./migration-logs/monitor.sh > migration-logs/monitoring.log &
```

---

## Phase 8: Validation & Documentation (Week 6)

### 8.1 Final Validation Checklist
```bash
cat > migration-logs/FINAL_VALIDATION.md << 'EOF'
# Final Validation Checklist

## Build & Compilation
- [ ] All modules compile without errors
- [ ] No deprecation warnings
- [ ] Maven build successful: `mvnw clean install`

## Testing
- [ ] All unit tests pass
- [ ] All integration tests pass
- [ ] Manual API testing complete
- [ ] Load testing shows acceptable performance

## Docker
- [ ] Docker image builds successfully
- [ ] Container starts without errors
- [ ] Container health checks pass
- [ ] Memory usage within acceptable limits

## Functional Testing
- [ ] Swagger UI accessible
- [ ] All REST APIs functional
- [ ] Database operations working
- [ ] Cache (Infinispan) working
- [ ] Elasticsearch integration working
- [ ] File uploads working

## Performance
- [ ] Startup time: Java 21 <= Java 17 + 10%
- [ ] API response time: Java 21 <= Java 17 + 5%
- [ ] Memory usage: Java 21 <= Java 17 + 10%
- [ ] Throughput: Java 21 >= Java 17 - 5%

## Security
- [ ] Security scan passed
- [ ] No new vulnerabilities introduced
- [ ] Authentication working
- [ ] Authorization working

## Documentation
- [ ] README.md updated
- [ ] Migration guide created
- [ ] Rollback procedure documented
- [ ] Known issues documented
EOF
```

### 8.2 Create Rollback Procedure
```bash
cat > migration-logs/ROLLBACK_PROCEDURE.md << 'EOF'
# Rollback Procedure

## Immediate Rollback (< 5 minutes)

### Option 1: Switch Docker Container
```bash
docker stop shopizer-java21
docker start shopizer-java17
# Update load balancer to point to port 8080
```

### Option 2: Git Revert
```bash
git checkout main
mvnw clean package -DskipTests
docker build -t shopizer:rollback .
docker run -d -p 8080:8080 shopizer:rollback
```

### Option 3: Keep Both Running
```bash
# Just update nginx/load balancer to route 100% to Java 17
# No deployment needed
```

## Verification After Rollback
- [ ] Application accessible
- [ ] APIs responding
- [ ] No errors in logs
- [ ] Database connections working

## Post-Rollback Analysis
1. Collect logs from Java 21 deployment
2. Analyze failure cause
3. Document issues
4. Plan remediation
5. Schedule retry
EOF
```

### 8.3 Update Project Documentation
```bash
# Update README.md
cat >> README.md << 'EOF'

## Java Version Support

This project supports Java 17 and Java 21.

### Building with Java 21
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
mvnw clean install
```

### Migration Notes
See `migration-logs/` directory for detailed migration documentation.
EOF
```

---

## Phase 9: Production Deployment (Post Week 6)

### 9.1 Pre-Production Checklist
```bash
# Run full validation
./migration-logs/final-validation.sh

# Backup production database
pg_dump shopizer_prod > backups/shopizer_prod_$(date +%Y%m%d).sql

# Create deployment package
mvnw clean package -Pprod
```

### 9.2 Blue-Green Deployment
```bash
# Green environment (Java 21)
# Deploy to green
# Run smoke tests
# Switch traffic
# Monitor
# Keep blue (Java 17) running for 24 hours
```

### 9.3 Post-Deployment Monitoring (24 hours)
```bash
# Monitor key metrics
# - Error rate
# - Response time
# - Memory usage
# - CPU usage
# - Database connections

# If any metric degrades >10%, rollback immediately
```

---

## Success Criteria

- [ ] Zero downtime during migration
- [ ] All functionality working in Java 21
- [ ] Performance within 5% of Java 17 baseline
- [ ] No increase in error rates
- [ ] Rollback procedure tested and documented
- [ ] Both Java 17 and Java 21 versions maintained for 1 month

---

## Risk Mitigation Strategies

1. **Parallel Environments**: Always maintain working Java 17 version
2. **Incremental Changes**: One module at a time
3. **Comprehensive Testing**: Test after every change
4. **Feature Flags**: Enable/disable Java 21 features
5. **Canary Deployment**: Gradual traffic shift
6. **Quick Rollback**: Multiple rollback options available
7. **Monitoring**: Continuous monitoring during migration
8. **Documentation**: Every change documented

---

## Timeline Summary

| Week | Phase | Activities |
|------|-------|------------|
| 1 | Preparation | Baseline, install Java 21, create checklists |
| 2 | Minimal Bump | Update Java version, attempt build |
| 3 | Dependencies | Update Spring Boot, fix modules one by one |
| 4 | Code Migration | Namespace changes, fix compilation errors |
| 5 | Parallel Testing | Run both versions, comparative testing |
| 6 | Staged Rollout | Canary deployment, gradual traffic shift |
| 6+ | Production | Full deployment with monitoring |

---

## Cost-Benefit Analysis

### Costs
- 6 weeks development time
- Additional infrastructure for parallel testing
- Extensive testing effort

### Benefits
- Zero risk of production outage
- Ability to rollback instantly
- Comprehensive validation
- Knowledge transfer through documentation
- Confidence in migration success
