# Task Management System
A Simple Task Management System similar to Trello

---

## 🧩 Project Description
This project is a task management system built using Spring Boot.
It allows users to create, assign, and manage tasks, similar to Trello.
The application supports authentication, user roles and email notifications.

---

## 🚀 Features

- Task Management (Create, update, delete, assign and unassign employees to/from tasks)
- Employee Management (Create, update, delete)
- Task Status Updates (Change task status dynamically)
- Authentication & Authorization (JWT, Spring Security, role-based access control)
- Notifications (Email)
- REST API Documentation (Swagger UI)
- Database Migrations (Flyway for schema management)
- Docker Support (Dockerfile & Docker Compose)

---

## 🧰 Tech stack used in this project
- **Java 21**
- **Spring Boot** (Web, Security, Data JPA)
- **Hibernate** (ORM) 
- **Spring Security + JWT** (Authentication)
- **PostgreSQL** (Database)
- **Flyway** (Database migrations)
- **Lombok** (for reducing boilerplate code)
- **Maven** (Build tool)
- **Swagger UI** (API Documentation)
- **Java Mail Sender** (Email notifications)
- **Docker** (Containerization)
- **JUnit & Mockito** (Testing)

---

## 📂 Project Structure
```
src/main/java/com/moldavets/task_management_system
├── auth                  # Authentication (JWT, Security)
│   ├── config            # Security configuration
│   ├── controller        # Login/Register API
│   ├── dto               # Request/Response DTOs
│   ├── exception         # Custom exceptions
│   ├── mapper            # Entity-DTO Mappers
│   ├── model             # User entity
│   ├── repository        # Repository layer
│   ├── service           # Business logic layer
│   ├── utils             # Utility classes
│
├── email                 # Email sending logic
│
├── employee              # Employee management
│   ├── controller
│   ├── dto
│   ├── mapper
│   ├── model
│   ├── repository
│   ├── service
│
├── task                  # Task management
│   ├── controller
│   ├── dto
│   ├── mapper
│   ├── model
│   ├── repository
│   ├── service
│
├── exception             # Global exception handling
│   ├── handler
│   ├── model
│
├── utils                 # Common utilities
│
resources
├── db.migration          # Flyway migration scripts
│   ├── V1__init.sql      # Initial schema setup
│
└── application.properties # Application settings

Docker
├── Dockerfile            # Docker image setup
├── docker-compose.yaml   # Container orchestration
```

---

## ⚙️ Installation and Setup

**1. Clone the Repository**

```
https://github.com/BohdanMoldavets/task-management-system.git
cd task-management-system
```

**2. Install Dependencies**
```
mvn clean package
mvn install
```

**3. Edit the docker-compose.yaml file**
```
FOLLOW THIS LINK TO SET UP NEXT PROPS - https://support.google.com/accounts/answer/185833?hl=en
SPRING_MAIL_USERNAME:
SPRING_MAIL_PASSWORD:
```

**4. Run the application with Docker**
```
docker-compose up --build
```

## 📝 API Documentation
Once the application is running, the Swagger UI will be available at:
http://localhost:8080/swagger-ui/index.html

# Contact
+ Email: [steamdlmb@gmail.com](mailto:steamdlmb@gmail.com)
+ [Telegram](https://telegram.me/moldavets)
+ [Linkedin](https://www.linkedin.com/in/bohdan-moldavets/)