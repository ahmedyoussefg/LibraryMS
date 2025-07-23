# 📚 LibraryMS

A Java console-based Library Management System built using OOP principles, Hibernate, and MySQL. It supports Admin and Regular User roles, with features for managing books and borrowing/returning operations.

## 🔧 Features

### Admin
- Add, delete, and view books
- Register new users (Admin or Regular)
- Search books by ID or title

### Regular User
- View and search the catalog
- Borrow and return books
- View borrowed books

## 🛠️ Tech Stack

- **Java 17+**
- **Hibernate ORM**
- **MySQL**
- **Jakarta Persistence API (JPA)**
- **dotenv-java** for environment variable support

## 🗂 Project Structure

```
org/
├── dao/ # Data Access Objects
├── marker/ # Marker interfaces (Borrowable, Searchable)
├── model/ # Entity classes: User, Admin, RegularUser, Book
├── service/ # Search functionality
├── util/ # Input & Hibernate utilities
├── LibrarySystem.java
└── Main.java
```


## 🧪 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/ahmedyoussefg/LibraryMS.git
cd LibraryMS
```

### 2. Setup MySQL
- Create a MySQL user with appropriate privileges.

- The app uses the database library-db (auto-created if not found).

- Add a .env file in the root directory with the following, make sure to add your MySQL password:
```
DB_PASSWORD=your_mysql_password
```
### 3. Run the Main class

## 📦 Dependencies
- `hibernate-core`
- `jakarta.persistence`
- `mysql-connector-java`
- `dotenv-java`

> Created by Ahmed Youssef