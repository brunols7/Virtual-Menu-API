# Virtual Menu API

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
[![AWS](https://custom-icon-badges.demolab.com/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=aws&logoColor=white)](#)
[![Docker](https://img.shields.io/badge/Docker-2496ED.svg?style=for-the-badge&logo=docker&logoColor=white)](#)
[![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=fff)](#)

A RESTful API for a Virtual Restaurant Menu, built with the Spring Boot framework.

---

### Running the Application

The simplest way to run this application is by using Docker Compose, which will set up both the API container and the PostgreSQL database container.

**1. Clone the repository**

First, clone the project from GitHub to your local machine:

```
git clone https://github.com/brunols7/Virtual-Menu-API.git
cd Virtual-Menu-API
```

**2. Create the Environment File**

This project uses an `.env` file to manage environment variables, especially secrets. Create a file named `.env` in the root of the project.

Open the `.env` file and replace the placeholder values with your own, especially for the JWT secret.

```
# Database configuration for Docker Compose
# The 'postgres' hostname refers to the database service name in docker-compose.yml
DB_URL=jdbc:postgresql://postgres:5432/virtual_menu_db
DB_USER=your_db_user
DB_PASSWORD=your_strong_password

# JWT Secret Key for token generation
JWT_SECRET=your_super_secret_jwt_key_that_is_long_and_secure
```

**3. Build and Run with Docker Compose**

With Docker running on your machine, execute the following command from the project root. This command will build the Spring Boot application's Docker image and start the API and database containers.

```
docker-compose up --build
```

The API will be running and available at `http://localhost:8080`.

## Features

-   **RESTful API:** Built with **Spring Boot** following REST architecture principles.
-   **Secure Endpoints:** Secured with **Spring Security** and **JSON Web Tokens (JWT)** for authentication and authorization.
-   **Database:** Uses **PostgreSQL** for data persistence, managed via Spring Data JPA.
-   **API Documentation:** Interactive API documentation available through **SpringDoc OpenAPI (Swagger UI)**.
-   **Containerized:** Fully containerized using **Docker** and **Docker Compose** for a consistent and easy-to-run development environment.

## Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Make sure you have the following tools installed on your system:

-   Java Development Kit (JDK) 21
-   Apache Maven
-   Docker
-   Git

---

## API Documentation (Swagger)

Once the application is running, you can access the interactive API documentation provided by Swagger UI. This allows you to view all available endpoints and test them directly from your browser.

Navigate to the following URL:

[**http://localhost:8080/swagger-ui.html**](http://localhost:8080/swagger-ui.html)

You can use the `/auth/login` endpoint to authenticate and receive a JWT token, then use the "Authorize" button at the top of the Swagger page to include this token in subsequent requests to secured endpoints.

## Author

- [@brunols7](https://www.linkedin.com/in/brunols7/)
