module com.example.todo {
    requires transitive javafx.controls;
    requires javafx.fxml;

    exports com.example.todo to javafx.graphics;
    opens com.example.todo to javafx.fxml;
}
