package com.example.todo;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Reflection;
import javafx.scene.paint.Color;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TodoController {

    @FXML
    private TextField taskInput;

    @FXML
    private Label titleLabel;

    @FXML
    private Label greetingLabel;

    @FXML
    private Label fontSizeValueLabel;

    @FXML
    private ComboBox<String> listSelector;

    @FXML
    private Slider fontSizeSlider;

    @FXML
    private ListView<TaskItem> taskListView;

    @FXML
    private BorderPane mainContent;

    @FXML
    private VBox todoCard;

    @FXML
    private StackPane popupOverlay;

    @FXML
    private TextField newListNameInput;

    @FXML
    private Button addButton;

    @FXML
    private Button newListButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button markDoneButton;

    @FXML
    private Button clearDoneButton;

    @FXML
    private Label counterLabel;

    private final Map<String, ObservableList<TaskItem>> todoLists = new LinkedHashMap<>();
    private ObservableList<TaskItem> currentTasks = FXCollections.observableArrayList();
    private final DoubleProperty listFontSize = new SimpleDoubleProperty(22);

    @FXML
    public void initialize() {
        setupVisualEffects();
        taskListView.setItems(currentTasks);
        taskListView.setCellFactory(listView -> new TodoTaskCell(listFontSize));
        taskInput.setOnAction(event -> onAddTask());
        newListNameInput.setOnAction(event -> onConfirmCreateNewList());

        addButton.disableProperty().bind(taskInput.textProperty().isEmpty());
        removeButton.disableProperty().bind(taskListView.getSelectionModel().selectedItemProperty().isNull());
        markDoneButton.disableProperty().bind(taskListView.getSelectionModel().selectedItemProperty().isNull());
        bindTodoListFontSize();
        installHoverScale(addButton);
        installHoverScale(newListButton);
        installHoverScale(markDoneButton);
        installHoverScale(removeButton);
        installHoverScale(clearDoneButton);

        listSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldName, newName) -> {
            if (newName != null) {
                switchToList(newName);
            }
        });

        ObservableList<TaskItem> defaultList = FXCollections.observableArrayList();
        defaultList.addAll(
                new TaskItem("Buy groceries"),
                new TaskItem("Write JavaFX demo"),
                new TaskItem("Review pull request")
        );

        todoLists.put("My Tasks", defaultList);
        listSelector.setItems(FXCollections.observableArrayList(todoLists.keySet()));
        listSelector.getSelectionModel().select("My Tasks");
    }

    @FXML
    private void onAddTask() {
        String title = taskInput.getText().trim();
        if (title.isEmpty()) {
            return;
        }

        TaskItem item = new TaskItem(title);
        currentTasks.add(item);
        attachDoneListener(item);
        taskInput.clear();
        taskListView.getSelectionModel().select(item);
        updateCounter();
    }

    @FXML
    private void onRemoveTask() {
        TaskItem selected = taskListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            currentTasks.remove(selected);
            updateCounter();
        }
    }

    @FXML
    private void onToggleDone() {
        TaskItem selected = taskListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setDone(!selected.isDone());
            taskListView.refresh();
            updateCounter();
        }
    }

    @FXML
    private void onClearDone() {
        currentTasks.removeIf(TaskItem::isDone);
        updateCounter();
    }

    @FXML
    private void onCreateNewList() {
        popupOverlay.setManaged(true);
        popupOverlay.setVisible(true);
        mainContent.setEffect(new GaussianBlur(8));
        newListNameInput.clear();
        newListNameInput.requestFocus();

        FadeTransition fadeIn = new FadeTransition(Duration.millis(45), popupOverlay);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    @FXML
    private void onConfirmCreateNewList() {
        String listName = newListNameInput.getText() == null ? "" : newListNameInput.getText().trim();
        if (listName.isEmpty()) {
            return;
        }

        if (todoLists.containsKey(listName)) {
            showWarning("List already exists");
            listSelector.getSelectionModel().select(listName);
            onCancelCreateNewList();
            return;
        }

        ObservableList<TaskItem> newList = FXCollections.observableArrayList();
        todoLists.put(listName, newList);
        listSelector.getItems().add(listName);
        listSelector.getSelectionModel().select(listName);
        onCancelCreateNewList();
    }

    @FXML
    private void onCancelCreateNewList() {
        mainContent.setEffect(null);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(40), popupOverlay);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(event -> {
            popupOverlay.setVisible(false);
            popupOverlay.setManaged(false);
        });
        fadeOut.play();
    }

    private void switchToList(String listName) {
        ObservableList<TaskItem> selectedList = todoLists.get(listName);
        if (selectedList == null) {
            return;
        }

        currentTasks = selectedList;
        taskListView.setItems(currentTasks);
        currentTasks.forEach(this::attachDoneListener);
        updateCounter();

        FadeTransition fade = new FadeTransition(Duration.millis(220), taskListView);
        fade.setFromValue(0.65);
        fade.setToValue(1);
        fade.play();
    }

    private void attachDoneListener(TaskItem item) {
        // Update aggregate counters whenever done state changes.
        item.doneProperty().addListener((obs, oldValue, newValue) -> updateCounter());
    }

    private void updateCounter() {
        long doneCount = currentTasks.stream().filter(TaskItem::isDone).count();
        long activeCount = currentTasks.size() - doneCount;
        counterLabel.setText(String.format(Locale.US, "Active: %d | Done: %d", activeCount, doneCount));
    }

    public void setLoggedInUser(String username) {
        String safeName = (username == null || username.isBlank()) ? "Guest" : username.trim();
        greetingLabel.setText("Welcome, " + safeName + "!");
    }

    private void setupVisualEffects() {
        DropShadow shadow = new DropShadow();
        shadow.setRadius(22);
        shadow.setSpread(0.12);
        shadow.setOffsetY(6);
        shadow.setColor(Color.rgb(15, 23, 42, 0.25));
        todoCard.setEffect(shadow);

        Reflection reflection = new Reflection();
        reflection.setFraction(0.25);
        reflection.setTopOffset(1.5);
        titleLabel.setEffect(reflection);
    }

    private void bindTodoListFontSize() {
        listFontSize.bind(fontSizeSlider.valueProperty());
        fontSizeValueLabel.textProperty().bind(Bindings.format("Todo text: %.0f px", fontSizeSlider.valueProperty()));
    }

    private void installHoverScale(Node node) {
        ScaleTransition zoomIn = new ScaleTransition(Duration.millis(300), node);
        zoomIn.setToX(1.05);
        zoomIn.setToY(1.05);

        ScaleTransition zoomOut = new ScaleTransition(Duration.millis(220), node);
        zoomOut.setToX(1.0);
        zoomOut.setToY(1.0);

        node.setOnMouseEntered(event -> zoomIn.playFromStart());
        node.setOnMouseExited(event -> zoomOut.playFromStart());
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static class TodoTaskCell extends ListCell<TaskItem> {
        private final CheckBox doneCheckBox = new CheckBox();
        private final Label title = new Label();
        private final Label status = new Label();
        private final Region spacer = new Region();
        private final HBox row = new HBox(10, doneCheckBox, title, spacer, status);

        private TaskItem boundItem;
        private final ChangeListener<Boolean> doneListener = (obs, oldValue, newValue) -> applyDoneState(newValue);

        private TodoTaskCell(ReadOnlyDoubleProperty fontSizeProperty) {
            HBox.setHgrow(title, Priority.ALWAYS);
            HBox.setHgrow(spacer, Priority.ALWAYS);
            status.getStyleClass().add("status-chip");
            doneCheckBox.setFocusTraversable(false);

            // Slider controls real todo list text sizes, not only a sample label.
            title.styleProperty().bind(Bindings.format("-fx-font-size: %.0fpx;", fontSizeProperty));
            status.styleProperty().bind(Bindings.format("-fx-font-size: %.0fpx; -fx-font-weight: 700;", fontSizeProperty.subtract(2)));
        }

        @Override
        protected void updateItem(TaskItem item, boolean empty) {
            super.updateItem(item, empty);

            if (boundItem != null) {
                doneCheckBox.selectedProperty().unbindBidirectional(boundItem.doneProperty());
                boundItem.doneProperty().removeListener(doneListener);
                boundItem = null;
            }

            if (empty || item == null) {
                setGraphic(null);
                return;
            }

            boundItem = item;

            title.textProperty().unbind();
            title.textProperty().bind(item.titleProperty());

            doneCheckBox.selectedProperty().bindBidirectional(item.doneProperty());
            item.doneProperty().addListener(doneListener);
            applyDoneState(item.isDone());

            setGraphic(row);
        }

        private void applyDoneState(boolean done) {
            title.getStyleClass().remove("task-done");
            if (done) {
                title.getStyleClass().add("task-done");
            }

            status.setText(done ? "DONE" : "TODO");
            status.getStyleClass().removeAll("status-done", "status-todo");
            status.getStyleClass().add(done ? "status-done" : "status-todo");
        }
    }
}
