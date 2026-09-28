# Rentora

**Rentora** is a Spring Boot-based product rental platform that allows users to list products for rent, request rentals, manage rental transactions, review products, and communicate with product owners.

The platform also integrates **Google Gemini AI** to provide intelligent daily rental price suggestions based on the product name and its original purchase price.

---

## 🚀 Features

### 👤 User Management

* Create a new user account.
* Prevent duplicate accounts using email validation.
* Update user information.
* Delete users.
* Retrieve all registered users.
* Send a welcome email after successful registration.
* Notify users when another user requests a specific product.

### 📦 Product Management

* Add products to the platform.
* Update product information.
* Delete products.
* Retrieve all available products.
* Search for products by name.
* Change product availability.
* Change the daily rental price.
* Calculate the estimated rental cost for a specific number of days.

### 🏠 Rental Requests

* Submit rental requests for products.
* Validate renter and product existence.
* Prevent renting unavailable products.
* Prevent users from renting their own products.
* Set rental start and end dates.
* Approve rental requests by product owners.
* Reject rental requests.
* Cancel pending rental requests.
* Retrieve pending requests for a renter.
* Reject all pending requests for a specific product.
* Update rental request dates.
* Automatically reject other pending requests when one request is approved.
* Send email notifications related to rental requests.
* Allow renters to ask product owners questions before renting.

### 📅 Rental Management

* Create active rentals after a request is approved.
* Complete rentals.
* Extend active rentals by requesting additional days.
* Allow product owners to approve rental extensions.
* Calculate the total rental price.
* Calculate owner earnings.
* Report issues with active rentals.
* Automatically make products available again after completing or returning a rental.

### ⭐ Reviews & Ratings

* Add reviews for rented products.
* Ensure that only the renter can review their rental.
* Update reviews.
* Delete reviews.
* Calculate the average rating of a product.

### 📧 Email Notifications

Rentora uses email notifications to keep users informed about important platform activities, including:

* Welcome emails after registration.
* Rental request confirmations.
* Rental date updates.
* Product questions.
* Requested products notifications.
* Rental issue reports.

### 🤖 Gemini AI Price Suggestions

Rentora integrates **Google Gemini AI** to help product owners choose a suitable daily rental price.

The AI receives:

* Product name.
* Original purchase price.

It then provides:

* A recommended daily rental price.
* A simple marketing suggestion for the product owner.

Example:

```text
Product: PlayStation 5
Original Price: 2,000 SAR

Recommended Daily Rental Price: 80 SAR/day
Marketing Tip: Offer a discount for rentals longer than one week.
```

---

## 🛠️ Technologies

* **Java**
* **Spring Boot**
* **Spring Data JPA**
* **Spring Mail**
* **MySQL**
* **Lombok**
* **Maven**
* **Google Gemini API**
* **Git & GitHub**

### External Services

* **Google Gemini API** — AI-powered rental price suggestions.
* **Gmail / SMTP** — Email notifications and communication.

---

## 🏗️ Project Architecture

The project follows a layered Spring Boot architecture:

```text
Rentora
│
├── Controller
│   └── REST API endpoints
│
├── Service
│   ├── UserService
│   ├── ProductService
│   ├── RentalService
│   ├── RentalRequestService
│   ├── RentedProductService
│   ├── ReviewService
│   └── EmailService
│
├── Repository
│   ├── UserRepository
│   ├── ProductRepository
│   ├── RentalRepository
│   ├── RentalRequestRepository
│   ├── RentedProductRepository
│   └── ReviewRepository
│
├── Model
│   ├── User
│   ├── Product
│   ├── Rental
│   ├── RentalRequest
│   ├── RentedProduct
│   └── Review
│
└── Resources
    └── application.properties
```

---

## 🔄 Rental Workflow

The main rental process works as follows:

```text
User
  │
  ▼
Search for Product
  │
  ▼
Submit Rental Request
  │
  ▼
Product Owner Reviews Request
  │
  ├── Reject ───────────────► Request Rejected
  │
  ▼
Approve Request
  │
  ▼
Rental Created
  │
  ▼
Product Becomes Unavailable
  │
  ▼
Rental Active
  │
  ├── Request Extension
  │       │
  │       ▼
  │   Owner Approval
  │
  ▼
Rental Completed
  │
  ▼
Product Becomes Available
  │
  ▼
Renter Can Leave a Review
```

---

## 💰 Rental Price Calculation

The rental price is calculated using the daily rental price, number of rental days, and security deposit.

```text
Total Price = (Number of Days × Daily Rental Price) + Deposit
```

For example:

```text
Daily Price = 50 SAR
Rental Period = 5 days
Deposit = 200 SAR

Total = (5 × 50) + 200
      = 450 SAR
```

---

## 🤖 AI Price Suggestion

The Gemini integration allows product owners to receive an AI-generated rental price recommendation.

The request contains:

```text
Product Name
Original Purchase Price
```

Gemini then returns a recommended rental price and a marketing suggestion.

The API key is configured through the application's properties:

```properties
gemini.api.key=YOUR_GEMINI_API_KEY
```

**Do not commit your actual API key to GitHub.**

---

## 📧 Email Configuration

Rentora uses Spring Mail to send notifications through Gmail/SMTP.

Example configuration:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL@gmail.com
spring.mail.password=YOUR_APP_PASSWORD

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

For Gmail, it is recommended to use a **Google App Password** rather than your regular Gmail password.

---

## ⚙️ Configuration

Create or update:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.application.name=Rentora

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/rentora
spring.datasource.username=YOUR_DATABASE_USERNAME
spring.datasource.password=YOUR_DATABASE_PASSWORD

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Gmail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL@gmail.com
spring.mail.password=YOUR_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Gemini
gemini.api.key=YOUR_GEMINI_API_KEY
```

> Replace all placeholder values with your own configuration.

---

## ▶️ How to Run

### 1. Clone the repository

```bash
git clone YOUR_REPOSITORY_URL
```

### 2. Open the project

Open the project using IntelliJ IDEA or another Java IDE.

### 3. Configure the database

Create a MySQL database:

```sql
CREATE DATABASE rentora;
```

Then update your database credentials in:

```text
application.properties
```

### 4. Configure Gmail

Add your Gmail address and App Password to the mail configuration.

### 5. Configure Gemini

Add your Gemini API key:

```properties
gemini.api.key=YOUR_GEMINI_API_KEY
```

### 6. Run the application

Using Maven:

```bash
./mvnw spring-boot:run
```

Or run the main Spring Boot application class directly from IntelliJ IDEA.

---

## 🔐 Security Notes

Do not commit sensitive information such as:

* Gemini API keys
* Gmail passwords
* Database passwords
* Environment secrets

Use environment variables or a local configuration file for sensitive credentials.

Example:

```properties
gemini.api.key=${GEMINI_API_KEY}
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
```

---

## 📌 Main Services

| Service                | Responsibility                                                |
| ---------------------- | ------------------------------------------------------------- |
| `UserService`          | User management and user notifications                        |
| `ProductService`       | Product management, search, pricing, and AI suggestions       |
| `RentalRequestService` | Rental request lifecycle                                      |
| `RentalService`        | Active rentals, completion, extensions, earnings, and reports |
| `RentedProductService` | Managing rented products and availability                     |
| `ReviewService`        | Reviews and product ratings                                   |
| `EmailService`         | Sending email notifications                                   |

---

## 📊 Core Business Rules

Rentora applies several validation rules to maintain the rental workflow:

* A user must exist before creating a rental request.
* A product must exist before it can be rented.
* An unavailable product cannot be rented.
* A user cannot rent their own product.
* Rental dates must be valid.
* Only the renter can modify their pending rental request.
* Only the product owner can approve a rental request.
* Only the renter can submit a review for their rental.
* Only the product owner can change the product price.
* Only the product owner can change product availability.
* A completed rental makes the product available again.
* Approving one rental request automatically rejects other pending requests for the same product.

---

## 🔮 Future Improvements

Possible future enhancements include:

* User authentication and authorization with Spring Security.
* JWT-based authentication.
* Online payment integration.
* Advanced product filtering.
* Product image upload and storage.
* Real-time messaging between renters and owners.
* Better AI-powered pricing recommendations.
* Rental history and analytics dashboard.
* Automated late-fee calculation.
* Improved exception handling with global exception handlers.
* API documentation using Swagger / OpenAPI.
* Unit and integration testing.

---

## 👨‍💻 Author

**Abdulelah Alwadani**

---

## 📄 License

This project is developed as a software project for educational and development purposes.

