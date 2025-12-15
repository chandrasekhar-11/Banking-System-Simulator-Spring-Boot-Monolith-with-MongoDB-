# Banking System Simulator — Spring Boot + MongoDB

A clean and modular monolithic backend application built using **Spring Boot** and **MongoDB**.  
It simulates real-world banking operations such as account creation, updates, deletions, deposits, withdrawals, transfers, and transaction history retrieval.

This project is designed as a transition from a **Core Java Banking System** to a **Spring Boot RESTful Backend** with data persistence, validation, logging, and testing.

---

## 🚀 Features
- Create and manage bank accounts (create, retrieve, update, delete)
- Deposit, withdraw, and transfer money
- Generate unique account numbers & transaction IDs
- Store data in MongoDB collections (accounts & transactions)
- View transaction history for any account
- Centralized exception handling with custom error responses
- DTO-based request/response models with validation
- JUnit + Mockito service-layer unit tests

---

## 🏗️ Architecture Overview

```
com.bankingsystem.simulator
│
├── controller      → REST API endpoints
├── service
│     ├── impl      → core business logic
├── repository      → MongoDB operations
├── model
│     ├── entity    → @Document classes (Account, Transaction)
│     └── dto       → request/response objects
├── exception       → custom exceptions + global handler
└── util            → ID generators and utility classes
```

**Flow:**  
Client → Controller → Service → Repository → MongoDB → Service → Controller → Client

---

## 📘 API Endpoints

### **Account APIs**
| Method  | Endpoint                                  | Description |
|---------|--------------------------------------------|-------------|
| POST    | `/api/accounts`                            | Create a new bank account |
| GET     | `/api/accounts/{accountNumber}`            | Get account details |
| PATCH   | `/api/accounts/{accountNumber}`            | Update account details (name/status) |
| DELETE  | `/api/accounts/{accountNumber}`            | Delete account (soft delete → INACTIVE) |
| GET     | `/api/accounts/{accountNumber}/transactions` | Get all transactions for an account |

---

### **Transaction APIs**
| Method | Endpoint                                           | Description |
|--------|-----------------------------------------------------|-------------|
| PUT    | `/api/accounts/{accountNumber}/deposit`            | Deposit funds |
| PUT    | `/api/accounts/{accountNumber}/withdraw`           | Withdraw funds |
| POST   | `/api/accounts/transfer`                           | Transfer money |

---

## 📥 Example Requests (Using Your Name: Chandrasekhar)

### ✔ Create Account
```json
POST /api/accounts
{
  "holderName": "Chandrasekhar"
}
```

---

### ✔ Update Account (Name & Status)
```json
PATCH /api/accounts/CH1234
{
  "holderName": "Chandrasekhar R",
  "status": "ACTIVE"
}
```

---

### ✔ Delete Account (Soft Delete)
```http
DELETE /api/accounts/CH1234
```

Expected response:
```
204 No Content
```

---

### ✔ Deposit Amount
```json
PUT /api/accounts/CH1234/deposit
{
  "amount": 500
}
```

---

### ✔ Withdraw Amount
```json
PUT /api/accounts/CH1234/withdraw
{
  "amount": 100
}
```

---

### ✔ Transfer Money
```json
POST /api/accounts/transfer
{
  "sourceAccount": "CH1234",
  "destinationAccount": "CR5678",
  "amount": 200
}
```

---

## 🗄️ MongoDB Collection Examples

### `accounts`
```json
{
  "accountNumber": "CH1234",
  "holderName": "Chandrasekhar",
  "balance": 1500.0,
  "status": "ACTIVE",
  "createdAt": "2025-11-17T10:25:00Z"
}
```

### `transactions`
```json
{
  "transactionId": "TXN-20251117-001",
  "type": "DEPOSIT",
  "amount": 500,
  "timestamp": "2025-11-17T10:26:00Z",
  "status": "SUCCESS",
  "sourceAccount": "CH1234",
  "destinationAccount": null
}
```

---

## ⚙️ Setup Instructions

### **Prerequisites**
- Java 17+
- Maven 3+
- MongoDB running locally (`localhost:27017`)

---

### **1️⃣ Clone the Project**
```bash
git clone <your-repository-url>
cd banking-system-simulator
```

---

### **2️⃣ Build the Project**
```bash
mvn clean install
```

---

### **3️⃣ Run the Application**
```bash
mvn spring-boot:run
```

Runs at:
```
http://localhost:8080
```

---

### **4️⃣ MongoDB Configuration**
Inside `src/main/resources/application.properties`:

```
spring.mongodb.uri=mongodb://localhost:27017/Your_Database_Name
```

---

## 🧪 Unit Testing

Run all tests:

```bash
mvn test
```

Covers:
- Service-layer logic
- Mockito repository mocking
- Validation scenarios
- Error handling for invalid amounts, missing accounts, etc.

---

## 🛠️ Technologies Used
- Spring Boot 3
- Spring Data MongoDB
- Java 17
- Lombok
- Maven
- JUnit 5 + Mockito

---

# ✅ End of README
This file is ready to place directly in your project root.

