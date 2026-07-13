# Dormitory Management REST API

## 1. General

**Base URL**

```http
/api/v1
```

**Content type**

```http
Content-Type: application/json
```

---

# 2. Buildings API

## 2.1 Get Buildings

```http
GET /api/v1/buildings
```

### Request

No request body.

Optional query parameters:

```http
GET /api/v1/buildings?keyword=A&status=ACTIVE&page=0&size=20
```

### Response

```json
[
  {
    "code": "A",
    "name": "Building A",
    "address": "Main campus",
    "numberOfFloors": 5,
    "description": "Male dormitory",
    "status": "ACTIVE"
  }
]
```

---

## 2.2 Get One Building

```http
GET /api/v1/buildings/{code}
```

### Request

Path parameter:

```text
code = A
```

### Response

```json
{
  "code": "A",
  "name": "Building A",
  "address": "Main campus",
  "numberOfFloors": 5,
  "description": "Male dormitory",
  "status": "ACTIVE"
}
```

---

## 2.3 Create Building

```http
POST /api/v1/buildings
```

### Request

```json
{
  "code": "A",
  "name": "Building A",
  "address": "Main campus",
  "numberOfFloors": 5,
  "description": "Male dormitory",
  "status": "ACTIVE"
}
```

### Response

```http
201 Created
```

```json
{
  "code": "A",
  "name": "Building A",
  "address": "Main campus",
  "numberOfFloors": 5,
  "description": "Male dormitory",
  "status": "ACTIVE"
}
```

---

## 2.4 Update Building

```http
PUT /api/v1/buildings/{code}
```

### Request

```json
{
  "code": "A",
  "name": "Building A1",
  "address": "Main campus",
  "numberOfFloors": 6,
  "description": "Updated building",
  "status": "ACTIVE"
}
```

### Response

```json
{
  "code": "A",
  "name": "Building A1",
  "address": "Main campus",
  "numberOfFloors": 6,
  "description": "Updated building",
  "status": "ACTIVE"
}
```

---

## 2.5 Delete Building

```http
DELETE /api/v1/buildings/{code}
```

### Request

No request body.

### Response

```http
204 No Content
```

---

# 3. Rooms API

## 3.1 Get Rooms

```http
GET /api/v1/rooms
```

### Request

No request body.

Optional filters:

```http
GET /api/v1/rooms?buildingCode=A&status=ACTIVE&availableOnly=true
```

### Response
`occupancyStatus`: "EMPTY" | "FULL" | "AVAILABLE".

```json
[
  {
    "id": 1,
    "roomNumber": "101",
    "buildingCode": "A",
    "buildingName": "Building A",
    "monthlyRent": 80.00,
    "maxOccupants": 4,
    "currentOccupants": 3,
    "availableBeds": 1,
	"occupancyStatus": "AVAILABLE",
    "status": "ACTIVE"
  }
]
```

---

## 3.2 Get One Room

```http
GET /api/v1/rooms/{id}
```

### Request

Path parameter:

```text
id = 1
```

### Response

```json
{
  "id": 1,
  "roomNumber": "101",
  "buildingCode": "A",
  "buildingName": "Building A",
  "monthlyRent": 80.00,
  "maxOccupants": 4,
  "currentOccupants": 3,
  "availableBeds": 1,
  "occupancyStatus": "AVAILABLE",
  "status": "ACTIVE"
}
```

---

## 3.3 Create Room

```http
POST /api/v1/rooms
```

### Request

```json
{
  "roomNumber": "101",
  "buildingCode": "A",
  "monthlyRent": 80.00,
  "maxOccupants": 4,
  "status": "ACTIVE"
}
```

### Response

```http
201 Created
```

```json
{
  "id": 1,
  "roomNumber": "101",
  "buildingCode": "A",
  "buildingName": "Building A",
  "monthlyRent": 80.00,
  "maxOccupants": 4,
  "currentOccupants": 0,
  "availableBeds": 4,
  "occupancyStatus": "EMPTY",
  "status": "ACTIVE"
}
```

---

## 3.4 Update Room

```http
PUT /api/v1/rooms/{id}
```

### Request

```json
{
  "roomNumber": "101",
  "buildingCode": "A",
  "monthlyRent": 90.00,
  "maxOccupants": 4,
  "status": "ACTIVE"
}
```

### Response

```json
{
  "id": 1,
  "roomNumber": "101",
  "buildingCode": "A",
  "buildingName": "Building A",
  "monthlyRent": 90.00,
  "maxOccupants": 4,
  "currentOccupants": 3,
  "availableBeds": 1,
  "occupancyStatus": "AVAILABLE",
  "status": "ACTIVE"
}
```

---

## 3.5 Delete Room

```http
DELETE /api/v1/rooms/{id}
```

### Request

No request body.

### Response

```http
204 No Content
```

---

# 4. Students API

## 4.1 Get Students

```http
GET /api/v1/students
```

### Request

No request body.

Optional filters:

```http
GET /api/v1/students?keyword=nguyen&buildingCode=A&roomId=1
```

### Response

```json
[
	{
	  "id": 1,
	  "studentCode": "STU20260001",
	  "fullName": "Nguyen Minh Anh",
	  "gender": "FEMALE",
	  "dateOfBirth": "2004-08-15",
	  "citizenId": "001204012345",
	  "phoneNumber": "0912345678",
	  "address": "123 Nguyen Trai Street, Thanh Xuan, Hanoi",
	  "className": "SE1801",
	  "major": "Software Engineering",
	  "hometown": "Hanoi",
	  "roomId": 101,
	  "roomNumber": "A-305",
	  "buildingCode": "BLD-A"
	}
]
```

---

## 4.2 Get One Student

```http
GET /api/v1/students/{id}
```

### Request

Path parameter:

```text
id = 15
```

### Response

```json
{
  "id": 1,
  "studentCode": "STU20260001",
  "fullName": "Nguyen Minh Anh",
  "gender": "FEMALE",
  "dateOfBirth": "2004-08-15",
  "citizenId": "001204012345",
  "phoneNumber": "0912345678",
  "address": "123 Nguyen Trai Street, Thanh Xuan, Hanoi",
  "className": "SE1801",
  "major": "Software Engineering",
  "hometown": "Hanoi",
  "roomId": 101,
  "roomNumber": "A-305",
  "buildingCode": "BLD-A"
}
```

---

## 4.3 Create Student

```http
POST /api/v1/students
```

### Request

```json
{
  "studentCode": "SV001",
  "fullName": "Nguyen Van A",
  "gender": "MALE",
  "dateOfBirth": "2004-02-12",
  "citizenId": "012345678901",
  "phoneNumber": "0912345678",
  "address": "123 Nguyen Trai Street, Thanh Xuan, Hanoi",
  "className": "CNTT-01",
  "major": "Software Engineering",
  "hometown": "Hanoi",
  "roomId": 101
}
```

### Response

```http
201 Created
```

```json
{
  "id": 1,
  "studentCode": "SV001",
  "fullName": "Nguyen Van A",
  "gender": "MALE",
  "dateOfBirth": "2004-02-12",
  "citizenId": "012345678901",
  "phoneNumber": "0912345678",
  "address": "123 Nguyen Trai Street, Thanh Xuan, Hanoi",
  "className": "CNTT-01",
  "major": "Software Engineering",
  "hometown": "Hanoi",
  "roomId": 101,
  "roomNumber": "A-305",
  "buildingCode": "BLD-A"
}
```

---

## 4.4 Update Student

```http
PUT /api/v1/students/{id}
```

### Request

```json
{
  "studentCode": "SV001",
  "fullName": "Nguyen Van A",
  "gender": "MALE",
  "dateOfBirth": "2004-02-12",
  "citizenId": "012345678901",
  "phoneNumber": "0912345678",
  "address": "123 Nguyen Trai Street, Thanh Xuan, Hanoi",
  "className": "CNTT-01",
  "major": "Software Engineering",
  "hometown": "Hanoi",
  "roomId": 101
}
```

### Response

```json
{
  "id": 1,
  "studentCode": "SV001",
  "fullName": "Nguyen Van A",
  "gender": "MALE",
  "dateOfBirth": "2004-02-12",
  "citizenId": "012345678901",
  "phoneNumber": "0912345678",
  "address": "123 Nguyen Trai Street, Thanh Xuan, Hanoi",
  "className": "CNTT-01",
  "major": "Software Engineering",
  "hometown": "Hanoi",
  "roomId": 101,
  "roomNumber": "A-305",
  "buildingCode": "BLD-A"
}
```

---

## 4.5 Assign or Change Room (Not implement yet)

```http
PATCH /api/v1/students/{id}/room
```

### Request

```json
{
  "roomId": 3
}
```

### Response

```json
{
  "id": 15,
  "fullName": "Nguyen Van A",
  "studentCode": "SV001",
  "room": {
    "id": 3,
    "roomNumber": "103",
    "buildingCode": "A"
  }
}
```

---

## 4.6 Remove Room Assignment (Not implement yet)

```http
DELETE /api/v1/students/{id}/room
```

### Request

No request body.

### Response

```http
204 No Content
```

---

## 4.7 Delete Student

```http
DELETE /api/v1/students/{id}
```

### Request

No request body.

### Response

```http
204 No Content
```

---

# 5. Staff API

## 5.1 Get Staff

```http
GET /api/v1/staff
```

### Request

No request body.

Optional filters:

```http
GET /api/v1/staff?keyword=pham&buildingCode=A
```

### Response

```json
[
	{
	  "id": 1,
	  "employeeCode": "EMP20260001",
	  "fullName": "Tran Quang Huy",
	  "gender": "MALE",
	  "dateOfBirth": "1990-05-20",
	  "citizenId": "001090012345",
	  "phoneNumber": "0987654321",
	  "address": "45 Le Loi Street, District 1, Ho Chi Minh City",
	  "assignedBuildingCode": "BLD-A",
	  "assignedBuildingName": "Sunrise Dormitory"
	}
]
```

---

## 5.2 Get One Staff Member

```http
GET /api/v1/staff/{id}
```

### Request

Path parameter:

```text
id = 1
```

### Response

```json
{
  "id": 1,
  "employeeCode": "EMP20260001",
  "fullName": "Tran Quang Huy",
  "gender": "MALE",
  "dateOfBirth": "1990-05-20",
  "citizenId": "001090012345",
  "phoneNumber": "0987654321",
  "address": "45 Le Loi Street, District 1, Ho Chi Minh City",
  "assignedBuildingCode": "BLD-A",
  "assignedBuildingName": "Sunrise Dormitory"
}
```

---

## 5.3 Create Staff Member

```http
POST /api/v1/staff
```

### Request

```json
{
  "employeeCode": "NV001",
  "fullName": "Tran Quang Huy",
  "gender": "MALE",
  "dateOfBirth": "1990-05-20",
  "citizenId": "012345678902",
  "phoneNumber": "0987654321",
  "address": "45 Le Loi Street, District 1, Ho Chi Minh City",
  "managedBuildingCode": "A"
}
```

### Response

```http
201 Created
```

```json
{
  "id": 1,
  "employeeCode": "NV001",
  "fullName": "Tran Quang Huy",
  "gender": "MALE",
  "dateOfBirth": "1990-05-20",
  "citizenId": "012345678902",
  "phoneNumber": "0987654321",
  "address": "45 Le Loi Street, District 1, Ho Chi Minh City",
  "assignedBuildingCode": "A",
  "assignedBuildingName": "Sunrise Dormitory"
}
```

---

## 5.4 Update Staff Member

```http
PUT /api/v1/staff/{id}
```

### Request

```json
{
  "employeeCode": "NV001",
  "fullName": "Tran Quang Huy",
  "gender": "MALE",
  "dateOfBirth": "1990-05-20",
  "citizenId": "012345678902",
  "phoneNumber": "0987654321",
  "address": "45 Le Loi Street, District 1, Ho Chi Minh City",
  "managedBuildingCode": "A"
}
```

### Response

```json
{
  "id": 1,
  "employeeCode": "NV001",
  "fullName": "Tran Quang Huy",
  "gender": "MALE",
  "dateOfBirth": "1990-05-20",
  "citizenId": "012345678902",
  "phoneNumber": "0987654321",
  "address": "45 Le Loi Street, District 1, Ho Chi Minh City",
  "assignedBuildingCode": "A",
  "assignedBuildingName": "Sunrise Dormitory"
}
```

---

## 5.5 Delete Staff Member

```http
DELETE /api/v1/staff/{id}
```

### Request

No request body.

### Response

```http
204 No Content
```

---

# 6. Authentication API (Not implement yet)

## 6.1 Login

```http
POST /api/v1/auth/login
```

### Request

```json
{
  "username": "admin",
  "password": "secret"
}
```

### Response

```json
{
  "accessToken": "jwt-token",
  "refreshToken": "refresh-token",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "id": 1,
    "username": "admin",
    "role": "ADMIN"
  }
}
```

---

## 6.2 Refresh Token

```http
POST /api/v1/auth/refresh
```

### Request

```json
{
  "refreshToken": "refresh-token"
}
```

### Response

```json
{
  "accessToken": "new-jwt-token",
  "refreshToken": "new-refresh-token",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

---

## 6.3 Get Current User

```http
GET /api/v1/auth/me
```

### Request

```http
Authorization: Bearer {accessToken}
```

### Response

```json
{
  "id": 1,
  "username": "admin",
  "role": "ADMIN"
}
```

---

# 7. Error Response

```json
{
    "timestamp": "2026-07-13T01:39:13.629822200Z",
    "status": 404,
    "error": "Not Found",
    "message": "Room not found: 9999",
    "path": "/api/rooms/9999",
    "fieldErrors": {}
}
```

Common status codes:

| Status | Meaning |
|---|---|
| `200` | Success |
| `201` | Created |
| `204` | Deleted successfully |
| `400` | Invalid request |
| `401` | Not authenticated |
| `403` | Access denied |
| `404` | Resource not found |
| `409` | Duplicate data or business conflict |
| `500` | Server error |

---
