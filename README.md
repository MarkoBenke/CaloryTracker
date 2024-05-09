<h1 align="center">Calory Tracker App</h1>

<p align="center">  
Welcome to Calory Tracker, a modern Android application showcasing multi-module clean architecture for efficient and scalable app development. 
  <br> Calory Tracker helps you keep track of your daily caloric intake and expenditure, empowering you to maintain a healthy lifestyle.
</p>
</br>

<p align="center">
  <img src="https://i.imgur.com/Ek47LGxh.jpg" />
</p>

## Features
**Modularization by Feature:** 
</br>
Calory Tracker is structured around distinct features, primarily focusing on onboarding and tracker functionalities. Each feature is further modularized by layers to ensure separation of concerns and maintainability.
</br>
**Clean Architecture:**
</br>
The project employs clean architecture principles to achieve a robust and maintainable codebase. The architecture is organized into layers including data, domain, and presentation, facilitating easier testing, debugging, and modification.

## Modules
The project is divided into the following modules:

**Core Module:** Contains common functionalities and utilities shared across all modules.</br>
**Onboarding Module:** Handles the onboarding flow.</br>
**Tracker Module:** Manages the calory tracking functionality, allowing users to log their food intake.</br>

## Tech Stack:
**UI with Compose:** The user interface is built using Jetpack Compose, offering a declarative and modern approach to UI development.</br>
**Canvas:** Canvas is used for custom widgets.</br>
**Dependency Injection with Hilt:** Hilt is utilized for dependency injection, enabling easier management and instantiation of objects throughout the application.</br>
**Kotlin Coroutines:** Asynchronous programming is handled efficiently using Kotlin Coroutines, ensuring smooth execution of tasks without blocking the main thread.</br>
**Kotlin Flows:** Kotlin Flows are leveraged for reactive programming, facilitating seamless data streams and updates within the app.</br>
**Room Database:** Calory Tracker utilizes Room Database for local data storage, providing a robust and efficient solution for managing app data.</br>
**Retrofit:** Networking operations are performed using Retrofit, enabling seamless communication with remote servers and APIs.</br>
**Testing:** The project includes comprehensive unit tests and end-to-end tests to ensure the reliability and stability of the application.</br>

