# 🏠 StayEasy — Property Rental Platform

## Tech Stack
- **Java** 17
- **Spring Boot** 3.4.0
- **Spring MVC** + **Thymeleaf**
- **Spring Data JPA** + **Hibernate**
- **MySQL** (database)
- **Maven** (build tool)
- **Lombok**

## Features & Functionalities

### Roles
| Role | Permissions |
|------|-------------|
| **Guest** | Browse properties, view details, register, login |
| **Renter** | Make reservations, cancel reservations, write reviews |
| **Host** | List/edit/delete properties, approve/reject reservations |
| **Admin** | Manage all users and listings |

### Domain Functionalities
1. **Create a reservation** — Renters can book available properties
2. **Cancel a reservation** — Renters can cancel their pending bookings
3. **Manage property listings** — Hosts can create, edit, and delete properties
4. **Approve / Reject reservation** — Hosts review incoming booking requests
5. **Write a review** — Renters can review a property after checkout

### Pages
| Page | Type |
|------|------|
| Home | Static |
| About | Static |
| Browse Properties | Dynamic |
| Property Details | Dynamic |
| Register / Login | Dynamic |
| My Reservations | Dynamic |
| Host Dashboard | Dynamic |
| Add / Edit Property | Dynamic |

## Domain Entities
- `User` — platform users (renters, hosts, admins)
- `Property` — listings created by hosts
- `Reservation` — bookings made by renters
- `Review` — reviews left by renters after checkout
- `Amenity` — features of a property (Wi-Fi, parking, etc.)

## Setup Instructions

1. Create a MySQL database:
   ```sql
   CREATE DATABASE stayeasy;
   ```

2. Update `application.properties` with your MySQL credentials:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Open your browser at: [http://localhost:8080](http://localhost:8080)
