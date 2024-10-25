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
