# MySQL Docker Setup - Execution Summary

## ✅ Completed Tasks

### 1. MySQL Docker Container
- **Status**: Running successfully
- **Container Name**: shopizer-mysql
- **Image**: mysql:8.0
- **Port**: 3306
- **Database**: SALESMANAGER
- **User**: shopizer
- **Password**: very-long-shopizer-password

### 2. Data Seeder Execution
- **Status**: Successfully executed
- **Seeder Class**: `InitializationLoader.java`
- **Trigger**: Runs automatically on startup when `db.init.data=true` and database is empty

## 📊 Seeded Data Summary

| Data Type | Count | Description |
|-----------|-------|-------------|
| **Tables** | 89 | All database tables created |
| **Languages** | 3 | English, French, German |
| **Countries** | 238 | All supported countries |
| **Currencies** | 158 | All supported currencies |
| **Zones** | ~500+ | Geographic zones |
| **Merchant Store** | 1 | Default store (code: DEFAULT) |
| **Admin User** | 1 | admin@shopizer.com |
| **Permissions** | 7 | AUTH, SUPERADMIN, ADMIN, PRODUCTS, ORDER, CONTENT, STORE |
| **Groups** | Multiple | Security groups with permissions |
| **Tax Classes** | 1 | Default tax class |
| **Manufacturers** | 1 | Default manufacturer |
| **Product Types** | 1 | General product type |
| **Module Configurations** | 13 | Payment, shipping, and integration modules |

## 🔑 Default Admin Credentials

- **Username**: admin@shopizer.com
- **Password**: password (default - should be changed)
- **First Name**: Administrator
- **Last Name**: User

## 📝 What the Seeder Created

### Security & Permissions
- 7 permission types (AUTH, SUPERADMIN, ADMIN, PRODUCTS, ORDER, CONTENT, STORE)
- Security groups with appropriate permissions
- Default admin user with SUPERADMIN and ADMIN roles

### Reference Data
- Languages (English, French, German)
- 238 countries with descriptions
- 158 currencies
- Geographic zones for all countries
- Tax classes

### Store Configuration
- Default merchant store (code: DEFAULT)
- Store name: "Shopizer"
- Domain: localhost:8080
- Email: contact@shopizer.com
- Default manufacturer
- Product types
- Integration modules (payment, shipping, etc.)

## 🚀 How to Use

### Start MySQL Container
```bash
cd /Users/pranjalgupta/local\ disk/Technogise/shopizer-suite/shopizer
docker-compose up -d
```

### Stop MySQL Container
```bash
docker-compose down
```

### Reset Database (Delete all data)
```bash
docker-compose down -v
docker-compose up -d
```

### Run Application with Seeder
```bash
cd sm-shop
../mvnw spring-boot:run -Dspring-boot.run.profiles=mysql -Dspring-boot.run.jvmArguments="-Ddb.init.data=true"
```

### Connect to MySQL
```bash
docker exec -it shopizer-mysql mysql -u shopizer -p'very-long-shopizer-password' SALESMANAGER
```

## 📂 Files Created

1. **docker-compose.yml** - MySQL container configuration
2. **DOCKER-MYSQL-SETUP.md** - Setup documentation
3. **MYSQL-SEEDER-SUMMARY.md** - This file

## ⚙️ Configuration Files Modified

- `sm-shop/src/main/resources/profiles/mysql/database.properties` - Enabled MySQL connection properties

## 🔍 Verification Queries

```sql
-- Check all tables
SHOW TABLES;

-- Check seeded data counts
SELECT COUNT(*) FROM LANGUAGE;
SELECT COUNT(*) FROM COUNTRY;
SELECT COUNT(*) FROM CURRENCY;
SELECT COUNT(*) FROM MERCHANT_STORE;
SELECT COUNT(*) FROM USERS;

-- View admin user
SELECT * FROM USERS;

-- View merchant store
SELECT * FROM MERCHANT_STORE;

-- View permissions
SELECT * FROM PERMISSION;
```

## 📌 Notes

- The seeder only runs when the database is empty (checked via `languageService.count() == 0`)
- To re-run the seeder, you must delete the database volume and restart
- The seeder is controlled by the `db.init.data` property (default: true)
- All data is persisted in the Docker volume `shopizer_mysql_data`
