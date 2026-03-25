# Shopizer - Quick Reference Guide

A cheat sheet for common development tasks in Shopizer.

## 🚀 Quick Commands

### Build & Run
```bash
# Build entire project
./mvnw clean install

# Run application
cd sm-shop
./mvnw spring-boot:run

# Run with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Build Docker image
docker build -t shopizer:local .

# Run with Docker
docker run -p 8080:8080 shopizer:local
```

### Testing
```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=ProductTest

# Run specific test method
./mvnw test -Dtest=ProductTest#testCreateProduct

# Run tests with coverage
./mvnw clean test jacoco:report

# Skip tests during build
./mvnw clean install -DskipTests
```

### Database
```bash
# Run with MySQL
# Edit application.properties first, then:
./mvnw spring-boot:run

# Run with H2 (default)
./mvnw spring-boot:run

# Access H2 console (if enabled)
# http://localhost:8080/h2-console
```

## 📁 File Locations

### Configuration Files
```
sm-shop/src/main/resources/
├── application.properties          # Main config
├── application-dev.properties      # Dev config
├── application-prod.properties     # Prod config
└── logback.xml                     # Logging config
```

### Key Directories
```
sm-core-model/src/main/java/        # Domain entities
sm-core/src/main/java/              # Business logic
sm-shop-model/src/main/java/        # API DTOs
sm-shop/src/main/java/              # REST API
sm-core/src/test/java/              # Unit tests
sm-shop/src/test/java/              # Integration tests
```

## 🔧 Common Tasks

### 1. Create a New Product

**Via API (Swagger UI)**:
```
POST /api/v1/products
{
  "sku": "PROD-001",
  "productShipeable": true,
  "quantity": 100,
  "price": 29.99,
  "descriptions": [{
    "language": "en",
    "name": "My Product",
    "description": "Product description"
  }]
}
```

**Via Code**:
```java
Product product = new Product();
product.setSku("PROD-001");
product.setProductShipeable(true);

ProductDescription description = new ProductDescription();
description.setName("My Product");
description.setDescription("Product description");
description.setLanguage(languageService.getByCode("en"));
product.getDescriptions().add(description);

ProductAvailability availability = new ProductAvailability();
availability.setProductQuantity(100);
product.getAvailabilities().add(availability);

ProductPrice price = new ProductPrice();
price.setProductPriceAmount(new BigDecimal("29.99"));
availability.getPrices().add(price);

productService.create(product);
```

### 2. Create a New Category

**Via API**:
```
POST /api/v1/categories
{
  "code": "electronics",
  "sortOrder": 0,
  "visible": true,
  "descriptions": [{
    "language": "en",
    "name": "Electronics",
    "description": "Electronic products"
  }]
}
```

### 3. Create a Shopping Cart

**Via API**:
```
POST /api/v1/cart
{
  "code": "cart-123"
}

# Add item to cart
POST /api/v1/cart/{code}/item
{
  "product": "PROD-001",
  "quantity": 2
}
```

### 4. Create an Order

**Via API**:
```
POST /api/v1/orders
{
  "customer": {
    "emailAddress": "customer@example.com",
    "billing": { /* address */ },
    "delivery": { /* address */ }
  },
  "shoppingCart": "cart-123",
  "payment": {
    "paymentType": "CREDITCARD",
    "paymentModule": "stripe"
  }
}
```

### 5. Search Products

**Via API**:
```
GET /api/v1/search?query=laptop&lang=en&store=DEFAULT
```

## 🎯 Common Code Patterns

### 1. Service Layer Pattern

```java
@Service("myService")
public class MyServiceImpl implements MyService {
    
    @Autowired
    private MyRepository repository;
    
    @Override
    @Transactional
    public MyEntity create(MyEntity entity) throws ServiceException {
        // Validation
        Validate.notNull(entity, "Entity cannot be null");
        
        // Business logic
        entity.setCreatedDate(new Date());
        
        // Save
        return repository.save(entity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public MyEntity getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
    }
}
```

### 2. Facade Pattern

```java
@Service
public class MyFacadeImpl implements MyFacade {
    
    @Autowired
    private MyService service;
    
    @Autowired
    private MyMapper mapper;
    
    @Override
    public ReadableMyEntity get(Long id, MerchantStore store, Language language) {
        MyEntity entity = service.getById(id);
        return mapper.toReadable(entity, store, language);
    }
    
    @Override
    public ReadableMyEntity create(PersistableMyEntity dto, MerchantStore store, Language language) {
        MyEntity entity = mapper.toDomain(dto);
        entity.setMerchantStore(store);
        entity = service.create(entity);
        return mapper.toReadable(entity, store, language);
    }
}
```

### 3. REST Controller Pattern

```java
@RestController
@RequestMapping("/api/v1/myentity")
@Api(tags = "My Entity API")
public class MyEntityApi {
    
    @Autowired
    private MyFacade facade;
    
    @GetMapping("/{id}")
    @ApiOperation("Get entity by ID")
    public ReadableMyEntity get(
            @PathVariable Long id,
            @RequestHeader(value = "store", defaultValue = "DEFAULT") String storeCode,
            @RequestHeader(value = "lang", defaultValue = "en") String lang) {
        
        MerchantStore store = merchantStoreService.getByCode(storeCode);
        Language language = languageService.getByCode(lang);
        
        return facade.get(id, store, language);
    }
    
    @PostMapping
    @ApiOperation("Create entity")
    public ResponseEntity<ReadableMyEntity> create(
            @Valid @RequestBody PersistableMyEntity entity,
            @RequestHeader(value = "store", defaultValue = "DEFAULT") String storeCode,
            @RequestHeader(value = "lang", defaultValue = "en") String lang) {
        
        MerchantStore store = merchantStoreService.getByCode(storeCode);
        Language language = languageService.getByCode(lang);
        
        ReadableMyEntity created = facade.create(entity, store, language);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
```

### 4. Repository Pattern

```java
public interface MyRepository extends JpaRepository<MyEntity, Long>, MyRepositoryCustom {
    
    @Query("SELECT e FROM MyEntity e WHERE e.merchantStore.id = ?1")
    List<MyEntity> findByStore(Long storeId);
    
    @Query("SELECT e FROM MyEntity e WHERE e.code = ?1 AND e.merchantStore.id = ?2")
    Optional<MyEntity> findByCodeAndStore(String code, Long storeId);
}

// Custom repository for complex queries
public interface MyRepositoryCustom {
    Page<MyEntity> search(String query, Pageable pageable);
}

@Repository
public class MyRepositoryImpl implements MyRepositoryCustom {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Page<MyEntity> search(String query, Pageable pageable) {
        // Complex query implementation
    }
}
```

### 5. Mapper Pattern

```java
@Component
public class MyMapper {
    
    @Autowired
    private LanguageService languageService;
    
    public ReadableMyEntity toReadable(MyEntity entity, MerchantStore store, Language language) {
        ReadableMyEntity readable = new ReadableMyEntity();
        readable.setId(entity.getId());
        readable.setCode(entity.getCode());
        
        // Map descriptions
        MyEntityDescription description = entity.getDescription(language);
        if (description != null) {
            readable.setName(description.getName());
            readable.setDescription(description.getDescription());
        }
        
        return readable;
    }
    
    public MyEntity toDomain(PersistableMyEntity dto) {
        MyEntity entity = new MyEntity();
        entity.setCode(dto.getCode());
        
        // Map descriptions
        if (dto.getDescriptions() != null) {
            for (MyEntityDescription desc : dto.getDescriptions()) {
                MyEntityDescription description = new MyEntityDescription();
                description.setName(desc.getName());
                description.setDescription(desc.getDescription());
                description.setLanguage(languageService.getByCode(desc.getLanguage()));
                entity.getDescriptions().add(description);
            }
        }
        
        return entity;
    }
}
```

## 🔍 Debugging Tips

### Enable SQL Logging
```properties
# application.properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### Enable Debug Logging
```properties
logging.level.com.salesmanager=DEBUG
logging.level.org.springframework.web=DEBUG
```

### Common Issues

**Issue**: LazyInitializationException
```java
// Solution: Use @Transactional or fetch joins
@Transactional(readOnly = true)
public Product getProduct(Long id) {
    return productRepository.findById(id).orElse(null);
}

// Or use fetch join
@Query("SELECT p FROM Product p LEFT JOIN FETCH p.descriptions WHERE p.id = ?1")
Product findByIdWithDescriptions(Long id);
```

**Issue**: N+1 Query Problem
```java
// Solution: Use @EntityGraph or fetch joins
@EntityGraph(attributePaths = {"descriptions", "categories"})
List<Product> findAll();
```

**Issue**: Store Context Missing
```java
// Always pass MerchantStore to services
MerchantStore store = merchantStoreService.getByCode("DEFAULT");
productService.getByStore(store);
```

## 📊 Useful Queries

### Get All Products for a Store
```java
List<Product> products = productService.listByStore(store);
```

### Get Product by SKU
```java
Product product = productService.getBySku("PROD-001", store);
```

### Get Categories by Store
```java
List<Category> categories = categoryService.listByStore(store, language);
```

### Get Orders by Customer
```java
OrderCriteria criteria = new OrderCriteria();
criteria.setCustomerId(customerId);
OrderList orders = orderService.listByStore(store, criteria);
```

### Search Products
```java
SearchRequest request = new SearchRequest();
request.setQuery("laptop");
SearchResponse response = searchService.search(store, language, request);
```

## 🔐 Security

### Get Current User
```java
@Autowired
private SecurityFacade securityFacade;

String username = securityFacade.getCurrentUser();
```

### Check Permissions
```java
@PreAuthorize("hasRole('ADMIN')")
public void adminOnlyMethod() {
    // Admin only
}

@PreAuthorize("hasAuthority('PRODUCT_CREATE')")
public void createProduct() {
    // Requires PRODUCT_CREATE permission
}
```

## 🌐 API Headers

### Required Headers
```
store: DEFAULT              # Store code
lang: en                    # Language code
```

### Authentication Header
```
Authorization: Bearer <JWT_TOKEN>
```

### Example cURL
```bash
curl -X GET "http://localhost:8080/api/v1/products" \
  -H "store: DEFAULT" \
  -H "lang: en" \
  -H "Authorization: Bearer eyJhbGc..."
```

## 📝 Swagger UI

### Access Swagger
```
http://localhost:8080/swagger-ui.html
```

### Common Endpoints
- Products: `/api/v1/products`
- Categories: `/api/v1/categories`
- Cart: `/api/v1/cart`
- Orders: `/api/v1/orders`
- Customers: `/api/v1/customers`
- Search: `/api/v1/search`

## 🎨 Response Formats

### Success Response
```json
{
  "id": 1,
  "code": "PROD-001",
  "name": "Product Name",
  "price": 29.99
}
```

### Error Response
```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": [
    {
      "field": "sku",
      "message": "SKU is required"
    }
  ]
}
```

### Paginated Response
```json
{
  "items": [...],
  "totalPages": 10,
  "totalItems": 100,
  "currentPage": 0,
  "pageSize": 10
}
```

## 🔄 Common Workflows

### Product Creation Workflow
1. Create manufacturer (optional)
2. Create categories
3. Create product with descriptions
4. Add product images
5. Set product pricing
6. Set product availability
7. Assign to categories
8. Publish product

### Order Processing Workflow
1. Customer adds items to cart
2. Customer proceeds to checkout
3. Calculate shipping options
4. Calculate taxes
5. Customer selects payment method
6. Process payment
7. Create order
8. Send confirmation email
9. Update inventory
10. Fulfill order

### Customer Registration Workflow
1. Customer submits registration
2. Validate email uniqueness
3. Hash password
4. Create customer record
5. Send welcome email
6. Auto-login (optional)

## 💡 Pro Tips

1. **Always use transactions** for write operations
2. **Always pass MerchantStore** to services
3. **Always handle multi-language** descriptions
4. **Use DTOs** for API layer, entities for business layer
5. **Write tests first** (TDD approach)
6. **Use Swagger** for API testing during development
7. **Check existing code** before creating new patterns
8. **Follow naming conventions** (Persistable*, Readable*)
9. **Use facades** to orchestrate multiple services
10. **Handle exceptions** gracefully with proper error messages

## 📚 Additional Resources

- **Main Onboarding**: [ONBOARDING_README.md](./ONBOARDING_README.md)
- **Business Context**: [ONBOARDING_BUSINESS_CONTEXT.md](./ONBOARDING_BUSINESS_CONTEXT.md)
- **Features & Tests**: [ONBOARDING_FEATURES_AND_TESTS.md](./ONBOARDING_FEATURES_AND_TESTS.md)
- **Architecture**: [ONBOARDING_TECHNICAL_ARCHITECTURE.md](./ONBOARDING_TECHNICAL_ARCHITECTURE.md)
- **Test Coverage**: [TEST_COVERAGE_REPORT.md](./TEST_COVERAGE_REPORT.md)

---

**Keep this guide handy for quick reference during development!**
