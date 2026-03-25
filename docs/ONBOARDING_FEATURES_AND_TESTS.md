# Shopizer - Existing Features & Test Coverage

## Overview

This document provides a comprehensive overview of existing features in Shopizer and their test coverage. It's designed to help new developers understand what's already built and tested.

## Test Coverage Summary

### Test Structure
- **Unit Tests**: Located in `sm-core/src/test/java` (22 test files)
- **Integration Tests**: Located in `sm-shop/src/test/java` (14 test files)
- **Total Test Files**: 37 test classes

### Test Categories

#### 1. Core Business Logic Tests (`sm-core/src/test`)
These tests validate the core business services without HTTP layer:

- **Catalog Tests**
  - `ProductTest.java` - Product creation, management, variants
  - `ProductNextGenTest.java` - Next-generation product features
  - `CategoryTest.java` - Category hierarchy and management
  - `ManufacturerTest.java` - Manufacturer/brand management

- **Order Tests**
  - `OrderTest.java` - Order creation, status management, order processing
  - `InvoiceTest.java` - Invoice generation

- **Shopping Cart Tests**
  - `ShoppingCartTest.java` - Cart operations (add, update, remove items)

- **Customer Tests**
  - `CustomerTest.java` - Customer registration, profile management

- **Shipping Tests**
  - `ShippingMethodDecisionTest.java` - Shipping method selection logic
  - `ShippingQuoteByWeightTest.java` - Weight-based shipping calculations
  - `ShippingDistanceProcessorTest.java` - Distance-based shipping

- **Content Tests**
  - `StaticContentTest.java` - Static content management
  - `ContentFolderTest.java` - Content folder operations
  - `ContentImagesTest.java` - Image upload and management

- **Configuration Tests**
  - `ConfigurationTest.java` - System configuration
  - `ReferencesTest.java` - Reference data (countries, zones, currencies)

- **Utility Tests**
  - `SendEmailTest.java` - Email sending functionality
  - `UtilsTestCase.java` - Utility functions
  - `DataUtilsTest.java` - Data manipulation utilities

- **Module Tests**
  - `ModulesTest.java` - Integration module testing

#### 2. API Integration Tests (`sm-shop/src/test`)
These tests validate the REST API endpoints:

- **Product API Tests**
  - `ProductManagementAPIIntegrationTest.java` - Product CRUD via API
  - `ProductV2ManagementAPIIntegrationTest.java` - V2 product API

- **Category API Tests**
  - `CategoryManagementAPIIntegrationTest.java` - Category CRUD via API

- **Order API Tests**
  - `OrderApiIntegrationTest.java` - Order placement and management via API

- **Shopping Cart API Tests**
  - `ShoppingCartAPIIntegrationTest.java` - Cart operations via API
  - `CartTestBean.java` - Test data helper

- **Customer API Tests**
  - `CustomerRegistrationIntegrationTest.java` - Customer registration flow

- **User API Tests**
  - `UserApiIntegrationTest.java` - Admin user management

- **Store API Tests**
  - `MerchantStoreApiIntegrationTest.java` - Store configuration

- **Tax API Tests**
  - `TaxRateIntegrationTest.java` - Tax rate management

- **Search API Tests**
  - `SearchApiIntegrationTest.java` - Product search functionality

- **System Tests**
  - `ActuatorTest.java` - Health check and monitoring
  - `OptinApiIntegrationTest.java` - Opt-in functionality

- **Utility Tests**
  - `GeneratePasswordTest.java` - Password generation

## Feature Inventory

### 1. Catalog Management ✅ TESTED

#### Products
**Features:**
- Create, read, update, delete products
- Multi-language product descriptions
- Product SKU management
- Product images (multiple per product)
- Product pricing (base price, special price, discounts)
- Product availability and inventory
- Product attributes (size, color, custom properties)
- Product variants (combinations of attributes)
- Product relationships (related products, upsells, cross-sells)
- Product reviews and ratings
- Product types (general, digital)
- SEO-friendly URLs
- Product search and filtering

**Test Coverage:**
- ✅ Core: `ProductTest.java`, `ProductNextGenTest.java`
- ✅ API: `ProductManagementAPIIntegrationTest.java`, `ProductV2ManagementAPIIntegrationTest.java`

**API Endpoints:**
- `ProductApi.java` (v1) - Main product operations
- `ProductApiV2.java` (v2) - Enhanced product operations
- `ProductImageApi.java` - Image management
- `ProductPriceApi.java` - Pricing management
- `ProductInventoryApi.java` - Inventory management
- `ProductReviewApi.java` - Reviews
- `ProductRelationshipApi.java` - Product relationships
- `ProductVariantApi.java` (v2) - Variant management
- `ProductVariationApi.java` (v2) - Variation management

#### Categories
**Features:**
- Hierarchical category structure (parent-child)
- Multi-language category descriptions
- Category images
- Category sorting and ordering
- Featured categories
- SEO-friendly URLs
- Category depth management
- Category lineage tracking

**Test Coverage:**
- ✅ Core: `CategoryTest.java`
- ✅ API: `CategoryManagementAPIIntegrationTest.java`

**API Endpoints:**
- `CategoryApi.java` (v1) - Category CRUD operations

#### Manufacturers
**Features:**
- Manufacturer/brand management
- Multi-language descriptions
- Manufacturer images
- Product-manufacturer associations

**Test Coverage:**
- ✅ Core: `ManufacturerTest.java`

**API Endpoints:**
- `ProductManufacturerApi.java` (v1)

### 2. Shopping Cart ✅ TESTED

**Features:**
- Add products to cart
- Update quantities
- Remove items
- Calculate cart totals
- Apply promotional codes
- Persistent and session-based carts
- Cart item attributes
- Multi-currency support

**Test Coverage:**
- ✅ Core: `ShoppingCartTest.java`
- ✅ API: `ShoppingCartAPIIntegrationTest.java`

**API Endpoints:**
- `ShoppingCartApi.java` (v1)

### 3. Order Management ✅ TESTED

**Features:**
- Order creation from cart
- Order status management (Ordered, Processing, Delivered, Cancelled, Refunded)
- Order products (line items)
- Order totals (subtotal, tax, shipping, discounts)
- Order history
- Order status history tracking
- Invoice generation
- Order search and filtering
- Payment information capture
- Shipping information capture

**Test Coverage:**
- ✅ Core: `OrderTest.java`, `InvoiceTest.java`
- ✅ API: `OrderApiIntegrationTest.java`

**API Endpoints:**
- `OrderApi.java` (v1) - Main order operations
- `OrderTotalApi.java` (v1) - Order totals
- `OrderPaymentApi.java` (v1) - Payment details
- `OrderShippingApi.java` (v1) - Shipping details
- `OrderStatusHistoryApi.java` (v1) - Status tracking

### 4. Customer Management ✅ TESTED

**Features:**
- Customer registration
- Customer authentication
- Customer profile management
- Billing and shipping addresses
- Customer groups
- Customer reviews
- Newsletter subscriptions
- Password reset
- Customer order history

**Test Coverage:**
- ✅ Core: `CustomerTest.java`
- ✅ API: `CustomerRegistrationIntegrationTest.java`

**API Endpoints:**
- `CustomerApi.java` (v1) - Customer CRUD
- `AuthenticateCustomerApi.java` (v1) - Login
- `ResetCustomerPasswordApi.java` (v1) - Password reset
- `CustomerReviewApi.java` (v1) - Customer reviews
- `CustomerNewsletterApi.java` (v1) - Newsletter management

### 5. Shipping ✅ TESTED

**Features:**
- Multiple shipping methods
- Weight-based shipping calculation
- Distance-based shipping calculation
- Shipping zones and restrictions
- Carrier integrations (UPS, USPS)
- Custom shipping rules
- Store pickup option
- Shipping quotes
- Shipping configuration per store

**Test Coverage:**
- ✅ Core: `ShippingMethodDecisionTest.java`, `ShippingQuoteByWeightTest.java`, `ShippingDistanceProcessorTest.java`

**API Endpoints:**
- `ShippingExpeditionApi.java` (v1) - Shipping quotes
- `ShippingConfigurationApi.java` (v1) - Shipping setup

**Shipping Modules:**
- `CustomWeightBasedShippingQuote.java` - Weight-based
- `CustomShippingQuoteRules.java` - Rule-based
- `StorePickupShippingQuote.java` - Store pickup
- `UPSShippingQuote.java` - UPS integration
- `USPSShippingQuote.java` - USPS integration
- `ShippingDistancePreProcessorImpl.java` - Distance calculation
- `ShippingDecisionPreProcessorImpl.java` - Method selection

### 6. Payment Processing ⚠️ LIMITED TESTING

**Features:**
- Multiple payment methods
- Payment gateway integrations:
  - Stripe (v1 and v3)
  - PayPal Express Checkout
  - Braintree
  - BeanStream
- Credit card processing
- Transaction management
- Payment capture and refund
- Payment configuration per store

**Test Coverage:**
- ⚠️ No dedicated payment tests found
- Payment logic tested indirectly through order tests

**API Endpoints:**
- `PaymentApi.java` (v1)

**Payment Modules:**
- `StripePayment.java` - Stripe v1
- `Stripe3Payment.java` - Stripe v3
- `PayPalExpressCheckoutPayment.java` - PayPal
- `BraintreePayment.java` - Braintree
- `BeanStreamPayment.java` - BeanStream

### 7. Tax Management ✅ TESTED

**Features:**
- Tax classes (standard, reduced, zero-rated)
- Tax rates by country/zone
- Tax calculation on products
- Tax calculation on shipping
- Tax configuration per store

**Test Coverage:**
- ✅ API: `TaxRateIntegrationTest.java`

**API Endpoints:**
- `TaxRatesApi.java` (v1)
- `TaxClassApi.java` (v1)

### 8. Content Management ✅ TESTED

**Features:**
- Static content pages
- Content folders
- File upload and management
- Image management
- Multi-language content
- SEO metadata
- Content types (page, box, section)

**Test Coverage:**
- ✅ Core: `StaticContentTest.java`, `ContentFolderTest.java`, `ContentImagesTest.java`

**API Endpoints:**
- `ContentApi.java` (v1) - Public content
- `ContentAdministrationApi.java` (v1) - Admin content management

**Storage Options:**
- Local file system
- AWS S3
- Google Cloud Storage
- Infinispan cache

### 9. Search ✅ TESTED

**Features:**
- Full-text product search
- Faceted search (category, price, attributes)
- Search indexing with Elasticsearch
- Search autocomplete
- Search result ranking
- Multi-language search

**Test Coverage:**
- ✅ API: `SearchApiIntegrationTest.java`

**API Endpoints:**
- `SearchApi.java` (v1)
- `SearchToolsApi.java` (v1) - Admin search tools

### 10. User Management ✅ TESTED

**Features:**
- Admin user accounts
- User authentication
- User roles and permissions
- User groups
- Password management
- User profile

**Test Coverage:**
- ✅ API: `UserApiIntegrationTest.java`

**API Endpoints:**
- `UserApi.java` (v1)
- `AuthenticateUserApi.java` (v1)
- `ResetUserPasswordApi.java` (v1)

### 11. Store Management ✅ TESTED

**Features:**
- Multi-store support
- Store configuration
- Store branding (logo, images)
- Store localization (language, currency)
- Store contact information
- Store operating hours
- Store zones and countries

**Test Coverage:**
- ✅ API: `MerchantStoreApiIntegrationTest.java`

**API Endpoints:**
- `MerchantStoreApi.java` (v1)

### 12. Reference Data ✅ TESTED

**Features:**
- Countries
- Zones/States
- Languages
- Currencies
- Integration modules

**Test Coverage:**
- ✅ Core: `ReferencesTest.java`

**API Endpoints:**
- `ReferencesApi.java` (v1)

### 13. System Configuration ✅ TESTED

**Features:**
- System settings
- Module configuration
- Email configuration
- Cache management
- Health monitoring (Actuator)
- Opt-in management

**Test Coverage:**
- ✅ Core: `ConfigurationTest.java`
- ✅ API: `ActuatorTest.java`, `OptinApiIntegrationTest.java`

**API Endpoints:**
- `ConfigurationsApi.java` (v1)
- `CacheApi.java` (v1)
- `PublicConfigsApi.java` (v1)
- `ModulesApi.java` (v1)
- `OptinApi.java` (v1)
- `ContactApi.java` (v1)

### 14. Email Notifications ✅ TESTED

**Features:**
- Email templates
- Order confirmation emails
- Customer registration emails
- Password reset emails
- SMTP configuration
- AWS SES integration

**Test Coverage:**
- ✅ Core: `SendEmailTest.java`

**Email Modules:**
- `DefaultEmailSenderImpl.java` - SMTP
- `SESEmailSenderImpl.java` - AWS SES

### 15. Security 🔒

**Features:**
- JWT authentication
- Role-based access control
- Password encryption
- API security
- CORS configuration

**API Endpoints:**
- `SecurityApi.java` (v1)

### 16. Marketplace 🆕

**Features:**
- Multi-vendor support (emerging feature)

**API Endpoints:**
- `MarketPlaceApi.java` (v1)

### 17. Catalog Facade 📦

**Features:**
- Simplified catalog operations
- Product bundles
- Product items

**API Endpoints:**
- `CatalogApi.java` (v1)

## Test Coverage Gaps

### Areas with Good Coverage ✅
- Product management (core and API)
- Category management
- Order processing
- Shopping cart
- Customer management
- Shipping calculations
- Content management
- Search functionality
- Tax management
- User management

### Areas Needing More Tests ⚠️
- **Payment Processing**: No dedicated payment gateway tests
- **Security**: Authentication and authorization flows
- **Email Templates**: Template rendering
- **Marketplace**: Multi-vendor features
- **Product Variants**: Complex variant scenarios
- **Inventory Management**: Stock tracking edge cases
- **Promotional Codes**: Discount calculations
- **Multi-Currency**: Currency conversion
- **Performance**: Load and stress testing

### Integration Test Gaps 🔍
- End-to-end checkout flow
- Payment gateway integration tests
- Shipping carrier integration tests
- Email delivery tests
- Search indexing tests
- Cache invalidation tests
- Multi-store scenarios
- Concurrent order processing

## Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Core Tests Only
```bash
cd sm-core
mvn test
```

### Run API Integration Tests Only
```bash
cd sm-shop
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=ProductTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=ProductTest#testCreateProduct
```

## Test Configuration

Tests use:
- **JUnit 4**: Test framework
- **Spring Boot Test**: Integration test support
- **H2 Database**: In-memory database for tests
- **MockMvc**: API endpoint testing
- **RestTemplate**: HTTP client for integration tests

## Key Test Patterns

### 1. Core Service Tests
```java
@Test
public void testCreateProduct() throws Exception {
    // Setup test data
    Language en = languageService.getByCode("en");
    MerchantStore store = merchantService.getByCode(MerchantStore.DEFAULT_STORE);
    
    // Create product
    Product product = new Product();
    product.setSku("TEST-SKU");
    // ... set properties
    
    // Execute
    productService.create(product);
    
    // Verify
    Product retrieved = productService.getById(product.getId());
    Assert.assertNotNull(retrieved);
}
```

### 2. API Integration Tests
```java
@Test
public void testCreateProductViaAPI() {
    // Prepare request
    PersistableProduct product = new PersistableProduct();
    product.setSku("TEST-SKU");
    
    // Execute API call
    ResponseEntity<ReadableProduct> response = restTemplate.postForEntity(
        "/api/v1/products",
        product,
        ReadableProduct.class
    );
    
    // Verify
    assertThat(response.getStatusCode(), is(CREATED));
    assertNotNull(response.getBody().getId());
}
```

## Next Steps for Test Improvement

1. **Add Payment Tests**: Create integration tests for payment gateways
2. **E2E Tests**: Add complete checkout flow tests
3. **Performance Tests**: Add load testing for critical paths
4. **Security Tests**: Add authentication/authorization tests
5. **Contract Tests**: Add API contract tests
6. **Mutation Tests**: Add mutation testing for code quality
7. **Test Documentation**: Document test data setup and teardown
8. **CI/CD Integration**: Ensure tests run in CI pipeline

## Useful Test Resources

- Test base class: `AbstractSalesManagerCoreTestCase.java`
- Test support: `ServicesTestSupport.java`
- Test data builders: Create reusable test data builders
- Test utilities: `DataUtilsTest.java`

## Contributing Tests

When adding new features:
1. Write unit tests for business logic
2. Write integration tests for API endpoints
3. Ensure tests are isolated and repeatable
4. Use meaningful test names
5. Document complex test scenarios
6. Clean up test data in teardown
