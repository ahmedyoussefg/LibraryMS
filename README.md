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
- **Docker & Docker Compose** for containerization

## 📦 Prerequisites
- [Docker](https://docs.docker.com/get-docker/)  
- [Docker Compose](https://docs.docker.com/compose/install/)  

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

### 2. Create a .env file in the project root with the following variables:
```bash
DB_USER=your_mysql_username
DB_PASSWORD=your_mysql_password
DB_NAME=library-db
DB_HOST=db
DB_PORT=3306
```

### 3. Start the Application using Docker Compose

```bash
docker compose up --build
```
This will:

- Build the application container

- Set up a MySQL database

- Launch Adminer for database management at `localhost:8080`

### 4. Use the Library Management System

```bash
docker compose run app
```

- Adminer (Database Management)
    - Available at http://localhost:8080

    - System: MySQL

    - Server: db

    - Username: as in .env

    - Password: as in .env

    - Database: library-db

## 🛠️ Troubleshooting Tips
###
Check Running Containers
Use the following command to view active containers:
```bash
docker ps
```
### Permission Denied for Docker Commands

Use `sudo` Or add your user to the docker group:

```bash
sudo usermod -aG docker $USER
newgrp docker
```

> Created by Ahmed Youssef