# Báo Cáo Demo Coverage - JavaFX Todo

Ngày cập nhật: 2026-03-15  
Mục tiêu tài liệu: tổng hợp mức độ hoàn thành theo từng yêu cầu demo, nêu rõ feature nào đóng góp vào coverage, kèm trích dẫn mã nguồn.

---

## 1. Tóm tắt coverage

| Mục | Nhóm yêu cầu | Trạng thái | Coverage |
|---|---|---|---|
| 1 | Tách UI/Logic bằng FXML + Controller | Hoàn thành | 100% |
| 2 | CSS + Visual Effects | Hoàn thành | 100% |
| 3 | Data Binding + Animation | Hoàn thành | 100% |
| 4 | Concurrency (Task/Service) | Chưa triển khai | 0% |

Kết quả tổng: 3/4 mục hoàn thành, tương đương 75%.

---

## 2. Coverage chi tiết theo yêu cầu

## Mục 1 - Tách UI/Logic bằng FXML + Controller

Trạng thái: Hoàn thành

### Feature đóng góp coverage

- Feature 1.1: Màn hình đăng nhập tách riêng bằng FXML
  - File: src/main/resources/com/example/todo/login-view.fxml
  - Dấu hiệu coverage: khai báo fx:controller="com.example.todo.LoginController"
- Feature 1.2: Màn hình Todo tách riêng bằng FXML
  - File: src/main/resources/com/example/todo/todo-view.fxml
  - Dấu hiệu coverage: khai báo fx:controller="com.example.todo.TodoController"
- Feature 1.3: Điều hướng giữa 2 màn hình qua App
  - File: src/main/java/com/example/todo/App.java
  - Dấu hiệu coverage: dùng FXMLLoader để load todo-view.fxml và scene.setRoot(root)
- Feature 1.4: Logic đăng nhập nằm trong controller, không nằm trong App
  - File: src/main/java/com/example/todo/LoginController.java
  - Dấu hiệu coverage: validate username/password trong onLogin()

### Trích dẫn mã then chốt

```java
// App.java
public static void showTodoView(String username) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/example/todo/todo-view.fxml"));
    Parent root = loader.load();
    TodoController controller = loader.getController();
    controller.setLoggedInUser(username);
    scene.setRoot(root);
}
```

### Demo nhìn như thế nào

- Người dùng vào màn hình login trước, nhập username/password.
- Bấm Login thành công thì chuyển sang màn hình Todo trong cùng một cửa sổ.
- Giao diện và logic được tách rõ: FXML lo layout, Controller lo xử lý.

---

## Mục 2 - CSS + Visual Effects

Trạng thái: Hoàn thành

### Feature đóng góp coverage

- Feature 2.1: Style tách riêng bằng todo.css
  - File: src/main/resources/com/example/todo/todo.css
  - Dấu hiệu coverage: toàn bộ màu, bo góc, typography, state hover/focus nằm trong CSS
- Feature 2.2: DropShadow cho thẻ nội dung Todo
  - File: src/main/java/com/example/todo/TodoController.java
  - Dấu hiệu coverage: todoCard.setEffect(shadow)
- Feature 2.3: Reflection cho tiêu đề
  - File: src/main/java/com/example/todo/TodoController.java
  - Dấu hiệu coverage: titleLabel.setEffect(reflection)
- Feature 2.4: GaussianBlur nền khi mở popup tạo list
  - File: src/main/java/com/example/todo/TodoController.java
  - Dấu hiệu coverage: mainContent.setEffect(new GaussianBlur(8))

### Trích dẫn mã then chốt

```java
// TodoController.java
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
```

### Demo nhìn như thế nào

- Card Todo nổi khối rõ hơn nhờ đổ bóng.
- Tiêu đề có phản chiếu nhẹ.
- Khi mở popup New List, nền mờ (blur) ngay lập tức để tập trung vào modal.

---

## Mục 3 - Data Binding + Animation

Trạng thái: Hoàn thành

### Feature đóng góp coverage

- Feature 3.1: Disable/Enable nút theo trạng thái dữ liệu (binding)
  - File: src/main/java/com/example/todo/TodoController.java
  - Dấu hiệu coverage:
    - addButton.disableProperty().bind(taskInput.textProperty().isEmpty())
    - removeButton/markDoneButton bind theo selectedItemProperty().isNull()
- Feature 3.2: Binding 2 chiều checkbox <-> trạng thái done
  - File: src/main/java/com/example/todo/TodoController.java
  - Dấu hiệu coverage: doneCheckBox.selectedProperty().bindBidirectional(item.doneProperty())
- Feature 3.3: Slider điều chỉnh trực tiếp cỡ chữ toàn bộ item trong list
  - File: src/main/java/com/example/todo/TodoController.java
  - Dấu hiệu coverage:
    - listFontSize.bind(fontSizeSlider.valueProperty())
    - taskListView.setCellFactory(listView -> new TodoTaskCell(listFontSize))
    - title/status styleProperty bind theo fontSizeProperty
- Feature 3.4: Animation hover button bằng ScaleTransition
  - File: src/main/java/com/example/todo/TodoController.java
- Feature 3.5: Animation popup bằng FadeTransition
  - File: src/main/java/com/example/todo/TodoController.java
  - Dấu hiệu coverage hiện tại:
    - fadeIn: Duration.millis(45)
    - fadeOut: Duration.millis(40)
- Feature 3.6: Fade khi chuyển list
  - File: src/main/java/com/example/todo/TodoController.java
  - Dấu hiệu coverage: FadeTransition(Duration.millis(220), taskListView)

### Trích dẫn mã then chốt

```java
// Binding slider -> font toàn bộ task row
private void bindTodoListFontSize() {
    listFontSize.bind(fontSizeSlider.valueProperty());
    fontSizeValueLabel.textProperty().bind(
        Bindings.format("Todo text: %.0f px", fontSizeSlider.valueProperty())
    );
}

private TodoTaskCell(ReadOnlyDoubleProperty fontSizeProperty) {
    title.styleProperty().bind(Bindings.format("-fx-font-size: %.0fpx;", fontSizeProperty));
    status.styleProperty().bind(Bindings.format(
        "-fx-font-size: %.0fpx; -fx-font-weight: 700;",
        fontSizeProperty.subtract(2)
    ));
}
```

### Demo nhìn như thế nào

- Nút Add chỉ sáng khi có nội dung nhập.
- Kéo slider sẽ thấy chữ của toàn bộ task trong list đổi cỡ theo thời gian thực.
- Hover nút có hiệu ứng phóng nhẹ, popup mở/đóng nhanh và mượt.
- Đổi list có fade-in giúp chuyển trạng thái tự nhiên hơn.

---

## Mục 4 - Concurrency (Task/Service)

Trạng thái: Chưa triển khai

### Feature cần có để được tính coverage

- Feature 4.1: Tạo tác vụ nền bằng javafx.concurrent.Task hoặc Service
- Feature 4.2: Bind tiến trình ra ProgressBar
- Feature 4.3: Bind thông điệp trạng thái ra Label
- Feature 4.4: Có nút Cancel và xử lý isCancelled() trong vòng lặp
- Feature 4.5: UI vẫn thao tác được trong lúc task chạy

### Kết luận coverage mục 4

- Hiện chưa có các thành phần trên trong code thực thi, nên coverage mục này là 0%.
- Tài liệu kế hoạch đã có tại: CONCURRENCY_DEMO_PLAN.md

---

## 3. Bản đồ feature -> coverage (tổng hợp nhanh)

- Coverage Mục 1 đến từ: login-view.fxml, todo-view.fxml, LoginController, App routing
- Coverage Mục 2 đến từ: todo.css + DropShadow + Reflection + GaussianBlur
- Coverage Mục 3 đến từ: property binding + bidirectional binding + slider-to-list binding + scale/fade transitions
- Coverage Mục 4 hiện chưa có implementation runtime

---

## 4. Mức độ sẵn sàng demo hiện tại

- Có thể demo trọn vẹn 3 phần đầu ngay: kiến trúc FXML/Controller, UI/Visual effects, Binding/Animation
- Chưa thể demo phần concurrency vì chưa có Task/Service thực tế trong controller

