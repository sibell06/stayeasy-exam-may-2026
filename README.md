# StayEasy 🏠

A property rental web application built with Spring Boot, allowing users to browse, list, and book properties.

## Tech Stack

- **Java 17**
- **Spring Boot 3.4.0**
- **Spring MVC + Thymeleaf**
- **Spring Data JPA**
- **MySQL 8.0**
- **BCrypt** (via Spring Security Crypto)
- **Maven**
- **Lombok**

## Features

### Authentication
- User registration with BCrypt password hashing
- Session-based login/logout
- Access control based on session

### Properties
- Browse all available properties
- View property details with reviews
- List a new property (logged-in users)
- Edit and delete your own property (host only)

### Reservations
- Book a property with check-in/check-out dates and guest count
- View your reservations with status (PENDING, APPROVED, REJECTED, CANCELLED)
- Cancel a pending reservation (renter only)
- Host dashboard to approve or reject reservations

### Reviews
- Leave a review with a rating (1-5 stars) for a property
- One review per user per property
- Delete your own review

### Validation
- Server-side form validation on all forms
- Red error messages displayed next to invalid fields
- Business rule enforcement (past dates blocked, guest limits enforced)

## Domain Entities

- **User** — stores account info, role (RENTER), and BCrypt hashed password
- **Property** — title, description, location, price, type, bedrooms, bathrooms, max guests
- **Reservation** — check-in/check-out dates, guests, total price, status
- **Review** — content, rating (1-5), linked to property and author

## Access Control

| Page | Guest | Logged In |
|------|-------|-----------|
| Home, Browse, About | ✅ | ✅ |
| Register, Login | ✅ | ✅ |
| Property Details | ✅ | ✅ |
| Add Property | ❌ | ✅ |
| Edit/Delete Property | ❌ | Host only |
| Book Property | ❌ | ✅ |
| My Reservations | ❌ | ✅ |
| Host Dashboard | ❌ | Host only |
| Leave/Delete Review | ❌ | ✅ |

## Setup

### Prerequisites
- Java 17
- MySQL 8.0
- Maven

### Database
Create a MySQL database:
```sql
CREATE DATABASE stayeasy;
```

### Configuration
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/stayeasy
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Run
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Git Repository

[GitHub](https://github.com/sibell06/stayeasy-exam-may-2026)