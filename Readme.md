# ☕ Covvee — Collaborative Cloud IDE

**Covvee** is a lightweight, cloud-native web code editor designed for students, hobbyists, and remote teams. It allows users to write, run, and collaborate on code directly in the browser without installing any local software.

![Project Status](https://img.shields.io/badge/status-development-orange?style=flat-square)
![Backend](https://img.shields.io/badge/backend-Spring%20Boot%203-green?style=flat-square&logo=springboot)
![Database](https://img.shields.io/badge/database-MongoDB-green?style=flat-square&logo=mongodb)
![Execution](https://img.shields.io/badge/execution-Docker%20Sandbox-blue?style=flat-square&logo=docker)
![License](https://img.shields.io/badge/license-MIT-lightgrey?style=flat-square)

---

## 📖 Table of Contents
- [Architecture](#-architecture)
- [Tech Stack](#-tech-stack)
- [Database Schema](#-database-schema-entities)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-endpoints-snapshot)
- [Security Features](#-security--validation-features)

---

## 🏗️ Architecture

Covvee uses a **Modular Monolith** architecture to ensure simplicity and performance during the MVP phase, with a clear path to microservices.

### The "Virtual File System" Strategy
Unlike traditional SQL databases, Covvee uses **MongoDB** to handle the recursive nature of file trees (folders inside folders).
* **Storage:** Files and Folders are stored as distinct documents but linked via `parentId` and `projectId`.
* **Execution:** Code is "materialized" from the database onto a real disk inside ephemeral Docker containers for execution.
* **Security:** File content is Base64 encoded during transport to prevent JSON corruption and injection attacks.

---

## 🛠️ Tech Stack

### Backend
* **Language:** Java 17+
* **Framework:** Spring Boot 3.x
* **Database:** MongoDB (NoSQL)
* **Security:** Spring Security + JWT (Access & Refresh Tokens)
* **Execution:** Docker (via Docker Java API)
* **Validation:** Jakarta Validation (Hibernate Validator)
* **Mapping:** MapStruct

### Frontend (Planned)
* **Framework:** React + TypeScript
* **Editor:** Monaco Editor (VS Code core)
* **State:** Redux / Zustand
* **Styling:** Tailwind CSS

---

## 🧩 Database Schema (Entities)

Covvee uses a 3-collection strategy to bypass MongoDB's 16MB document limit while maintaining recursive folder structures.

| Collection | Role | Key Fields |
| :--- | :--- | :--- |
| **`users`** | Identity | `id`, `username`, `email`, `role` (USER/ADMIN), `projects` |
| **`projects`** | Root Container | `id`, `name`, `language`, `visibility`. (Does *not* store the full tree) |
| **`folders`** | Directory Nodes | `name`, `parentId`, `projectId`. |
| **`files`** | Leaf Nodes | `name`, `content` (Source Code), `parentId`, `projectId`. |

> **Note:** The file tree is reconstructed on the server-side (or lazily loaded) by querying items where `projectId == X`.

---

## 🚀 Getting Started

### Prerequisites
* **Java 17** or higher
* **Docker Desktop** (must be running for code execution)
* **MongoDB** (running locally on port `27017` or via Atlas)

### Installation

1.  **Clone the repository**
    ```bash
    git clone [https://github.com/your-username/covvee.git](https://github.com/your-username/covvee.git)
    cd covvee
    ```

2.  **Configure Environment**
    Create a `src/main/resources/application.properties` file:
    ```properties
    spring.data.mongodb.uri=mongodb://localhost:27017/covvee_db
    spring.data.mongodb.auto-index-creation=true
    
    # JWT Settings
    application.security.jwt.secret-key=YOUR_SUPER_SECRET_KEY_HERE
    application.security.jwt.expiration=86400000
    application.security.jwt.refresh-token.expiration=604800000
    ```

3.  **Run the Application**
    ```bash
    ./mvnw spring-boot:run
    ```

---

## 🔌 API Endpoints (Snapshot)

### 👤 Auth Service
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register new user |
| `POST` | `/api/auth/login` | Login & receive JWT |
| `POST` | `/api/auth/refresh` | Get new Access Token |

### 📁 Project Service
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/projects` | Create a new project |
| `GET` | `/api/projects` | List my projects (Summary) |
| `GET` | `/api/projects/{id}` | Open project (Full Tree) |

### 📄 File Service
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/files` | Create new file |
| `PUT` | `/api/files/{id}/content` | Update code (Save) |
| `PATCH`| `/api/files/{id}/rename` | Rename file |
| `POST` | `/api/folders` | Create new folder |

### ⚙️ Execution Service
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/execute/run` | Materialize project & Run in Sandbox |

---

## 🛡️ Security & Validation Features

* **Input Sanitization:** Regex patterns prevent directory traversal attacks (e.g., filenames cannot contain `../` or `/`).
* **Resource Limits:** File uploads limited to ~100KB (character count) to prevent database bloating.
* **Isolation:** User code runs in ephemeral containers with no network access (planned).

---

## 🤝 Contributing

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.