# Support Desk (Spring Boot + MySQL)

Users raise tickets at `/`, admin replies at `/admin.html`.

## Run locally (no Docker)
1. Install Java 17, Maven, MySQL 8 and start MySQL.
2. `mvn spring-boot:run` (defaults: DB root/root on localhost, admin/admin123)
3. Open http://localhost:8080

## Run with Docker (also the deployment method)
    export DB_PASS='strong-db-password' ADMIN_USER=admin ADMIN_PASS='strong-admin-password'
    docker compose up -d --build
Open http://SERVER-IP/ and http://SERVER-IP/admin.html

## Environment variables
DB_URL, DB_USER, DB_PASS, ADMIN_USER, ADMIN_PASS, PORT
