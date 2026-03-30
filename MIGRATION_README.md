# Java 21 Migration Guide

This guide provides two distinct approaches for migrating Shopizer from Java 17 to Java 21.

## Current State

- **Java Version**: 17
- **Spring Boot**: 2.5.12
- **Version**: 3.2.5
- **Modules**: sm-core-model, sm-core-modules, sm-core, sm-shop-model, sm-shop

## Available Migration Plans

### Fast Modernization Approach
**File**: `MIGRATION_PLAN_FAST_MODERNIZATION.md`

Fast-paced migration with full modernization and latest features.

**Timeline**: 1-2 weeks  
**Risk Level**: Medium-High  
**Rollback**: Moderate difficulty

**Choose this if:**
- You want to leverage Java 21 features immediately (records, pattern matching, virtual threads)
- You can afford some downtime for testing
- Your team is comfortable with rapid changes
- You want to upgrade to Spring Boot 3.3.x and latest dependencies
- You have good test coverage

**Key Activities:**
- Bulk dependency updates to latest versions
- Automated migration using OpenRewrite
- Implement Java 21 features (records, pattern matching, switch expressions)
- Enable virtual threads
- Migrate to SpringDoc OpenAPI
- Update to Spring Boot 3.3.x

---

### Zero-Downtime Approach
**File**: `MIGRATION_PLAN_ZERO_DOWNTIME.md`

Zero-downtime migration with parallel environments and gradual rollout.

**Timeline**: 4-6 weeks  
**Risk Level**: Very Low  
**Rollback**: Instant

**Choose this if:**
- You require zero downtime
- You're migrating a production system
- You need comprehensive validation before committing
- You want the ability to instantly rollback
- You prefer gradual traffic shifting (canary deployment)
- You need to maintain both versions temporarily

**Key Activities:**
- Maintain Java 17 and Java 21 environments simultaneously
- Module-by-module incremental updates
- Parallel testing and comparison
- Canary deployment with gradual traffic shift (10% → 25% → 50% → 75% → 100%)
- Comprehensive monitoring and validation
- Blue-green deployment strategy

---

## Quick Comparison

| Factor | Fast Modernization | Zero-Downtime |
|--------|-------------------|-------------------|
| **Duration** | 1-2 weeks | 4-6 weeks |
| **Downtime** | Some required | Zero |
| **Risk** | Medium-High | Very Low |
| **Modernization** | Maximum | Minimal |
| **Rollback Time** | Hours | Seconds |
| **Testing Approach** | Rapid iteration | Exhaustive validation |
| **Production Ready** | After full testing | Gradual rollout |
| **Team Size** | Small, agile | Any size |
| **Best For** | New features | Production stability |

---

## Prerequisites (Both Plans)

### 1. Install Java 21
```bash
# macOS
brew install openjdk@21

# Verify
java -version
```

### 2. Backup Current State
```bash
# Create backup branch
git checkout -b backup/java-17-baseline
git push origin backup/java-17-baseline

# Return to main
git checkout main
```

### 3. Document Baseline
```bash
# Current dependencies
mvnw dependency:tree > baseline-dependencies.txt

# Current test results
mvnw clean test > baseline-tests.txt
```

---

## Getting Started

### For Fast Modernization:
```bash
# 1. Create branch
git checkout -b feature/java-21-fast-modernization

# 2. Follow MIGRATION_PLAN_FAST_MODERNIZATION.md
# Start with Phase 1: Rapid Environment Setup

# 3. Quick validation
mvnw clean install
mvnw spring-boot:run
```

### For Zero-Downtime:
```bash
# 1. Create branch
git checkout -b feature/java-21-zero-downtime

# 2. Set up migration tracking
mkdir -p migration-logs/{baseline,java21}

# 3. Follow MIGRATION_PLAN_ZERO_DOWNTIME.md
# Start with Phase 1: Preparation & Baseline

# 4. Establish baseline
mvnw clean test > migration-logs/baseline/test-results.txt
```

---

## Key Migration Changes (Both Plans)

### 1. Java Version
```xml
<!-- pom.xml -->
<java.version>21</java.version>
```

### 2. Spring Boot Upgrade
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version>  <!-- Plan 3: Conservative -->
    <version>3.3.0</version>  <!-- Plan 2: Latest -->
</parent>
```

### 3. Namespace Migration
```bash
# javax.* → jakarta.*
find . -name "*.java" -type f -exec sed -i '' 's/import javax\./import jakarta./g' {} +
```

### 4. Swagger → SpringDoc OpenAPI
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

### 5. Docker Update
```dockerfile
FROM eclipse-temurin:21-jre-alpine
```

---

## Testing Strategy

### Fast Modernization:
- Rapid iteration with automated tests
- Fix issues as they arise
- Full regression testing at end
- Performance benchmarking

### Zero-Downtime:
- Side-by-side comparison (Java 17 vs 21)
- Identical API responses validation
- Load testing comparison
- Gradual production rollout

---

## Rollback Procedures

### Fast Modernization:
```bash
# Revert commits
git revert <migration-commits>

# Or reset to baseline
git checkout backup/java-17-baseline
mvnw clean package
```

### Zero-Downtime:
```bash
# Instant rollback - just switch traffic
# Java 17 environment still running

# Update load balancer to route to Java 17
# Or stop Java 21 container, start Java 17 container
docker stop shopizer-java21
docker start shopizer-java17
```

---

## Success Criteria

Both plans must meet:

- [ ] All modules compile with Java 21
- [ ] All tests pass (unit + integration)
- [ ] Application starts without errors
- [ ] All REST APIs functional via Swagger UI
- [ ] Docker image builds and runs
- [ ] No critical performance regressions
- [ ] Documentation updated
- [ ] CI/CD pipeline updated

**Additional for Zero-Downtime:**
- [ ] Parallel environments validated
- [ ] Canary deployment successful
- [ ] Zero production incidents
- [ ] Rollback procedure tested

---

## Support & Troubleshooting

### Common Issues

**Issue**: Compilation errors with javax imports
```bash
# Solution: Replace with jakarta
find . -name "*.java" -exec sed -i '' 's/javax\.persistence/jakarta.persistence/g' {} +
```

**Issue**: Spring Security configuration errors
```java
// Update to Spring Security 6.x pattern
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .build();
}
```

**Issue**: Swagger UI not accessible
```properties
# Add to application.properties
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## Post-Migration

### Verify Installation
```bash
# Check Java version
java -version  # Should show 21

# Build project
mvnw clean install

# Run application
mvnw spring-boot:run

# Access Swagger UI
open http://localhost:8080/swagger-ui.html
```

### Performance Validation
```bash
# Startup time
time mvnw spring-boot:run

# Load testing
ab -n 1000 -c 50 http://localhost:8080/api/products

# Memory usage
jcmd <pid> VM.native_memory summary
```

### Update Documentation
- [ ] Update README.md with Java 21 requirement
- [ ] Update build instructions
- [ ] Update Docker documentation
- [ ] Document any breaking changes
- [ ] Update CI/CD documentation

---

## Decision Matrix

Use this to choose your migration plan:

```
                                      Fast      Zero-
                                  Modernization Downtime
┌─────────────────────────────────────────────────────┐
│ Production System                    ✗          ✓   │
│ Development/Staging                  ✓          ✓   │
│ Zero Downtime Required               ✗          ✓   │
│ Fast Migration Needed                ✓          ✗   │
│ Want Latest Features                 ✓          ✗   │
│ Risk Averse                          ✗          ✓   │
│ Small Team                           ✓          ✗   │
│ Need Instant Rollback                ✗          ✓   │
│ Comprehensive Testing                ✗          ✓   │
│ Canary Deployment                    ✗          ✓   │
└─────────────────────────────────────────────────────┘
```

---

## Timeline Overview

### Fast Modernization (1-2 weeks)
```
Week 1:
├── Day 1: Environment + Bulk Config Updates
├── Day 2-3: Bulk Dependency Updates
├── Day 4-6: Mass Code Migration
├── Day 7-8: Modernize with Java 21 Features
├── Day 9: Spring Boot 3.x Configurations
└── Day 10: Build & Fix Compilation

Week 2:
├── Day 11: Docker & CI/CD
├── Day 12-13: Testing & Performance
└── Day 14: Documentation & Deployment
```

### Zero-Downtime (4-6 weeks)
```
Week 1: Preparation & Baseline
Week 2: Minimal Version Bump
Week 3: Incremental Dependency Updates
Week 4: Code Migration
Week 5: Parallel Testing + Docker
Week 6: Staged Rollout + Validation
Week 6+: Production Deployment
```

---

## Next Steps

1. **Review both plans** in detail
2. **Choose the plan** that fits your requirements
3. **Get team alignment** on approach and timeline
4. **Schedule migration** window (if needed)
5. **Begin migration** following chosen plan
6. **Track progress** using checklists in each plan
7. **Document issues** and resolutions
8. **Validate success** against criteria

---

## Questions?

- Review detailed plan documents for step-by-step instructions
- Check troubleshooting section for common issues
- Ensure all prerequisites are met before starting
- Consider running a pilot migration on a non-critical module first

**Good luck with your migration!** 🚀
