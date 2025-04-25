# Quiz Application

A Java-based quiz application designed for educational purposes, allowing teachers to create and manage quizzes while
students can take them and receive immediate feedback. Built as a final project for the 2024-2025 school year of Honors
Computer Science at RL.

## Authors

- Marco Suri (RL '26) => Project Manager; Core Feature Developer; UI Developer
- Avish Kumar (RL '26) => Head of Feature Design; UI Developer
- Michael DiLallo (RL '26) => Core Feature Developer

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Git

### Installation

1. Clone the repository:

    ```bash
    git clone [repository-url]
    cd quiz-app
    ```

2. Install Maven dependencies:

    ```bash
    mvn clean install
    ```

### Further Setup

Teachers/testers must have the following directory structure on their home directory for proper functionality:

```
~/quiz-app/
├── chapter-questions/
│   ├── chapter_1_questions.csv
│   ├── chapter_12_questions.csv
│   └── ...
├── student-results/
└── ...
```

1. Create the main directory:

    ```bash
    mkdir -p ~/quiz-app
    ```

2. Create the required subdirectories:

    ```bash
    mkdir -p ~/quiz-app/chapter-questions
    mkdir -p ~/quiz-app/student-results
    ```

3. Generate question CSV files:
    - Navigate to the `/python` directory
    - Use the provided Python script to generate question CSV files from your source material
    - Place the generated CSV files in the `~/quiz-app/chapter-questions/` directory
    - Note: Currently, the student UI supports chapters 1 and 12, so ensure you have `chapter_1_questions.csv` and
      `chapter_12_questions.csv` in place

#### Important Notes on Question Generation

- The Python script in the `/python` directory can be used to generate question CSV files from source material
- Due to potential input inconsistencies, the generated CSV files may require manual review
- Pay special attention to the difficulty column in the generated files:
    - 1 = Easy
    - 2 = Medium
    - 3 = Hard
- Some difficulty values may not be properly converted to numbers and may need manual correction

### Bundling the App

Due to time constraints, we were unable to bundle the application as a native .app or .exe file. However, you can create
a runnable binary using `jlink`. Full native bundling remains as a potential improvement (see "Possible Improvements"
section below).

#### Creating a Runnable Binary (Platform Specific)

1. Build the project using Maven and jlink (note this will build the app using the main class specified in the pom.xml,
   change the main class if you wish to bundle the student vs. teacher app):

    ```bash
    mvn clean javafx:jlink
    ```

2. The application binary will be available in the `target/app/bin` directory

3. Run the binary

## Project Structure

The application follows a modular architecture with the following main packages:

```
src/
├── application/
│   ├── student/     # Student-facing UI and functionality
│   └── teacher/     # Teacher-facing UI and functionality
├── backend/         # Data handling and persistence
└── core/           # Core business logic and shared utilities
```

Each package serves a specific purpose:

- `application/student`: Contains the student interface and quiz-taking functionality
- `application/teacher`: Houses the teacher interface for question management
- `backend`: Handles data storage, retrieval, and file operations
- `core`: Contains shared utilities, models, and core business logic

## Technologies

- Java 17+
- JavaFX
- Maven
- Jackson Data Formats (CSV)
- JSON
- Java Sockets

## Current Features

- Core classes
    - Adaptive question loading for better student assessment => use strategy pattern for future ease of use
    - Store original XHTML to replicate the original styling of the question bank
- Student UI
    - 50/50 lifeline
    - Ask teacher lifeline
    - FXML and CSS files for UI layout/styling
- Teacher UI
    - Views to manage students while taking their quizzes
    - Integration with server to manage student connections and save results
    - FXML and CSS files for UI layout/styling
- Backend
    - Modular server design => requests are handled on the Teacher side
    - TCP wrapper around Java Sockets with Client/Server

## Possible Improvements

- Allow teachers to accept/deny students from taking certain quizzes (half-implemented already, requires making Server
  multithreaded)
- Bundle app as .app or .exe
- Create new lifeline: Another one => allows students to get another question of similar difficulty without losing
  points
