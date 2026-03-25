# MySQL Docker Setup for Shopizer Backend

## Quick Start

Start the MySQL container:
```bash
docker-compose up -d
```

Stop the container:
```bash
docker-compose down
```

## Database Configuration

- **Database Name**: SALESMANAGER
- **User**: shopizer
- **Password**: very-long-shopizer-password
- **Port**: 3306

## Data Seeding

The application includes an automatic data seeder (`InitializationLoader.java`) that runs on startup when the database is empty.

### What Gets Seeded:

- Security groups and permissions (AUTH, SUPERADMIN, ADMIN, PRODUCTS, ORDER, CONTENT, STORE)
- Languages
- Countries and zones
- Currencies
- Tax classes
- Default merchant store
- Default admin user
- Integration modules
- Merchant configuration

### Control Seeding:

The seeder is controlled by the `db.init.data` property (default: `true`).

To disable seeding, set in your application properties:
```properties
db.init.data=false
```

## Connecting Application to MySQL

Update your `database.properties` file (e.g., `sm-shop/src/main/resources/profiles/mysql/database.properties`):

```properties
db.jdbcUrl=jdbc:mysql://localhost:3306/SALESMANAGER?autoReconnect=true&serverTimeZone=UTC&useUnicode=true&characterEncoding=UTF-8
db.user=shopizer
db.password=very-long-shopizer-password
db.driverClass=com.mysql.cj.jdbc.Driver
hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
hibernate.hbm2ddl.auto=update
```

## Volume Persistence

Database data is persisted in the `mysql_data` Docker volume. To reset the database:

```bash
docker-compose down -v
docker-compose up -d
```
