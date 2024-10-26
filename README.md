# spring-jwt-demo
Spring Boot JWT Demo with MongoDB Integration

## Features

- **JWT Authentication**: Users can authenticate using JWT tokens.
- **MongoDB Integration**: MongoDB is used to store user data.
- **Role-Based Access Control**: Provides access control based on user roles (`ADMIN`, `USER`).
- **Spring Security**: Uses `SecurityContextHolder` to manage security context and protect endpoints.

## Setup and Run

### Step 1: Clone the Repository

First, clone the repository to your local machine:

```bash
git clone https://github.com/your-username/spring-jwt-demo.git
cd spring-jwt-demo
```

### Step 2: Run MongoDB using Docker

Run a MongoDB container using the following Docker command:

```bash
docker run -d --name "MongoDB_4.4.29" -p 27017:27017 mongo:4.4.29
```

This command will:
- Start a MongoDB container in the background.
- Expose MongoDB on port `27017`, which is the default port for MongoDB.

### Step 3: Configure application.properties

In your `application.properties` file, ensure the MongoDB connection URI is correctly set up to connect to your local MongoDB instance:
```application.properties
#mongoDB-related configuration
spring.data.mongodb.uri=mongodb://localhost:27017/mydb
```

Also, it is important to set up JWT related configuration.

```application.properties
#jwt-related configuration
jwt.secret-key=1234567890abcdefghij9876543210ji
jwt.valid-seconds=120
```


### Step4: Test the Application

You can use curl command or API testing tools (Postman, Apidog...) to test the application.

1. Register a new user (POST /users)
- Request body
```json
  {
    "username": "newuser",
    "password": "password123",
    "nickname": "New User",
    "authorities": ["ADMIN", "USER"]
  }
```
2. User login (POST /auth/login)
- Request body
```json
  {
  "username": "newuser",
  "password": "password123"
  }
```
3. Get current user info (GET /me)
- JWT Required
4. Get all users (GET /users)
- JWT Required
5. Delete a user (DELETE /users/{id})
- JWT Required

**API Endpoints Summary**

| Method | Endpoint         | Description                                 | Authentication Required | Roles     |
|--------|------------------|---------------------------------------------|-------------------------|-----------|
| POST   | `/users`          | Register a new user                         | No                      | Any       |
| POST   | `/auth/login`     | Login and get JWT                           | No                      | Any       |
| GET    | `/me`             | Get current user information                | Yes                     | Any       |
| GET    | `/users`          | Get all users                               | Yes                     | Any       |
| DELETE | `/users/{id}`     | Delete a user by ID                         | Yes                     | ADMIN     |

## Deploy to Docker

### Prerequisites
- **Docker**: You need Docker installed to run both MongoDB and your Spring Boot application in containers.
- **Maven**: Make sure Maven is installed to build the Spring Boot application.

### Step to Deploy
1. Package the Spring Boot Application
First, package the Spring Boot application into a JAR file using Maven.
```bash
# Clean previous builds and package the application
mvn clean package
```
After this command runs, a JAR file will be created in the `target/ directory`, for example, `spring-jwt-demo-0.0.1-SNAPSHOT.jar`.

2. Create a Dockerfile
Create a `Dockerfile` in the root directory of the project to define how Docker should build and run the Spring Boot application.
```Dockerfile
# Use OpenJDK 22 (or what you prefered) as the base image
FROM openjdk:22-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/spring-jwt-demo-0.0.1-SNAPSHOT.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
```

3. Build the Docker Image
Use the `Dockerfile` to build a Docker image for the Spring Boot application.
```bash
# Build the Docker image and tag it as "spring-jwt-demo"
docker build -t spring-jwt-demo .
```

### Step to Test
1. Run MongoDB in Docker
Next, you need to run MongoDB in a separate Docker container. The following command will start MongoDB and expose its port to the local machine.
```bash
# Run MongoDB and expose it on port 27017
docker run -d --name mongodb -p 27017:27017 mongo:4.4.29
```

2. Run the Spring Boot Application in Docker
Now, run the Spring Boot application in its own Docker container and connect it to MongoDB.

The Spring Boot application is configured to connect to MongoDB using the following connection string in `application.properties`:
```application.properties
spring.data.mongodb.uri=mongodb://host.docker.internal:27017/mydb
```

Run the application container with the following command:
```bash
# Run the Spring Boot application and expose it on port 8080
docker run -p 8080:8080 spring-jwt-demo
```

3. Once both containers are running (Spring Boot and MongoDB), you can test the application by accessing it in your browser or using tools like Postman.
- Access the application at `http://localhost:8080`.
- Make sure the Spring Boot application can interact with MongoDB.
