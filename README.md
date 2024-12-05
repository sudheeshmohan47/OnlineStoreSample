# OnlineStoreSample

**Table of Contents**

- [Technology stack](#technology-stack)
- [Architecture](#architecture)
- [Design patterns](#design-patterns)
- [Installation](#installation)
- [Testing](#testing)
- [Note](#note)

## Technology Stack
The following are the tech stack used for this project:
- Jetpack Compose ( Showing UI)
- Kotlin
- Retrofit & Okhttp (Networking)
- Junit & Mockk( Unit testing), Papparazzi for screenshot testing
- Hilt ( Dependency Injection)
- Ktlint for detecting lint errors and Detekt with Compose rules for improving code quality

## Architecture
OnlineStoreSample app is structured in a mix of clean architecture and MVI pattern.
The whole project is clearly separated in layers - Presentation, Business logic and Data layer.

Project is a combination of multiple MFE's (Micro Front Ends)
- **designsystem**: This module is used for defining the common app theme for app which includes Spacing details, Sizing details, custom UI components etc
- **commonmodule**: This module contains all the common items shared across different modules
- **datastoragemodule**: This module used for handling Database and Preference which is used in different modules
- **authenticationmodule**: This MFE is used for authenticating the user. Contains authentication related screens
- **productsmodule**: This MFE is used for showing Products and Details in the app. 
- **categoriesmodule**: This MFE is used for showing Categories in the app
- **cartmodule**: This MFE is used for showing Cart in the app.
- **wishlistmodule**: This MFE is used for showing wishlisted items in the app
- **shared**: This module is used for handling database operations as well as app related shared info's

  ![arch](https://github.com/user-attachments/assets/8af276ac-f0c5-4a10-a1a3-b65ce1238e0e)


## Design Patterns

This app make use of the following patterns:
 - Dependency Inversion pattern which helps to avoid coupling
 - MVI is used in presentation layer for handling user interactions in the form of State changes with the help of Actions and Events

## Installation
#### Add these in gradle.properties file
- BASE_URL="https://fakestoreapi.com"

All the dependencies are managed using .toml file inside the root -> gradle folder.
libs.versions.toml contains all the dependencies required for this project

## Testing
Login using the following credentials:
username: mor_2314
password: 83r5^_

Products listing screen will be loaded in the home page. 
Category specific products can be showing by going to the categories tab and Select categories. If no categories are selected, full product list will be loaded.
Wishlisted and Cart items are displayed in the Wishlist screen and Cart screen respectively.

Unit test and Ui test are added for app module and products module. 
