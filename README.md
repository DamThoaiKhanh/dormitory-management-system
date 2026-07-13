# Dormitory Management System

A web-based dormitory management system developed using Vue.js and Spring Boot.

The system helps administrators manage dormitory rooms, residents, payments, maintenance requests, and other dormitory-related operations.

## Features

* User authentication and authorization
* Resident management
* Room management
* Room assignment
* Check-in and check-out management
* Payment management
* Maintenance request management
* Dashboard and reports

## Technologies

### Frontend

* Vue.js
* Vue Router
* Pinia
* Axios
* HTML
* CSS
* JavaScript

### Backend

* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* REST API
* Maven

### Database

* MySQL

## Project Structure

```text
dormitory-management-system/
├── frontend/       # Vue.js frontend application
├── backend/        # Spring Boot backend application
├── docs/           # Project documentation
└── README.md
```

## Getting Started

### Prerequisites

Make sure the following software is installed:

* Node.js
* npm
* Java JDK
* Maven
* MySQL

## Frontend Setup

Navigate to the frontend directory:

```bash
cd frontend
```

Install the dependencies:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

The frontend will normally run at:

```text
http://localhost:5173
```

## Backend Setup

Navigate to the backend directory:

```bash
cd backend
```

Configure the database connection in:

```text
backend/src/main/resources/application.properties
```

Example configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dormitory_management
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Run the Spring Boot application:

```bash
mvn spring-boot:run
```

The backend will normally run at:

```text
http://localhost:8080
```
 
