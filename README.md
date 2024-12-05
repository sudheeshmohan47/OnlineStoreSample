# OnlineStoreSample

**Table of Contents**

- [Technology stack](#technology-stack)
- [Architecture](#architecture)
- [Design patterns](#design-patterns)
- [Installation](#installation)
- [Testing](#testing)

## Technology Stack
The following are the tech stack used for this project:
- Jetpack Compose
- Kotlin
- Room (Database)
- Retrofit & Okhttp (Networking)
- Junit & Mockk( Unit testing), Papparazzi for screenshot testing
- Hilt ( Dependency Injection)
- Ktlint for detecting lint errors and Detekt with Compose rules for improving code quality

## Architecture
OnlineStoreSample app is structured in a mix of clean architecture, MVVM, and MVI pattern.
The whole project is clearly separated in layers - Presentation, Business logic and Data layer.

Project is a combination of multiple MFE's (Micro Front Ends)
- **designsystem**: This module is used for defining the common app theme for app which includes  Spacing details, Sizing details, custom UI components etc
- **commonmodule**: This module contains all the common items shared across different modules including Networking
- **datastoragemodule**: This module used for handling Database and Preference which is used in different modules
- **authenticationmodule**: This MFE is used for authenticating the user. Contains authentication related screens. 
- **categoriesmodule**: This MFE is used for showing Categories in the app. 
- **cartmodule**: This MFE is used for showing Cart in the app. 
- **wishlistmodule**: This MFE is used for showing wishlisted items in the app 
- **productsmodule**: This MFE is used for showing Products and Details in the app. From here you can wishlist or add a product to cart. 

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
