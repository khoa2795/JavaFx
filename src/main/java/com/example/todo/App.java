package com.example.todo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login-view"), 700, 450);
        scene.getStylesheets().add(App.class.getResource("/com/example/todo/todo.css").toExternalForm());
        stage.setTitle("JavaFX Demo");
        stage.setScene(scene);
        stage.show();
    }

    public static void showTodoView(String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/example/todo/todo-view.fxml"));
        Parent root = loader.load();
        TodoController controller = loader.getController();
        controller.setLoggedInUser(username);
        scene.setRoot(root);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/example/todo/" + fxml + ".fxml"));
        return loader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
