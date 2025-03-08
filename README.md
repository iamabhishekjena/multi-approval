# Multi-Approval System API Documentation

## Overview

This API provides endpoints for managing tasks and user authentication in the Multi-Approval system. It allows users to create, approve, and comment on tasks while also managing user authentication actions like signup, login, and logout.

---

## Task Controller

### Base URL: `/api/tasks`

### **1. Create Task**

**Endpoint:** `POST /create`

**Description:** Creates a new task in the system.

**Headers:**

```http
Content-Type: application/json
Accept: application/json
```

**Request Body:**

```json
{
  "loginId": "string",
  "description": "string",
  "approverLoginIds": "List<string>"
}
```
**example:**

```json
{
    "loginId": "USER06268596",
    "description": "Approve budget for Q3",
    "approverLoginIds": [
        "USER23836281",
        "USER00672277",
        "USER31820477"
    ]
}
```
**Response:**


{
  "message": "Task created and assigned successfully with task Id: 24"
}


- `200 OK`: Task created successfully.
- `400 Bad Request`: Invalid request data.

---

### **2. Approve Task**

**Endpoint:** `POST /approve`

**Description:** Approves a task by a user.

**Query Parameters:**

- `taskId` (int, required) - The ID of the task to approve.
- `loginId` (string, required) - The ID of the user approving the task.

**Response:**

```json
{
  "message": "Task approved successfully"
}
```

- `200 OK`: Task approved successfully.
- `404 Not Found`: Task not found.

---

### **3. Add Comment**

**Endpoint:** `POST /addComment`

**Description:** Adds a comment to a task.

**Query Parameters:**

- `taskId` (int, required) - The ID of the task.
- `loginId` (string, required) - The ID of the user adding the comment.
- `comment` (string, required) - The comment text.

**Response:**

```json
{
  "message": "Comment added successfully"
}
```

- `200 OK`: Comment added successfully.
- `400 Bad Request`: Invalid input.

---

## User Controller

### Base URL: `/api/users`

### **1. User Signup**

**Endpoint:** `POST /signup`

**Description:** Registers a new user in the system.

**Headers:**

```http
Content-Type: application/json
Accept: application/json
```

**Request Body:**

```json
{
  "name": "string",
  "email": "string"
}
```

**Response:**

```json
{
  "message": "User registered successfully with Login ID: USER31820477"
}
```

- `200 OK`: User signed up successfully.
- `400 Bad Request`: Invalid request data.

---

### **2. User Login**

**Endpoint:** `POST /login`

**Description:** Logs in a user to the system.

**Query Parameters:**

- `loginId` (string, required) - The login ID of the user.

**Response:**

```json
{
  "message": "Login successful"
}
```

- `200 OK`: Login successful.
- `400 Bad Request`: User already logged in

---

### **3. User Logout**

**Endpoint:** `POST /logout`

**Description:** Logs out a user from the system.

**Query Parameters:**

- `loginId` (string, required) - The login ID of the user.

**Response:**

```json
{
  "message": "Logout successful"
}
```

- `200 OK`: Logout successful.

---

## Setup Instructions

### Prerequisites

- Java 17+
- Maven
- Spring Boot
- MySQL/PostgreSQL Database (Optional, if needed for persistence)

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/multi-approval.git
   ```
2. Navigate to the project directory:
   ```sh
   cd multi-approval
   ```
3. Configure the database (if applicable) in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/multi_approval
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   ```
4. Build the project:
   ```sh
   mvn clean install
   ```
5. Run the application:
   ```sh
   mvn spring-boot:run
   ```

### Notes

- All endpoints return responses in JSON format.
- Proper authentication mechanisms should be implemented for securing endpoints.
- Error handling and validation should be performed on the client-side before making API calls.

