# Shopizer Backend - Business Context & Architecture

## 📋 Table of Contents
1. [Business Overview](#business-overview)
2. [System Architecture](#system-architecture)
3. [Core Business Domains](#core-business-domains)
4. [Technology Stack](#technology-stack)
5. [Project Structure](#project-structure)
6. [Key Components](#key-components)

---

## 🏢 Business Overview

### What is Shopizer?

Shopizer is an **open-source headless e-commerce platform** built with Java and Spring Boot. It provides a complete REST API for building modern e-commerce applications with any frontend technology.

### Business Model

- **Multi-tenant**: Supports multiple merchant stores in a single installation
- **Headless Commerce**: Backend API decoupled from frontend presentation
- **B2C & B2B**: Supports both business-to-consumer and business-to-business models
- **Multi-language & Multi-currency**: Global commerce support

### Target Users

1. **Merchants**: Store owners who sell products online
2. **Customers**: End users who browse and purchase products
3. **Administrators**: System admins who manage stores, users, and configurations
4. **Developers**: Build custom storefronts using the REST API

---

## 🏗️ System Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Frontend Layer                          │
│  (React Shop, Admin Panel, Mobile Apps, Custom Frontends)  │
└─────────────────────────────────────────────────────────────┘
                            ↓ REST API
┌─────────────────────────────────────────────────────────────┐
│                   API Layer (sm-shop)                       │
│  • REST Controllers (v1, v2)                                │
│  • Authentication & Authorization (JWT)                     │
│  • Request/Response Mapping                                 │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                 Business Logic Layer (sm-core)              │
│  • Services (Product, Order, Customer, etc.)                │
│  • Business Rules & Validation                              │
│  • Integration Modules (Payment, Shipping, Email)           │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│              Data Access Layer (sm-core)                    │
│  • JPA Repositories                                         │
│  • Entity Models                                            │
│  • Database Queries                                         │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                    Database Layer                           │
│  • MySQL (Production)                                       │
│  • H2 (Development/Testing)                                 │
└─────────────────────────────────────────────────────────────┘
```

### Architecture Patterns

- **Layered Architecture**: Clear separation of concerns
- **Service-Oriented**: Business logic encapsulated in services
- **Repository Pattern**: Data access abstraction
- **DTO Pattern**: Data transfer objects for API communication
- **Facade Pattern**: Complex operations simplified through facades

---

## 🎯 Core Business Domains

### 1. **Catalog Management**
Manages products, categories, and product attributes.

**Key Entities:**
- `Product`: Core product information (SKU, name, description, price)
- `Category`: Product categorization and hierarchy
- `Manufacturer`: Product brands/manufacturers
- `ProductAttribute`: Product options (size, color, etc.)
- `ProductImage`: Product images and media
- `ProductReview`: Customer reviews and ratings

**Business Rules:**
- Products must belong to at least one category
- Products can have multiple images (default image required)
- Product prices can vary by currency
- Products can have variants (size, color combinations)
- Inventory tracking per product

### 2. **Customer Management**
Handles customer accounts, authentication, and profiles.

**Key Entities:**
- `Customer`: Customer account information
- `CustomerAttribute`: Custom customer fields
- `CustomerReview`: Customer-submitted reviews
- `CustomerOptin`: Marketing preferences

**Business Rules:**
- Unique email per customer
- Password encryption required
- Email verification for registration
- Customer groups for segmentation
- Billing and shipping addresses

### 3. **Shopping Cart**
Manages shopping cart and checkout process.

**Key Entities:**
- `ShoppingCart`: Customer's cart
- `ShoppingCartItem`: Items in cart
- `ShoppingCartAttribute`: Cart-level attributes

**Business Rules:**
- Cart persists across sessions
- Real-time price calculation
- Inventory validation before checkout
- Cart expiration after inactivity
- Guest and registered customer carts

### 4. **Order Management**
Processes orders from creation to fulfillment.

**Key Entities:**
- `Order`: Order header information
- `OrderProduct`: Products in order
- `OrderTotal`: Order totals (subtotal, tax, shipping)
- `OrderStatusHistory`: Order status tracking
- `Transaction`: Payment transactions

**Business Rules:**
- Order number generation (unique)
- Order status workflow (Pending → Processing → Shipped → Delivered)
- Inventory deduction on order confirmation
- Order cannot be deleted, only cancelled
- Email notifications on status changes

### 5. **Payment Processing**
Integrates with payment gateways.

**Supported Gateways:**
- PayPal
- Stripe
- Braintree
- Custom payment modules

**Business Rules:**
- PCI compliance for card data
- Payment authorization before capture
- Refund processing
- Transaction logging

### 6. **Shipping & Fulfillment**
Calculates shipping costs and manages delivery.

**Key Entities:**
- `ShippingQuote`: Shipping cost calculation
- `ShippingOrigin`: Warehouse/store location
- `PackingBox`: Package dimensions

**Shipping Methods:**
- Flat rate
- Weight-based
- Distance-based
- Real-time carrier rates (UPS, FedEx, USPS)

### 7. **Merchant Store**
Multi-tenant store management.

**Key Entities:**
- `MerchantStore`: Store configuration
- `MerchantConfiguration`: Store settings
- `Language`: Supported languages
- `Currency`: Supported currencies
- `Country` & `Zone`: Geographic data

**Business Rules:**
- Each store has unique code
- Store-specific products and categories
- Store-specific pricing and inventory
- Store-level tax configuration

### 8. **User & Security**
Admin user management and permissions.

**Key Entities:**
- `User`: Admin users
- `Group`: User groups (Admin, Superadmin, etc.)
- `Permission`: Access permissions

**Security Features:**
- JWT authentication
- Role-based access control (RBAC)
- Password policies
- Session management

### 9. **Content Management**
Manages static content and media.

**Key Entities:**
- `Content`: CMS content (pages, banners)
- `ContentDescription`: Multi-language content

**Features:**
- File storage (local or cloud)
- Image resizing and optimization
- Content versioning

### 10. **Tax Management**
Calculates taxes based on rules.

**Key Entities:**
- `TaxClass`: Tax categories
- `TaxRate`: Tax rates by location

**Business Rules:**
- Location-based tax calculation
- Product-specific tax classes
- Tax exemptions

---

## 🛠️ Technology Stack

### Backend
- **Java 11+** (tested with Java 11, 17)
- **Spring Boot 2.5.12**: Application framework
- **Spring Data JPA**: Data access
- **Hibernate**: ORM
- **Spring Security**: Authentication & authorization
- **JWT**: Token-based authentication

### Database
- **MySQL 8.0**: Production database
- **H2**: In-memory database for testing

### API & Documentation
- **REST API**: RESTful web services
- **Swagger/OpenAPI**: API documentation
- **Jackson**: JSON serialization

### Build & Testing
- **Maven**: Build tool
- **JUnit**: Unit testing
- **Spring Test**: Integration testing

### Integration
- **Email**: SMTP integration
- **Payment**: PayPal, Stripe, Braintree
- **Shipping**: UPS, FedEx, USPS APIs
- **Search**: Elasticsearch (optional)

---

## 📁 Project Structure

```
shopizer/
├── sm-core-model/          # Domain entities (JPA models)
│   └── src/main/java/com/salesmanager/core/model/
│       ├── catalog/        # Product, Category entities
│       ├── customer/       # Customer entities
│       ├── order/          # Order entities
│       ├── merchant/       # Store entities
│       └── ...
│
├── sm-core/                # Business logic & services
│   └── src/main/java/com/salesmanager/core/business/
│       ├── services/       # Business services
│       │   ├── catalog/
│       │   ├── customer/
│       │   ├── order/
│       │   └── ...
│       ├── repositories/   # JPA repositories
│       └── modules/        # Integration modules
│
├── sm-core-modules/        # External integrations
│   └── Payment, Shipping, Email modules
│
├── sm-shop-model/          # API DTOs (Request/Response models)
│   └── src/main/java/com/salesmanager/shop/model/
│
├── sm-shop/                # REST API & Web layer
│   └── src/main/java/com/salesmanager/shop/
│       ├── store/api/      # REST controllers
│       │   ├── v1/         # API version 1
│       │   └── v2/         # API version 2
│       ├── populator/      # DTO converters
│       ├── store/facade/   # Business facades
│       └── application/    # Spring Boot app
│
└── pom.xml                 # Maven parent POM
```

### Module Responsibilities

| Module | Purpose |
|--------|---------|
| **sm-core-model** | JPA entities, database models |
| **sm-core** | Business logic, services, repositories |
| **sm-core-modules** | Payment, shipping, email integrations |
| **sm-shop-model** | API DTOs, request/response objects |
| **sm-shop** | REST API, controllers, security |

---

## 🔑 Key Components

### Services Layer

Services contain business logic and are injected via Spring DI.

**Naming Convention**: `{Entity}Service` (e.g., `ProductService`, `OrderService`)

**Common Service Methods:**
- `create(entity)`: Create new entity
- `update(entity)`: Update existing entity
- `delete(entity)`: Delete entity
- `getById(id)`: Retrieve by ID
- `list()`: List all entities
- `count()`: Count entities

**Example Services:**
- `ProductService`: Product CRUD and business logic
- `OrderService`: Order processing and management
- `CustomerService`: Customer account management
- `ShoppingCartService`: Cart operations
- `PaymentService`: Payment processing

### Repository Layer

Repositories handle database operations using Spring Data JPA.

**Naming Convention**: `{Entity}Repository`

**Features:**
- Automatic CRUD operations
- Custom query methods
- Criteria API for complex queries

### API Controllers

REST controllers expose business functionality via HTTP endpoints.

**Naming Convention**: `{Entity}Api` (e.g., `ProductApi`, `OrderApi`)

**API Versions:**
- `/api/v1/*`: Version 1 (stable)
- `/api/v2/*`: Version 2 (new features)

**Authentication:**
- `/api/v1/private/*`: Requires admin authentication
- `/api/v1/auth/*`: Requires customer authentication
- Public endpoints: No authentication required

### Populators (Mappers)

Convert between entities and DTOs.

**Types:**
- `Persistable{Entity}Populator`: DTO → Entity (for create/update)
- `Readable{Entity}Populator`: Entity → DTO (for read operations)

### Facades

Simplify complex operations by orchestrating multiple services.

**Example**: `OrderFacade` coordinates:
- Cart validation
- Inventory check
- Payment processing
- Order creation
- Email notification

---

## 📊 Database Schema Highlights

### Key Tables

| Table | Purpose |
|-------|---------|
| `PRODUCT` | Product master data |
| `CATEGORY` | Product categories |
| `CUSTOMER` | Customer accounts |
| `SALES_ORDER` | Orders |
| `ORDER_PRODUCT` | Order line items |
| `SHOPPING_CART` | Shopping carts |
| `MERCHANT_STORE` | Store configurations |
| `USERS` | Admin users |
| `PERMISSION` | Access permissions |

### Relationships

- **Product ↔ Category**: Many-to-Many
- **Product ↔ Manufacturer**: Many-to-One
- **Order ↔ Customer**: Many-to-One
- **Order ↔ OrderProduct**: One-to-Many
- **Store ↔ Product**: One-to-Many (multi-tenant)

---

## 🔐 Security Model

### Authentication

1. **JWT Tokens**: Stateless authentication
2. **Username/Password**: Traditional login
3. **OAuth2**: Social login (optional)

### Authorization

**Permission Levels:**
- `AUTH`: Basic authenticated access
- `ADMIN`: Store administration
- `SUPERADMIN`: System-wide administration
- `PRODUCTS`: Product management
- `ORDER`: Order management
- `CONTENT`: Content management
- `STORE`: Store configuration

### User Groups

- **SUPERADMIN**: Full system access
- **ADMIN**: Store-level administration
- **ADMIN_CATALOGUE`: Product management only
- **ADMIN_ORDER`: Order management only

---

## 📝 Configuration

### Application Profiles

- `local`: Local development (H2 database)
- `mysql`: MySQL database
- `cloud`: Cloud deployment
- `docker`: Docker container

### Key Configuration Files

- `application.properties`: Main configuration
- `database.properties`: Database connection
- `shopizer-core.properties`: Business configuration
- `email.properties`: Email server settings

---

## 🚀 Getting Started

See [DEVELOPER_ONBOARDING.md](./DEVELOPER_ONBOARDING.md) for detailed setup instructions.

---

## 📚 Additional Resources

- **API Documentation**: http://localhost:8080/swagger-ui.html
- **Official Site**: http://www.shopizer.com
- **GitHub**: https://github.com/shopizer-ecommerce/shopizer
- **Documentation**: https://shopizer-ecommerce.github.io/documentation/
- **Slack Community**: https://shopizer.slack.com
