# Shopizer - Business Context & Domain Overview

## What is Shopizer?

Shopizer is an **open-source headless e-commerce platform** built with Java (Spring Boot) that provides a complete REST API for building modern e-commerce applications. It's designed to support multi-store, multi-language, and multi-currency operations.

## Business Domain

### Core Business Entities

#### 1. **Merchant Store**
- Represents a single e-commerce store/tenant in the system
- Supports multi-store architecture (one installation, multiple stores)
- Each store has its own:
  - Products catalog
  - Customers
  - Orders
  - Configuration (currency, language, payment methods)
  - Branding (logo, images)

#### 2. **Catalog Management**
The product catalog is the heart of the e-commerce system:

- **Products**: Physical or digital items for sale
  - SKU-based identification
  - Multi-language descriptions
  - Multiple images
  - Pricing with support for discounts
  - Inventory tracking
  - Product variants (size, color, etc.)
  - Product attributes (custom properties)
  - Product relationships (related products, cross-sells)
  
- **Categories**: Hierarchical organization of products
  - Tree structure with parent-child relationships
  - Multi-language support
  - SEO-friendly URLs
  - Featured categories
  
- **Manufacturers**: Product brands/manufacturers
  - Multi-language descriptions
  - Associated with products

#### 3. **Customer Management**
- Customer registration and authentication
- Customer profiles with billing/shipping addresses
- Customer reviews and ratings
- Customer groups for segmentation
- Newsletter subscriptions
- Order history

#### 4. **Shopping Cart**
- Session-based or persistent carts
- Add/remove/update items
- Apply promotional codes
- Calculate totals with taxes and shipping
- Cart abandonment tracking

#### 5. **Order Management**
Complete order lifecycle:
- **Order Creation**: From cart to order
- **Order Status**: Ordered, Processing, Delivered, Cancelled, Refunded
- **Order Products**: Line items with pricing snapshot
- **Order Totals**: Subtotal, tax, shipping, discounts, grand total
- **Payment Processing**: Integration with payment gateways
- **Shipping**: Integration with shipping providers
- **Invoicing**: Generate invoices

#### 6. **Pricing & Tax**
- **Product Pricing**: Base price, special prices, discounts
- **Tax Management**: 
  - Tax classes (standard, reduced, zero-rated)
  - Tax rates by country/zone
  - Tax calculation on products and shipping
- **Currency Support**: Multi-currency with exchange rates

#### 7. **Shipping**
- Multiple shipping methods
- Weight-based and distance-based calculations
- Integration with carriers (UPS, USPS)
- Custom shipping rules
- Store pickup option
- Shipping zones and restrictions

#### 8. **Payment**
- Multiple payment methods support
- Integration with payment gateways:
  - Stripe
  - PayPal
  - Braintree
  - BeanStream
- Credit card processing
- Transaction management

#### 9. **Content Management**
- Static content pages
- Content folders and files
- Image management
- Multi-language content
- SEO metadata

#### 10. **Search**
- Product search with Elasticsearch integration
- Faceted search (filter by category, price, attributes)
- Full-text search across product descriptions
- Search indexing and optimization

## Business Workflows

### Customer Purchase Flow
1. **Browse Catalog** → Customer views products by category or search
2. **Add to Cart** → Customer adds products to shopping cart
3. **Review Cart** → Customer reviews items, quantities, and prices
4. **Checkout** → Customer provides shipping and billing information
5. **Calculate Shipping** → System calculates shipping options and costs
6. **Calculate Tax** → System calculates applicable taxes
7. **Payment** → Customer selects payment method and completes payment
8. **Order Confirmation** → System creates order and sends confirmation
9. **Fulfillment** → Merchant processes and ships order
10. **Delivery** → Customer receives products

### Merchant Product Management Flow
1. **Create Product** → Define product details (SKU, name, description)
2. **Set Pricing** → Configure base price, special prices, discounts
3. **Manage Inventory** → Set stock quantities and availability
4. **Add Images** → Upload product images
5. **Categorize** → Assign to categories
6. **Configure Variants** → Set up size, color, or other variations
7. **Publish** → Make product available for sale
8. **Monitor** → Track sales, inventory, and reviews

### Order Fulfillment Flow
1. **Order Received** → New order created from checkout
2. **Payment Verification** → Confirm payment processed successfully
3. **Order Processing** → Merchant prepares items for shipment
4. **Shipping** → Create shipping label and ship items
5. **Tracking** → Update order with tracking information
6. **Delivery Confirmation** → Mark order as delivered
7. **Post-Sale** → Handle returns, refunds, or customer service

## Multi-Tenancy Model

Shopizer supports **multi-store architecture**:
- Single codebase serves multiple stores
- Each store identified by unique code (e.g., "DEFAULT")
- Data isolation at the store level
- Shared infrastructure (users, reference data)
- Store-specific configuration

## Internationalization (i18n)

- **Multi-Language Support**: Products, categories, and content in multiple languages
- **Multi-Currency**: Prices displayed in different currencies
- **Localization**: Date, time, and number formatting per locale
- **SEO URLs**: Language-specific friendly URLs

## Integration Points

### External Systems
- **Payment Gateways**: Stripe, PayPal, Braintree
- **Shipping Carriers**: UPS, USPS, custom integrations
- **Email Service**: SMTP, AWS SES
- **Cloud Storage**: AWS S3, Google Cloud Storage for images/files
- **Search Engine**: Elasticsearch for product search
- **Cache**: Infinispan for distributed caching

### API Architecture
- **RESTful API**: JSON-based REST API for all operations
- **Swagger/OpenAPI**: API documentation at `/swagger-ui.html`
- **Versioned APIs**: v0, v1, v2 endpoints
- **Authentication**: JWT-based authentication for customers and admin users

## Key Business Rules

### Product Rules
- Products must have at least one availability record to be purchasable
- Products must belong to at least one category
- SKU must be unique within a store
- Product prices are stored in the store's default currency

### Order Rules
- Orders capture a snapshot of product prices at time of purchase
- Order status transitions follow a defined workflow
- Orders cannot be deleted, only cancelled or refunded
- Tax and shipping are calculated based on delivery address

### Inventory Rules
- Inventory is tracked per product availability
- Out-of-stock products can be hidden or shown as unavailable
- Inventory is decremented when order is placed
- Inventory can be restored on order cancellation

### Pricing Rules
- Special prices override base prices when active
- Discounts can be percentage or fixed amount
- Promotional codes apply to cart total
- Tax is calculated after discounts

## User Roles

### Customer
- Browse products
- Manage cart
- Place orders
- View order history
- Write reviews

### Merchant Admin
- Manage products and catalog
- Process orders
- Configure store settings
- View reports
- Manage customers

### Super Admin
- Manage multiple stores
- Configure system settings
- Manage users and permissions
- System maintenance

## Technical Business Constraints

- **Performance**: Support for high-traffic e-commerce sites
- **Scalability**: Horizontal scaling with stateless architecture
- **Security**: PCI compliance for payment processing
- **Reliability**: Transaction integrity for orders and payments
- **Availability**: 24/7 operation for global customers

## Success Metrics

Key business metrics tracked:
- **Conversion Rate**: Visitors to customers
- **Average Order Value**: Revenue per order
- **Cart Abandonment Rate**: Carts not converted to orders
- **Product Performance**: Sales by product/category
- **Customer Lifetime Value**: Revenue per customer over time
- **Inventory Turnover**: Stock movement efficiency

## Future Business Considerations

- **B2B Features**: Wholesale pricing, bulk orders, quotes
- **Subscription Products**: Recurring billing
- **Marketplace**: Multi-vendor support
- **Mobile Apps**: Native mobile applications
- **Advanced Analytics**: Business intelligence and reporting
- **Personalization**: AI-driven product recommendations
