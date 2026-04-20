# 💰 Expense Tracker (Clean Architecture + Compose)

A modern Android application for tracking daily expenses, refactored to follow **Clean Architecture** principles and built with **Jetpack Compose**.

---

## ✨ Features

- ➕ Add expenses with title, amount, category, date, and note.
- 📋 View expenses in a list with category filtering.
- 💵 Live total balance calculation.
- 🎨 Modern UI with Material Design 3 and Jetpack Compose.
- 🏗️ Robust Clean Architecture with Domain, Data, and Presentation layers.
- 💾 Persistent storage using Room Database.

---

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| Kotlin | Primary programming language |
| Jetpack Compose | Declarative UI |
| Clean Architecture | separation of concerns (Domain, Data, Presentation) |
| Room Database | Local data storage |
| Use Cases | Encapsulated business logic |
| Coroutines/Flow | Asynchronous data streams |
| Material 3 | Modern UI components |

---

## 🏗️ Architecture

The app is built following **Clean Architecture** principles:

### 1. Domain Layer (Pure Kotlin)
- **Entities**: Business models (`Expense`).
- **Repository Interfaces**: Defined the contract for data operations.
- **Use Cases**: Individual business logic operations (`GetExpensesUseCase`, `AddExpenseUseCase`, etc.).

### 2. Data Layer
- **Room Entities & DAO**: Local database implementation.
- **Repository Implementation**: Connects the domain repository interface with Room.
- **Mappers**: Converts between Data Entities and Domain Models.

### 3. Presentation Layer (Jetpack Compose)
- **ViewModels**: Manages UI state and interacts with Use Cases.
- **Compose UI**: Declarative screens and components.
- **Theme**: Material 3 based styling.

---

## 📂 Project Structure

```
com.example.expensetracker/
├── domain/
│   ├── model/         # Business Entities
│   ├── repository/    # Repository Interfaces
│   └── usecase/       # Business Logic (Use Cases)
├── data/
│   ├── mapper/        # Domain-Data Mappers
│   ├── repository/    # Repository Implementations
│   └── [Room classes] # Dao, Entity, Database
└── presentation/
    ├── viewmodel/     # Screen ViewModels
    └── ui/            # Compose Screens & Theme
```

---

## 🚀 Getting Started

1. Open the project in **Android Studio**.
2. Run the app on an emulator or physical device (Min SDK: 24).
