# Shopizer - Developer Onboarding Guide

Welcome to Shopizer! This guide will help you get up to speed quickly with the codebase.

## 📚 Documentation Overview

We've created comprehensive documentation to help you understand Shopizer:

### 1. [Business Context](./ONBOARDING_BUSINESS_CONTEXT.md)
**Read this first!** Understand the e-commerce domain and business logic.

**What you'll learn:**
- Core business entities (Products, Orders, Customers, etc.)
- Business workflows (checkout, order fulfillment)
- Multi-tenancy and internationalization
- Integration points and business rules

**Time to read**: 20-30 minutes

### 2. [Features & Test Coverage](./ONBOARDING_FEATURES_AND_TESTS.md)
**Essential for understanding what's already built.**

**What you'll learn:**
- Complete feature inventory
- Test coverage analysis (37 test files)
- API endpoints for each feature
- Test coverage gaps and improvement areas
- How to run tests

**Time to read**: 30-40 minutes

### 3. [Technical Architecture](./ONBOARDING_TECHNICAL_ARCHITECTURE.md)
**Deep dive into the technical implementation.**

**What you'll learn:**
- System architecture and module structure
- Design patterns used
- Data flow and request lifecycle
- Configuration and deployment
- Development workflow
- Extensibility points

**Time to read**: 40-50 minutes

## 🚀 Quick Start (30 Minutes)

### Step 1: Setup (10 min)
```bash
# Clone the repository
git clone https://github.com/shopizer-ecommerce/shopizer.git
cd shopizer

# Build the project
./mvnw clean install

# Run the application
cd sm-shop
./mvnw spring-boot:run
```

Access the API: http://localhost:8080/swagger-ui.html

### Step 2: Explore the API (10 min)
Open Swagger UI and try these endpoints:
1. **GET** `/api/v1/products` - List products
2. **GET** `/api/v1/categories` - List categories
3. **POST** `/api/v1/cart` - Create a shopping cart
4. **GET** `/api/v1/store/DEFAULT` - Get default store info

### Step 3: Run Tests (10 min)
```bash
# Run all tests
./mvnw test

# Run specific test
./mvnw test -Dtest=ProductTest

# Run integration tests
cd sm-shop
./mvnw test
```

## 📖 Learning Path

### Week 1: Foundation
**Goal**: Understand the business domain and architecture

- [ ] Read [Business Context](./ONBOARDING_BUSINESS_CONTEXT.md)
- [ ] Read [Technical Architecture](./ONBOARDING_TECHNICAL_ARCHITECTURE.md)
- [ ] Explore the codebase structure
- [ ] Run the application locally
- [ ] Explore Swagger UI and test APIs
- [ ] Run existing tests

**Hands-on Exercise**: Create a simple product via API and retrieve it

### Week 2: Deep Dive
**Goal**: Understand existing features and code patterns

- [ ] Read [Features & Test Coverage](./ONBOARDING_FEATURES_AND_TESTS.md)
- [ ] Study a complete feature (e.g., Product Management)
  - Domain model: `Product.java`
  - Repository: `ProductRepository.java`
  - Service: `ProductServiceImpl.java`
  - Facade: `ProductFacadeImpl.java`
  - API: `ProductApi.java`
  - Tests: `ProductTest.java`, `ProductManagementAPIIntegrationTest.java`
- [ ] Understand the request flow
- [ ] Study the test patterns

**Hands-on Exercise**: Add a new field to Product and expose it via API

### Week 3: Contribution
**Goal**: Make your first contribution

- [ ] Pick a feature to enhance or bug to fix
- [ ] Write tests first (TDD approach)
- [ ] Implement the feature
- [ ] Create a pull request

**Suggested First Tasks**:
- Add missing tests for payment processing
- Improve error handling in a specific API
- Add validation to an existing endpoint
- Enhance documentation

## 🏗️ Codebase Structure

```
shopizer/
├── sm-core-model/          # Domain entities (JPA)
│   └── src/main/java/com/salesmanager/core/model/
│       ├── catalog/        # Product, Category, Manufacturer
│       ├── order/          # Order, OrderProduct
│       ├── customer/       # Customer
│       └── merchant/       # MerchantStore
│
├── sm-core/                # Business logic
│   └── src/main/java/com/salesmanager/core/business/
│       ├── services/       # Service layer
│       ├── repositories/   # Data access
│       └── modules/        # Integration modules
│
├── sm-shop-model/          # API DTOs
│   └── src/main/java/com/salesmanager/shop/model/
│       ├── catalog/        # Product DTOs
│       ├── order/          # Order DTOs
│       └── customer/       # Customer DTOs
│
└── sm-shop/                # REST API
    └── src/main/java/com/salesmanager/shop/
        ├── store/api/      # REST controllers
        ├── store/controller/facade/  # Business facades
        └── mapper/         # Entity ↔ DTO mappers
```

## 🔑 Key Concepts

### 1. Multi-Store Architecture
- One installation can serve multiple stores
- Each store has its own products, customers, orders
- Store identified by code (e.g., "DEFAULT")

### 2. Multi-Language Support
- Entities have `*Description` classes for translations
- Example: `Product` has `ProductDescription` for each language
- Always pass `Language` parameter to services

### 3. Request Flow
```
HTTP Request → Controller → Facade → Service → Repository → Database
                    ↓           ↓        ↓
                  DTO      Mapper   Entity
```

### 4. DTO Pattern
- **Persistable\***: Input DTOs (create/update)
- **Readable\***: Output DTOs (read)
- **Entity**: Domain model (JPA)

### 5. Facade Pattern
- Facades orchestrate multiple services
- Handle DTO ↔ Entity conversion
- Provide simplified API for controllers

## 🧪 Test Coverage Summary

### Well-Tested Features ✅
- Product management (core + API)
- Category management
- Order processing
- Shopping cart
- Customer management
- Shipping calculations
- Content management
- Search functionality

### Needs More Tests ⚠️
- Payment processing
- Security flows
- Email templates
- Multi-currency
- Performance testing

### How to Add Tests
1. **Unit Tests**: Test business logic in isolation
   - Location: `sm-core/src/test/java`
   - Example: `ProductTest.java`

2. **Integration Tests**: Test API endpoints
   - Location: `sm-shop/src/test/java`
   - Example: `ProductManagementAPIIntegrationTest.java`

## 🛠️ Development Tips

### 1. Database
- **Development**: H2 in-memory (default)
- **Production**: MySQL
- **Configuration**: `application.properties`

### 2. Debugging
```properties
# Enable SQL logging
spring.jpa.show-sql=true

# Enable debug logging
logging.level.com.salesmanager=DEBUG
```

### 3. API Testing
- Use Swagger UI: http://localhost:8080/swagger-ui.html
- Use Postman or curl for manual testing
- Write integration tests for automated testing

### 4. Common Patterns

**Creating a new entity:**
```java
// 1. Create entity in sm-core-model
@Entity
public class MyEntity extends SalesManagerEntity<Long, MyEntity> {
    // fields, getters, setters
}

// 2. Create repository in sm-core
public interface MyEntityRepository extends JpaRepository<MyEntity, Long> {
}

// 3. Create service in sm-core
@Service
public class MyEntityServiceImpl implements MyEntityService {
    @Autowired
    private MyEntityRepository repository;
    // business logic
}

// 4. Create DTOs in sm-shop-model
public class PersistableMyEntity { /* input DTO */ }
public class ReadableMyEntity { /* output DTO */ }

// 5. Create facade in sm-shop
@Service
public class MyEntityFacadeImpl implements MyEntityFacade {
    @Autowired
    private MyEntityService service;
    // orchestration logic
}

// 6. Create API in sm-shop
@RestController
@RequestMapping("/api/v1/myentity")
public class MyEntityApi {
    @Autowired
    private MyEntityFacade facade;
    // REST endpoints
}
```

## 📊 Test Coverage Analysis

Based on our analysis of 37 test files:

### By Module
- **sm-core tests**: 22 files (business logic)
- **sm-shop tests**: 15 files (API integration)

### By Feature
- **Catalog**: 4 test files (Product, Category, Manufacturer)
- **Orders**: 2 test files (Order, Invoice)
- **Shopping Cart**: 2 test files (Core + API)
- **Customer**: 2 test files (Core + API)
- **Shipping**: 3 test files (Methods, Weight, Distance)
- **Content**: 3 test files (Static, Folders, Images)
- **Search**: 1 test file (API)
- **Tax**: 1 test file (API)
- **User**: 1 test file (API)
- **Store**: 1 test file (API)

## 🎯 Contribution Guidelines

### Before You Start
1. Check existing issues on GitHub
2. Discuss major changes on Slack
3. Read the existing code to understand patterns
4. Write tests for your changes

### Pull Request Process
1. Fork the repository
2. Create a feature branch
3. Write tests
4. Implement feature
5. Ensure all tests pass
6. Create pull request with clear description

### Code Style
- Follow existing code patterns
- Use meaningful variable names
- Add JavaDoc for public methods
- Keep methods focused and small
- Use Spring annotations appropriately

## 🔗 Resources

### Documentation
- **This Guide**: Start here!
- **Official Docs**: https://shopizer-ecommerce.github.io/documentation/
- **API Docs**: http://localhost:8080/swagger-ui.html (when running)

### Community
- **GitHub**: https://github.com/shopizer-ecommerce/shopizer
- **Slack**: https://shopizer.slack.com
- **Stack Overflow**: Tag questions with `shopizer`

### Related Projects
- **Admin Panel**: shopizer-admin (React)
- **Shop Frontend**: shopizer-shop-reactjs (React)

## ❓ FAQ

### Q: Where do I start?
**A**: Read the Business Context document first, then explore the codebase structure.

### Q: How do I run tests?
**A**: `./mvnw test` from the root directory or specific module.

### Q: How do I add a new API endpoint?
**A**: Follow the pattern: Entity → Repository → Service → Facade → Controller → Tests

### Q: Where are the API endpoints documented?
**A**: Swagger UI at http://localhost:8080/swagger-ui.html

### Q: How do I debug the application?
**A**: Run with debug mode in your IDE or use remote debugging.

### Q: What database does it use?
**A**: H2 (development), MySQL (production)

### Q: How do I add a new payment gateway?
**A**: Implement the `PaymentModule` interface in `sm-core/modules`.

### Q: How do I add a new shipping method?
**A**: Implement the `ShippingQuoteModule` interface.

### Q: Where are the tests?
**A**: `sm-core/src/test` (unit) and `sm-shop/src/test` (integration)

### Q: How do I contribute?
**A**: Fork, create branch, write tests, implement, create PR.

## 🎓 Next Steps

1. **Read the documentation** in order:
   - Business Context (understand the domain)
   - Features & Tests (understand what exists)
   - Technical Architecture (understand how it works)

2. **Explore the code**:
   - Start with a simple feature like Product
   - Follow the request flow from API to database
   - Read the tests to understand expected behavior

3. **Make your first change**:
   - Pick a small task (add validation, improve error message)
   - Write tests first
   - Implement the change
   - Submit a PR

4. **Join the community**:
   - Slack channel for questions
   - GitHub for issues and PRs
   - Stack Overflow for technical questions

## 📝 Feedback

This documentation is a living document. If you find:
- Missing information
- Unclear explanations
- Outdated content
- Errors or typos

Please create an issue or submit a PR to improve it!

---

**Welcome to the Shopizer community! Happy coding! 🚀**
