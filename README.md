# Cafe Shop Management System

A full-stack **Cafe Management and Point of Sale (POS) System** built with a **JavaFX desktop client** and a **Spring Boot backend API**.  
The system is designed for cafes and small food businesses to manage daily operations including authentication, users, roles, categories, products, customers, orders, receipts, archive review, image upload, and dashboard reporting.

---


> Add your screenshots here later

## Project Overview


The Cafe Shop Management System is a role-based management platform that combines a desktop POS experience with a RESTful backend.
It supports the core needs of a cafe business, including:

- daily sales operations
- order and receipt handling
- product and category management
- customer management
- user and role administration
- dashboard statistics and reporting
- image upload for products
- role-based access control for administrators and cashiers
  

#### The project is divided into two main parts:

> 1. Backend API

The backend is responsible for:

- business logic
- authentication support
- role protection
- validation
- database operations
- file upload and static file serving
- receipt and archive data
- dashboard statistics and reporting

  
> 2. Desktop Application

The desktop application is responsible for:

- graphical user interface
- user interaction
- product browsing
- cart handling
- customer selection
- order creation
- receipt display
- receipt printing
- admin and cashier tools


#### Main Goals
- Provide a practical desktop-based POS experience for cafes
- Separate backend logic from frontend UI using a client-server architecture
- Support both ADMIN and CASHIER roles
- Protect restricted features through role-based access control
- Manage users, categories, products, customers, and orders in one system
- Support receipt generation, display, printing, and archive review
- Build a strong academic and portfolio-ready full-stack Java project


 ## System Architecture

 This project follows a client-server architecture.

> Backend

The backend is built with Spring Boot and exposes REST endpoints under /api/....
It handles:

- authentication
- persistence
- services
- validation
- file storage
- dashboard statistics
- role-based restrictions


> Desktop Client

The desktop application is built with JavaFX.
It communicates with the backend through HTTP requests, receives JSON responses, parses them, and updates:

- JavaFX tables
- cards
- charts
- forms
- receipt views


## Technologies Used
> Backend 

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Hibernate
- Lombok
- MySQL
- Maven


> Desktop
- Java
- JavaFX
- FXML
- CSS
- Maven


#### Tools
- IntelliJ IDEA
- Postman
- phpMyAdmin or any MySQL client
- Git
- GitHub

## Core Features
 ####  1. Authentication
- User login
- User registration
- Forgot password using security question and answer
- Role-based access control


####  2. Users Management
- Admin can create new users
- Admin can assign ADMIN or CASHIER role
- Admin can update user role
- Admin can enable or disable users


####  3. Inventory Management
- Add product
- Update product
- Deactivate product
- Upload product image
- Search products

#### 4. Category Management
- Add category
- Update category
- Deactivate category
- Search categories


####  5. POS / Menu
- Product cards with image, category, stock, and price
- Quantity selection
- Cart management
- Payment and change calculation
- Receipt viewing
- Receipt printing


####  6. Customers Management
- Add customer
- Update customer
- Delete customer
- Review customer order history
- Open selected receipt from customer history


####  7. Dashboard and Reporting
- Summary cards
- Today's income
- Total income
- Total customers
- Sold products count
- Sales chart
- Orders chart
- Archive and transaction review


##  Roles and Permissions

#### ADMIN

Admin users can access:

- Dashboard
- Inventory management
- Category management
- Users management
- Archive and reports
- Menu
- Customers


#### CASHIER

Cashier users can access:

- Menu
- Customers
- Order creation
- Receipt printing

Cashiers **( cannot )**  access:

- Dashboard
- Inventory management
- Category administration
- Archive administration
- Users management

## Project Structure

#### Backend Structure
```text
com.cafe.backend
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── service
└── CafeBackendApplication
```

#### Desktop Structure

```text
com.cafe.cafedesktop
├── controllers and models
├── service
├── fxml resources
├── css styles
└── launcher/application classes

```

## Backend Details

The backend follows a standard layered Spring architecture.

#### Controller Layer

Responsible for receiving HTTP requests and returning JSON responses.

Main controllers include:

- UserController
- ProductController
- CategoryController
- CustomerController
- OrderController
- DashboardController
- UploadController


#### Service Layer

Contains the core business logic.

Important services include:

- UserService
- ProductService
- CategoryService
- CustomerService
- OrderService
- DashboardService
- FileStorageService

#### Repository Layer

Uses Spring Data JPA for database interaction through repository interfaces such as:

- UserRepository
- ProductRepository
- CategoryRepository
- CustomerRepository
- OrderRepository


#### DTO Layer

Transfers data safely between client and server, including:

- login requests
- registration requests
- product responses
- category responses
- dashboard response models


#### Entity Layer

Represents database tables such as:

- User
- Category
- Product
- Customer
- Order
- OrderItem


## Desktop Application Details

The JavaFX desktop application is the interactive client of the system.

Main screens include:

- Login screen
- Register screen
- Forgot password screen
- Main dashboard screen
- Inventory management screen
- Menu / POS screen
- Customers screen
- Users screen
- Receipt archive section

#### Important desktop classes include:

* MainController
* ApiService
* ProductCardController
* ReceiptController
  
## Authentication and Security

The system includes role-aware authentication and backend protection.

* Security Features
* Passwords are stored encoded using BCryptPasswordEncoder
* Restricted backend endpoints are protected by role
* Desktop stores active session credentials for authorized backend access
* Admin-only operations are restricted
* Login returns role data that is used by the desktop application to shape the visible interface


## User Management Logic

#### Public Registration

Any user who registers through the registration screen is automatically created as a CASHIER.
Public registration never creates an admin account.

#### Admin-Created Users

An admin can create a new user from the desktop application and choose:

* username
* password
* role
* security question
* security answer

#### Role and Status Updates

An admin can:

* update the user's role
* enable a user
* disable a user

#### Self-Protection Rules

To avoid locking out administrators accidentally, the system includes these protections:

* an admin cannot disable their own active account
* an admin cannot downgrade their own active role from *( ADMIN )* to *( CASHIER )* during the current session



## Category Management Logic

Categories organize products into menu groups such as:

- Hot Drinks
- Cold Drinks
- Desserts
- Other menu categories

Supported operations:

- create category
- update category
- search category
- deactivate category instead of hard delete

This soft-delete approach helps preserve historical data and prevents breaking relationships with old products or reports.



## Product Management Logic

Each product can store:

- name
- description
- price
- quantity
- image URL
- category
- active state
- created date
  
#### Product Rules

* If quantity is greater than zero, the product can be available
* If quantity becomes zero, the product can be marked inactive
* Only active products with stock greater than zero are shown in the menu cards
* Admin can add, update, deactivate, search, and upload image for products


## Customer Management Logic

Customers are optional in the sales process.
An order can be linked to either:

- a saved customer
- a walk-in customer

Supported customer operations:

- add customer
- update customer
- delete customer
- load customer order history
- open selected receipt from customer history



## Order and Receipt Logic

#### Order Flow
* Available products are loaded from the backend
* Products are displayed as cards in the menu screen
* The user selects quantities and adds items to the cart
* A customer can be selected or the order can remain walk-in
* The payment amount is entered and change is calculated
* The order is submitted to the backend
* A receipt becomes available after successful order creation


#### Receipt Data

The generated receipt includes:

- order ID
- customer
- cashier
- date
- time
- line items
- total amount
- paid amount
- change

#### Printing

The receipt can be shown in a separate window and printed using JavaFX printing support.
The print button is hidden while the printable node is sent to the printer so that the printed receipt remains clean.



## Dashboard and Statistics

The dashboard is intended for ADMIN users and provides an overview of business performance.

It includes:

- total customers
- today's income
- total income
- number of sold products
- sales chart
- orders chart


## Image Upload and Static File Serving

#### Upload Flow

- The desktop application selects an image through a file chooser
- The image is uploaded to the backend using multipart/form-data
- The backend stores the file inside the uploads directory
- The backend returns a public image URL
- The desktop saves the returned image URL with the product
- Menu cards later load the image from the backend using that URL

#### Static File Mapping

Uploaded files are exposed under:
```text
/uploads/**
```
Example image URL:
```text
[/uploads/**](http://localhost:8080/uploads/filename.png)
```

## Password Reset Flow

The application supports password reset using security question verification.

#### Reset Steps

* User enters username
* User provides security question and answer
* Backend verifies the values
* User sets a new password

#### Notes
- Answers are normalized before validation
- Password rules are still enforced during reset


## Important Business Rules

- Public registration always creates a CASHIER
- Only an admin can create admin accounts
- Only an admin can change roles
- Only an admin can enable or disable users
- The current active admin cannot disable their own account
- The current active admin cannot downgrade their own role during the active session
- Only active products with available stock appear in the menu
- Category and product delete operations are handled as deactivation rather than hard deletion in the intended business flow
- Payment cannot complete if the paid amount is less than the total
- Cart checkout must pass the latest stock validation before final payment



## API Overview

#### Users
* POST /api/users/register
* POST /api/users/login
* GET /api/users/by-username
* POST /api/users/forgot-password
* POST /api/users/verify-reset-data
* GET /api/users
* POST /api/users/admin-create
* PUT /api/users/{userId}/role
* PUT /api/users/{userId}/enabled

#### Categories
* POST /api/categories
* GET /api/categories
* PUT /api/categories/{id}
* DELETE /api/categories/{id}

#### Products
* POST /api/products
* GET /api/products
* GET /api/products/{id}
* PUT /api/products/{id}
* DELETE /api/products/{id}

#### Customers
* POST /api/customers
* GET /api/customers
* PUT /api/customers/{id}
* DELETE /api/customers/{id}
* GET /api/customers/{id}/orders

#### Orders
* POST /api/orders
* GET /api/orders
* GET /api/orders/{id}/receipt
* GET /api/orders/archive/all

#### Dashboard
* GET /api/dashboard
* GET /api/dashboard/sales-chart
* GET /api/dashboard/orders-chart

#### Uploads
* POST /api/uploads




## Database Design Overview

#### users

Stores:

* id
* username
* password
* role
* security question
* security answer
* enabled state
* created timestamp

#### categories

Stores:

* id
* name
* description
* active state
* created timestamp

#### products

Stores:

* id
* name
* description
* price
* quantity
* image URL
* active state
* category ID
* created timestamp

#### customers

Stores:

* id
* name
* phone
* notes
* created timestamp

#### orders

Stores:

* id
* user ID
* customer ID
* total amount
* created timestamp

#### order_items

Stores:

* id
* order ID
* product ID
* quantity
* unit price
* line total


## How the Application Works

- Desktop starts with the login screen
- After successful registration, the interface returns to login
- After successful login, the system loads the user role
- Admin users can see dashboard, inventory, users, menu, and customers
- Cashier users can see menu and customers only
- The desktop hides or blocks forms that are not allowed for the current role


## How to Run the Backend

#### Requirements
- Java installed
- Maven installed
- MySQL running
- Database created

#### Steps

- Configure the database values in application.properties
- Make sure MySQL is running and the target database exists
- Run the Spring Boot main class:  CafeBackendApplication
- Confirm that the backend runs on: (http://localhost:8080)



## How to Run the Desktop App

#### Requirements
- Java installed
- JavaFX configured
- Backend already running

#### Steps
- Open the JavaFX desktop project
- Confirm that ApiService points to:
http://localhost:8080/api
- Run the JavaFX launcher or main application
- Login using valid credentials




## Configuration Notes

#### Backend Base URL

```text

http://localhost:8080/api

```


#### Uploads Base URL

```text
http://localhost:8080/uploads/...

```

#### Database

Database credentials must match the configuration in the backend.




## User Interface Notes

The desktop application includes:

* side navigation
* role-based button visibility
* searchable tables
* menu product cards
* receipt pop-up window
* logout confirmation
* return to normal login size after logout
* register slider returning to login after successful registration
* hidden print button during receipt printing
* image preview in inventory


## Error Handling

The project includes backend validation and desktop-side user feedback.

Handled cases include:

* invalid username or password
* duplicate username
* duplicate category name
* password rule validation
* insufficient payment
* exceeded stock quantity
* missing required fields
* no selected item for update or delete
* protection against disabling the currently active admin account


## Known Limitations

* Desktop JSON parsing is still manual and can be improved using a JSON library
* Session handling is basic
* Monthly closing logic may still be placeholder if not fully implemented
* Export features such as PDF or Excel are not yet part of the system
* Reporting can still be expanded further


## Possible Future Improvements

#### Business Improvements
- expenses tracking
- supplier management
- purchase invoices
- low stock alerts
- discount system
- tax support
- multi-branch support

#### Technical Improvements
- use a JSON library such as Jackson or Gson in the desktop app
- introduce stronger session handling or JWT
- improve exception response formatting
- add database migrations with Flyway or Liquibase


#### UI Improvements
- dark mode
- keyboard shortcuts for cashier workflow
- more polished reports
- additional analytics screens
