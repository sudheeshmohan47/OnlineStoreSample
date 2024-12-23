# OnlineStoreSample

**Table of Contents**

- [Technology stack](#technology-stack)
- [Architecture](#architecture)
- [Design patterns](#design-patterns)
- [Installation](#installation)
- [Testing](#testing)

## Technology Stack
The following are the tech stack used for this project:
- **Jetpack Compose**: Ui
- **Kotlin**  
- **Room**: Database
- **Retrofit & OkHttp**: For Networking  
- **JUnit & MockK**: Unit testing 
- **Paparazzi**: Screenshot testing library.  
- **Hilt**: Dependency injection   
- **Ktlint**: Linting tool for Kotlin code.  
- **Detekt**: Static code analysis tool with Compose-specific rules.
- **Jacoco**: Used for measuring code coverage, providing insights into the extent of testing with both unit tests and UI tests.
- **Github Workflow**:  Automated workflows that perform specified checks(eg: ktlint, detekt...) during pull requests, ensuring code quality and consistency by raising errors if any checks fail.

## Architecture
OnlineStoreSample app is structured in a mix of clean architecture, MVVM, and MVI pattern.
The whole project is clearly separated in layers - Presentation, Business logic and Data layer.

Project is a combination of multiple MFE's (Micro Front Ends)
- **designsystem**: Defines the app's theme, spacing, sizing, and reusable UI components.  
- **commonmodule**: Serves as a shared library across modules, encapsulating utilities, networking functionality, and other common components to ensure reusability and maintainability.
- **datastoragemodule**: DataStorageModule: Manages database operations and preference handling, providing a centralized solution for data storage and retrieval.
- **authenticationmodule**: Manages user authentication, including login and registration screens.  
- **categoriesmodule**: For managing product categories  
- **cartmodule**: Manages cart functionality and screens.  
- **wishlistmodule**: Handles the display and management of items added to the wishlist, providing functionality for users to view or remove their favorite items.

![OnlineStoreApp Architecture](https://github.com/user-attachments/assets/83f01d94-7d29-477d-9731-6559b8010313)

## Design Patterns

This app make use of the following patterns:
 - Dependency Inversion pattern which helps to avoid coupling
 - MVI is used in presentation layer for handling user interactions in the form of State changes with the help of Actions and Events

## Installation
#### Add these in gradle.properties file
- BASE_URL="https://fakestoreapi.com"

## Dependencies
All the dependencies are managed using .toml file inside the root -> gradle folder.
libs.versions.toml contains all the dependencies required for this project

## Testing
Login using the following credentials:
 - username: mor_2314
 - password: 83r5^_

Products listing screen will be loaded in the home page. 
Category specific products can be showing by going to the categories tab and Select categories. If no categories are selected, full product list will be loaded.
Wishlisted and Cart items are displayed in the Wishlist screen and Cart screen respectively.

Unit test and Ui test are added for app module and products module. 
