# smart-contact-manager-scm
Smart Contact Manager (SCM) is a full-stack web application built with Spring Boot, Thymeleaf, and Tailwind CSS. It features secure authentication, comprehensive contact management, profile handling, and image uploading.
# Smart Contact Manager (SCM)

A full-stack, secure, and fully responsive **Smart Contact Manager** web application built with modern Java and frontend technologies. This platform enables users to securely manage personal contacts, organize information, and authenticate using custom credentials or social providers.

---

## 🚀 Key Features

* **Secure Authentication & Authorization**: Powered by Spring Security, supporting form-based login and OAuth2 social logins (Google & GitHub).
* **Comprehensive Contact Management**: Add, update, view, search, and delete contacts efficiently.
* **Responsive UI Design**: Styled using **Tailwind CSS** and **Flowbite** for a seamless user experience across mobile, tablet, and desktop viewports.
* **Cloud Profile Media**: Integrated with Cloudinary for handling profile and contact pictures.
* **Robust Form Validation**: Implements Jakarta Validation and custom file validators.

---

## 🛠️ Tech Stack

* **Backend**: Java, Spring Boot, Spring Security, Spring Data JPA, Hibernate, MySQL
* **Frontend**: Thymeleaf, Tailwind CSS, JavaScript, HTML5
* **Tools & Utilities**: Maven, Cloudinary, Lombok, Spring Boot DevTools

---

## ⚙️ Getting Started

Follow these steps to set up and run the project locally on your machine.

### Prerequisites
* Java JDK (version 17 or higher)
* Apache Maven
* MySQL Server

### 1. Clone the Repository
```bash
git clone [https://github.com/akashpathak22/smart-contact-manager-scm.git](https://github.com/akashpathak22/smart-contact-manager-scm.git)
cd smart-contact-manager-scm



2. Configure Environment Properties
Create an application.properties file inside src/main/resources/ (referencing your local environment details). You can use the following template structure:
spring.application.name=scm
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/scm
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

# Cloudinary Configuration
cloudinary.cloud.name=YOUR_CLOUD_NAME
cloudinary.api.key=YOUR_API_KEY
cloudinary.api.secret=YOUR_API_SECRET


3. Build and Run the Application
Run the application using the Maven wrapper:

On Windows (CMD / PowerShell):
mvnw.cmd spring-boot:run


📂 Project Structure

scm/
├── src/
│   ├── main/
│   │   ├── java/com/scm/
│   │   │   ├── config/        # Security and application configurations
│   │   │   ├── controller/    # Web controllers (Dashboard, Profile, Contacts)
│   │   │   ├── entities/      # JPA database entities (User, Contacts, etc.)
│   │   │   ├── enums/         # Enumeration types (Providers, MessageTypes)
│   │   │   ├── forms/         # DTOs and form handling objects (UserDto, etc.)
│   │   │   ├── helpers/       # Utility classes, helpers, and exceptions
│   │   │   ├── repositories/  # Spring Data JPA data access interfaces
│   │   │   ├── service/       # Business logic layer and implementations
│   │   │   ├── validators/    # Custom validators (e.g., file validation)
│   │   │   ├── Db.java        # Database utility/runner component
│   │   │   └── ScmApplication.java # Main Spring Boot entry point
│   │   └── resources/
│   │       ├── static/        # CSS, JavaScript, and asset files
│   │       ├── templates/     # Thymeleaf HTML views and layout fragments
│   │       └── application.properties # Local environment configurations (ignored by git)