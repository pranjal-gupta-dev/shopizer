# Shopizer - Technical Architecture Guide

## System Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Client Applications                      │
│  (React Shop, Admin Panel, Mobile Apps, Third-party Apps)  │
└────────────────────┬────────────────────────────────────────┘
                     │ REST API (JSON)
                     │
┌────────────────────▼────────────────────────────────────────┐
│                    sm-shop (API Layer)                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │ Controllers  │  │   Facades    │  │   Mappers    │     │
│  │  (REST API)  │─▶│  (Business)  │─▶│ (DTO ↔ Model)│     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│                 sm-core (Business Layer)                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │   Services   │  │ Repositories │  │   Modules    │     │
│  │  (Business   │─▶│    (Data     │  │ (Integrations)│     │
│  │    Logic)    │  │    Access)   │  │              │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│              sm-core-model (Domain Model)                    │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  JPA Entities (Product, Order, Customer, etc.)       │  │
│  └──────────────────────────────────────────────────────┘  │
└────────────────────┬────────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────────┐
│                  Data & External Services                    │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐      │
│  │ Database │ │  Search  │ │  Storage │ │ Payment  │      │
│  │(MySQL/H2)│ │(Elastic) │ │ (S3/GCP) │ │ Gateway  │      │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘      │
└─────────────────────────────────────────────────────────────┘
```

## Module Structure

### 1. sm-core-model
**Purpose**: Domain model and JPA entities

**Key Packages:**
- `com.salesmanager.core.model.catalog` - Product, Category, Manufacturer
- `com.salesmanager.core.model.order` - Order, OrderProduct, OrderTotal
- `com.salesmanager.core.model.customer` - Customer, CustomerAddress
- `com.salesmanager.core.model.merchant` - MerchantStore
- `com.salesmanager.core.model.reference` - Country, Zone, Language, Currency
- `com.salesmanager.core.model.shipping` - Shipping entities
- `com.salesmanager.core.model.tax` - Tax classes and rates
- `com.salesmanager.core.model.user` - Admin users and permissions

**Key Entities:**
- All entities extend `SalesManagerEntity` or `BusinessDomain`
- Use JPA annotations for persistence
- Support for multi-language descriptions via `*Description` entities
- Audit fields (created date, modified date)

### 2. sm-core
**Purpose**: Business logic and data access

**Key Packages:**
- `com.salesmanager.core.business.services` - Service layer
  - `catalog` - Product, category, manufacturer services
  - `order` - Order processing services
  - `customer` - Customer management
  - `shipping` - Shipping calculation
  - `payments` - Payment processing
  - `tax` - Tax calculation
  - `search` - Search indexing and querying
  - `content` - Content management
  - `reference` - Reference data services
  
- `com.salesmanager.core.business.repositories` - Data access layer
  - Spring Data JPA repositories
  - Custom repository implementations for complex queries
  
- `com.salesmanager.core.business.modules` - Integration modules
  - `integration.payment` - Payment gateway integrations
  - `integration.shipping` - Shipping carrier integrations
  - `cms` - Content management system integrations
  - `email` - Email service integrations
  
- `com.salesmanager.core.business.utils` - Utility classes
  - `ProductPriceUtils` - Price calculations
  - `CatalogServiceHelper` - Catalog utilities

### 3. sm-core-modules
**Purpose**: Additional integration modules

**Key Packages:**
- Payment modules
- Shipping modules
- CMS modules

### 4. sm-shop-model
**Purpose**: API DTOs (Data Transfer Objects)

**Key Packages:**
- `com.salesmanager.shop.model.catalog` - Product, category DTOs
- `com.salesmanager.shop.model.order` - Order DTOs
- `com.salesmanager.shop.model.customer` - Customer DTOs
- `com.salesmanager.shop.model.cart` - Shopping cart DTOs
- `com.salesmanager.shop.store.controller.*` - Facade interfaces

**DTO Types:**
- `Persistable*` - Input DTOs for create/update operations
- `Readable*` - Output DTOs for read operations
- `*Entity` - Base DTOs
- `*List` - Paginated list responses

### 5. sm-shop
**Purpose**: REST API and web layer

**Key Packages:**
- `com.salesmanager.shop.store.api.v1` - Version 1 REST APIs
- `com.salesmanager.shop.store.api.v2` - Version 2 REST APIs
- `com.salesmanager.shop.store.controller` - Facade implementations
- `com.salesmanager.shop.mapper` - Entity to DTO mappers
- `com.salesmanager.shop.populator` - DTO populators
- `com.salesmanager.shop.utils` - Web utilities
- `com.salesmanager.shop.filter` - Request filters
- `com.salesmanager.shop.application` - Spring Boot application

## Design Patterns

### 1. Facade Pattern
**Location**: `sm-shop/store/controller/**/facade`

Facades provide a simplified interface to complex business operations:
```java
public interface ProductFacade {
    ReadableProduct getProduct(Long id, MerchantStore store, Language language);
    ReadableProduct createProduct(PersistableProduct product, MerchantStore store, Language language);
    // ... more methods
}
```

### 2. Service Layer Pattern
**Location**: `sm-core/business/services`

Services encapsulate business logic:
```java
@Service("productService")
public class ProductServiceImpl implements ProductService {
    // Business logic for product operations
}
```

### 3. Repository Pattern
**Location**: `sm-core/business/repositories`

Repositories handle data access:
```java
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    // Data access methods
}
```

### 4. DTO Pattern
**Location**: `sm-shop-model`

Separate DTOs from domain entities:
- Input: `PersistableProduct`
- Output: `ReadableProduct`
- Domain: `Product` (entity)

### 5. Mapper Pattern
**Location**: `sm-shop/mapper`

Convert between entities and DTOs:
```java
@Component
public class ProductMapper {
    public ReadableProduct toReadable(Product product, Language language);
    public Product toDomain(PersistableProduct dto);
}
```

### 6. Strategy Pattern
**Location**: `sm-core/business/modules`

Pluggable modules for payments, shipping, etc.:
```java
public interface PaymentModule {
    Transaction processPayment(...);
    Transaction authorize(...);
    Transaction capture(...);
}
```

### 7. Module Pattern
**Location**: Integration modules

Extensible integration points:
- Payment modules (Stripe, PayPal, Braintree)
- Shipping modules (UPS, USPS, custom)
- CMS modules (local, S3, GCP)
- Email modules (SMTP, SES)

## Key Technologies

### Core Framework
- **Spring Boot 2.x** - Application framework
- **Spring Data JPA** - Data access
- **Hibernate** - ORM
- **Spring Security** - Authentication/authorization
- **Spring MVC** - REST API

### Database
- **MySQL** - Production database
- **H2** - Development/testing database
- **Flyway** - Database migrations (if configured)

### Search
- **Elasticsearch** - Product search and indexing

### Storage
- **Local File System** - Default storage
- **AWS S3** - Cloud storage option
- **Google Cloud Storage** - Cloud storage option
- **Infinispan** - Distributed cache

### Integration
- **Stripe** - Payment processing
- **PayPal** - Payment processing
- **Braintree** - Payment processing
- **UPS/USPS** - Shipping carriers
- **AWS SES** - Email service

### Build & Tools
- **Maven** - Build tool
- **Swagger/OpenAPI** - API documentation
- **JUnit** - Testing framework
- **Docker** - Containerization

## API Versioning

### Version Strategy
- **v0**: Legacy endpoints (deprecated)
- **v1**: Current stable API
- **v2**: Next-generation API (products, variants)

### URL Structure
```
/api/v1/{resource}
/api/v2/{resource}
```

### Version Migration
- v2 introduces improved product variant management
- v1 remains supported for backward compatibility

## Data Flow

### Typical Request Flow

1. **HTTP Request** → REST Controller (`sm-shop/api`)
2. **Controller** → Facade (`sm-shop/controller/facade`)
3. **Facade** → Service (`sm-core/services`)
4. **Service** → Repository (`sm-core/repositories`)
5. **Repository** → Database
6. **Response** ← Entity → DTO → JSON

### Example: Create Product

```
POST /api/v1/products
↓
ProductApi.createProduct()
↓
ProductFacade.createProduct(PersistableProduct)
↓
ProductMapper.toDomain(PersistableProduct) → Product
↓
ProductService.create(Product)
↓
ProductRepository.save(Product)
↓
Database INSERT
↓
ProductMapper.toReadable(Product) → ReadableProduct
↓
JSON Response
```

## Configuration

### Application Properties
**Location**: `sm-shop/src/main/resources/application.properties`

Key configurations:
- Database connection
- Elasticsearch connection
- File storage location
- Email settings
- Payment gateway credentials
- Shipping carrier credentials

### Multi-Store Configuration
Each store can have:
- Custom domain
- Custom branding
- Custom payment methods
- Custom shipping methods
- Custom tax rules

### Environment-Specific Config
- `application-dev.properties` - Development
- `application-prod.properties` - Production
- `application-test.properties` - Testing

## Security

### Authentication
- **JWT Tokens** - Stateless authentication
- **Customer Authentication** - For storefront users
- **Admin Authentication** - For admin users

### Authorization
- **Role-Based Access Control (RBAC)**
- Roles: ADMIN, SUPERADMIN, CUSTOMER
- Permissions: READ, WRITE, DELETE

### API Security
- CORS configuration
- CSRF protection
- Rate limiting (configurable)
- Input validation

## Performance Considerations

### Caching
- **Infinispan** - Distributed cache
- **Spring Cache** - Method-level caching
- **HTTP Cache** - Browser caching

### Database Optimization
- Indexed columns for frequent queries
- Lazy loading for associations
- Query optimization in custom repositories
- Connection pooling

### Search Optimization
- Elasticsearch indexing
- Async indexing for product updates
- Search result caching

## Extensibility Points

### 1. Payment Modules
Implement `PaymentModule` interface:
```java
public class CustomPaymentModule implements PaymentModule {
    // Custom payment logic
}
```

### 2. Shipping Modules
Implement `ShippingQuoteModule` interface:
```java
public class CustomShippingModule implements ShippingQuoteModule {
    // Custom shipping logic
}
```

### 3. Order Total Modules
Implement `OrderTotalModule` interface:
```java
public class CustomOrderTotalModule implements OrderTotalModule {
    // Custom order total calculation
}
```

### 4. CMS Modules
Implement `ContentAssetsManager` interface:
```java
public class CustomCMSModule implements ContentAssetsManager {
    // Custom content storage
}
```

## Development Workflow

### 1. Adding a New Feature

1. **Define Domain Model** (`sm-core-model`)
   - Create JPA entity
   - Add relationships
   - Add validation

2. **Create Repository** (`sm-core/repositories`)
   - Extend `JpaRepository`
   - Add custom queries if needed

3. **Implement Service** (`sm-core/services`)
   - Create service interface
   - Implement business logic
   - Add transaction management

4. **Create DTOs** (`sm-shop-model`)
   - Create `Persistable*` for input
   - Create `Readable*` for output

5. **Create Mapper** (`sm-shop/mapper`)
   - Implement entity ↔ DTO conversion

6. **Create Facade** (`sm-shop/controller/facade`)
   - Implement business orchestration
   - Handle DTO conversion

7. **Create API Controller** (`sm-shop/api`)
   - Define REST endpoints
   - Add Swagger documentation
   - Add validation

8. **Write Tests**
   - Unit tests for service
   - Integration tests for API

### 2. Debugging Tips

- Enable SQL logging: `spring.jpa.show-sql=true`
- Enable debug logging: `logging.level.com.salesmanager=DEBUG`
- Use Swagger UI for API testing: `http://localhost:8080/swagger-ui.html`
- Check Actuator endpoints: `http://localhost:8080/actuator`

### 3. Common Pitfalls

- **Lazy Loading Issues**: Use `@Transactional` or fetch joins
- **N+1 Queries**: Use `@EntityGraph` or fetch joins
- **DTO Mapping**: Ensure all required fields are mapped
- **Multi-Language**: Always handle language-specific descriptions
- **Store Context**: Always pass `MerchantStore` to services

## Deployment

### Docker Deployment
```bash
docker run -p 8080:8080 shopizerecomm/shopizer:latest
```

### Database Setup
1. Create MySQL database
2. Configure connection in `application.properties`
3. Run application (auto-creates schema)
4. Load initial data

### Production Checklist
- [ ] Configure production database
- [ ] Set up Elasticsearch cluster
- [ ] Configure cloud storage (S3/GCP)
- [ ] Set up payment gateway credentials
- [ ] Configure email service
- [ ] Enable HTTPS
- [ ] Set up monitoring (Actuator)
- [ ] Configure logging
- [ ] Set up backups
- [ ] Performance tuning

## Monitoring & Observability

### Spring Boot Actuator
Endpoints:
- `/actuator/health` - Health check
- `/actuator/metrics` - Application metrics
- `/actuator/info` - Application info

### Logging
- SLF4J with Logback
- Configurable log levels
- Log rotation

### Metrics
- JVM metrics
- HTTP request metrics
- Database connection pool metrics
- Custom business metrics

## Resources

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **Actuator**: `http://localhost:8080/actuator`
- **Official Docs**: https://shopizer-ecommerce.github.io/documentation/
- **GitHub**: https://github.com/shopizer-ecommerce/shopizer
- **Slack**: https://shopizer.slack.com
