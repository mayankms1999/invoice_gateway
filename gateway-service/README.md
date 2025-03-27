# Invoice & Product Gateway Service - README

## Overview
This project serves as an API Gateway for the Invoice and Product services. It routes requests to the appropriate microservices and ensures seamless communication between them.

## Base URL
The API Gateway acts as a single entry point for accessing the services:
```
http://your-domain.com/
```

---

## Invoice Service Routes

### 1. Create an Invoice
**Endpoint:** `POST /api/invoices/create`

**Request Body:**
```json
{
  "user": {
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "1234567890"
  },
  "products": [
    {
      "productId": 1,
      "productName": "Laptop",
      "quantity": 1,
      "price": 1500.99
    }
  ]
}
```

**Response:**
```json
{
  "id": 1,
  "invoiceNumber": "abc-123",
  "createdAt": "2025-03-26T12:00:00",
  "items": [ ],
  "userDetail": { }
}
```

**Status Codes:**
- `201 Created`: Invoice successfully created
- `400 Bad Request`: Validation error

---

### 2. Get Invoice by ID
**Endpoint:** `GET /api/invoices/get/{id}`

**Response:**
```json
{
  "id": 1,
  "invoiceNumber": "abc-123",
  "createdAt": "2025-03-26T12:00:00",
  "items": [ ],
  "userDetail": { }
}
```

**Status Codes:**
- `200 OK`: Invoice found
- `404 Not Found`: Invoice does not exist

---

### 3. Get All Invoices
**Endpoint:** `GET /api/invoices/get-all`

**Response:**
```json
[
  {
    "id": 1,
    "invoiceNumber": "abc-123",
    "createdAt": "2025-03-26T12:00:00",
    "items": [ ],
    "userDetail": { }
  }
]
```

**Status Codes:**
- `200 OK`: List of invoices retrieved successfully
- `204 No Content`: No invoices found

---

### 4. Delete Invoice Item
**Endpoint:** `DELETE /api/invoices/delete/{itemId}`

**Status Codes:**
- `204 No Content`: Invoice item successfully deleted
- `404 Not Found`: Invoice item does not exist

---

## Product Service Routes

### 1. Create a Product
**Endpoint:** `POST /api/product`

**Request Body:**
```json
{
  "name": "Laptop",
  "description": "A high-performance laptop.",
  "hsnCode": "123456",
  "price": 1500.99
}
```

**Response:**
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "A high-performance laptop.",
  "hsnCode": "123456",
  "price": 1500.99
}
```

**Status Codes:**
- `201 Created`: Product successfully created
- `400 Bad Request`: Validation error

---

### 2. Get All Products
**Endpoint:** `GET /api/product`

**Response:**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "A high-performance laptop.",
    "hsnCode": "123456",
    "price": 1500.99
  }
]
```

**Status Codes:**
- `200 OK`: List of products retrieved successfully

---

### 3. Get Product by ID
**Endpoint:** `GET /api/product/{id}`

**Response:**
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "A high-performance laptop.",
  "hsnCode": "123456",
  "price": 1500.99
}
```

**Status Codes:**
- `200 OK`: Product found
- `404 Not Found`: Product does not exist

---

### 4. Get Products by Name
**Endpoint:** `GET /api/product/by-name?name={name}`

**Response:**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "A high-performance laptop.",
    "hsnCode": "123456",
    "price": 1500.99
  }
]
```

**Status Codes:**
- `200 OK`: Products retrieved successfully
- `404 Not Found`: No product found

---

### 5. Delete Product
**Endpoint:** `DELETE /api/product/{id}`

**Status Codes:**
- `204 No Content`: Product successfully deleted
- `404 Not Found`: Product does not exist

---

## Error Handling

### Common Error Responses

**400 Bad Request:**
```json
{
  "error": "Validation failed",
  "message": "Product name is required"
}
```

**404 Not Found:**
```json
{
  "error": "Not Found",
  "message": "Product not found with ID: 1"
}
```

---

## Deployment
This API Gateway can be deployed alongside microservices using Docker and Kubernetes. Ensure the correct ports and service names are configured in the application properties.

---

## License
This project is licensed under MIT.

