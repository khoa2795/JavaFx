# Project Structure And File Responsibilities

This document explains the layout of the JavaFX Todo Demo and the role of each file.

## Folder Structure

```text
JavaFx/
├── pom.xml
├── README.md
├── PROJECT_STRUCTURE.md
└── src/
    └── main/
        ├── java/
        │   ├── module-info.java
        │   └── com/example/todo/
        │       ├── App.java
        │       ├── TaskItem.java
        │       └── TodoController.java
        └── resources/
            └── com/example/todo/
                ├── todo-view.fxml
                └── todo.css
```

## File-By-File Explanation

### `pom.xml`
- Maven project configuration.
- Declares JavaFX dependencies (`javafx-controls`, `javafx-fxml`).
- Configures compiler target (`release 17`).
- Configures `javafx-maven-plugin` so `mvn javafx:run` launches the app.

### `README.md`
- Quick start instructions.
- Shows how to run the app and open the FXML in Scene Builder.
- Lists demo features and prerequisites.

### `PROJECT_STRUCTURE.md`
- This file.
- Serves as a technical map of project layout and file responsibilities.

### `src/main/java/module-info.java`
- Java module declaration for the app.
- Requires JavaFX modules (`javafx.controls`, `javafx.fxml`).
- Exports package to `javafx.graphics` so JavaFX launcher can instantiate `App`.
- Opens package to `javafx.fxml` so `FXMLLoader` can access controller members.

### `src/main/java/com/example/todo/App.java`
- JavaFX application entry point.
- Loads `todo-view.fxml`.
- Creates and shows the main window (`Stage`).
- Applies `todo.css` stylesheet.

### `src/main/java/com/example/todo/TaskItem.java`
- Data model for one todo item.
- Holds:
  - `title` (`StringProperty`)
  - `done` (`BooleanProperty`)
- Uses JavaFX properties so UI can bind/react to data changes.

### `src/main/java/com/example/todo/TodoController.java`
- Controller for `todo-view.fxml`.
- Handles UI actions:
  - Add task
  - Remove selected task
  - Toggle done
  - Clear completed tasks
- Manages `ObservableList<TaskItem>` used by the `ListView`.
- Configures custom list cell rendering for status badges and done styling.
- Updates task counter (`Active | Done`).

### `src/main/resources/com/example/todo/todo-view.fxml`
- UI layout file for Scene Builder and JavaFX runtime.
- Defines controls and layout containers (`BorderPane`, `VBox`, `HBox`, `ListView`, buttons, labels).
- Wires button actions to controller methods (`onAddTask`, `onRemoveTask`, etc.).
- Declares `fx:controller` as `com.example.todo.TodoController`.

### `src/main/resources/com/example/todo/todo.css`
- Visual style definitions for the UI.
- Provides styles for:
  - App background
  - Title typography
  - Task list appearance
  - Status chips (`TODO`, `DONE`)
  - Completed-task text effect

## Runtime Flow (High Level)

1. `App.java` starts JavaFX and loads `todo-view.fxml`.
2. `FXMLLoader` creates `TodoController`.
3. `TodoController.initialize()` binds controls and fills initial tasks.
4. User actions trigger controller methods to modify `ObservableList<TaskItem>`.
5. `ListView` re-renders based on model state and CSS styles.
