# 📚 Digital Library Management System

A robust, object-oriented **Digital Library Management System** CLI application written in Java. This project was developed as **Task 5** of the **Oasis InfoByte Internship (OIBSIP)**. It features a complete role-based system (Administrator & User), real-time transaction records, overdue fine calculation, dynamic report generation, data validation, and a command-line interface styled with ANSI colors.

---

## 🚀 Features

### 👤 User (Member) Portal
* **Browse Catalog:** View the complete list of books in the library with real-time status.
* **Search System:** Search for books by ID, Title, or Author (case-insensitive substring matches).
* **Availability Checker:** Instantly check if a specific book is available for borrowing or checked out.
* **Personal Checkout Records:** View current borrowed books, historical records, and due dates.
* **Overdue Fine Tracker:** Review current late fee summaries and outstanding penalties.

### 🔑 Administrator Portal
* **Catalog Management (CRUD):** 
  * Add new books to the catalog (with unique ID validation).
  * Update book details (Title/Author) dynamically.
  * Safely delete books (preventing deletion of currently issued books).
* **User Management:** Register new users, list all registered members, and add new administrators.
* **Lending Transactions:**
  * **Issue Books:** Check out books to registered users with automated due-date setting.
  * **Return Books:** Process returns and automatically calculate/log outstanding overdue fines.
* **Advanced Reports & Metrics:**
  * View total/available/issued book stats.
  * Generate summary statistics including outstanding fines, total fines collected, and active ratios.
  * Review an automated **Overdue Books List** highlighting late returns.

### ⚙️ Core Technical Highlights
* **File-Based Serialization:** Data is persistently saved across app restarts using Java's `ObjectOutputStream` / `ObjectInputStream` into `.ser` binary files under the `data/` directory.
* **ANSI Colored Terminal UI:** Styled UI banners, dividers, and console output text for warnings, errors, and successes.
* **Input Validation & Safety:** Robust checking for email format, numeric menu inputs with range bounds, mandatory fields, and invalid usernames.

---

## 📂 File Architecture

The codebase is organized cleanly as follows:

```
TASK5/
├── bin/                          # Compiled bytecode .class files
├── data/                         # Persistent binary data storage (.ser)
│   ├── admins.ser                # Admin credentials database
│   ├── books.ser                 # Book inventory database
│   ├── issues.ser                # Transaction & fine history database
│   └── users.ser                 # User accounts database
├── src/
│   └── com/
│       └── library/
│           ├── Admin.java        # Entity class representing an Administrator
│           ├── Authentication.java # Handles active login sessions and validation
│           ├── Book.java         # Entity class representing a Book
│           ├── FileManager.java  # Reads & writes .ser files using Object Serialization
│           ├── IssueRecord.java  # Entity class tracking checkouts, due dates, & fines
│           ├── Library.java      # Application controller & core business logic
│           ├── Main.java         # Application starter / entry point
│           ├── Menu.java         # Command line interface & menus flow logic
│           ├── User.java         # Entity class representing a library Member
│           └── Utility.java      # Inputs validation, scanners, and custom tables
└── README.md                     # Project documentation (this file)
```

---

## 🛠️ Requirements & Installation

### Prerequisites
* **Java Development Kit (JDK) 8 or higher** must be installed.
* Standard console/terminal with ANSI escape code support (standard on modern macOS, Linux, and Windows terminal applications).

### Compilation
Open a terminal in the project directory (`TASK5/`) and run the following command to compile all Java files into the `bin/` directory:

```bash
javac -d bin src/com/library/*.java
```

### Execution
Run the compiled application using the Java launcher:

```bash
java -cp bin com.library.Main
```

---

## 🔑 Default Credentials

When launching the application for the first time, the `data/` folder files will be created automatically. To log into the Administrator Portal for initial setup, use the following default credentials:

* **Username:** `admin`
* **Password:** `admin123`

*Note: Once logged in as an administrator, you can register new administrators and users directly through the console menu.*



