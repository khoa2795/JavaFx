# JavaFX Todo Demo

Simple JavaFX Todo List demo with FXML + Controller, ready to open in Scene Builder.

## Project Files

- FXML: `src/main/resources/com/example/todo/todo-view.fxml`
- Controller: `src/main/java/com/example/todo/TodoController.java`
- App entry: `src/main/java/com/example/todo/App.java`
- Styles: `src/main/resources/com/example/todo/todo.css`

## Open In Scene Builder

1. Open Scene Builder.
2. Choose `File` -> `Open`.
3. Select `src/main/resources/com/example/todo/todo-view.fxml`.
4. If controller warnings appear, keep the `fx:controller` as-is (`com.example.todo.TodoController`) and run from Maven/IDE.

## Run The Demo

Requirements:

- Java 17+
- Maven

### Windows Setup (PowerShell)

1. Install JDK 17 (or newer).
2. Install Maven 3.9+.
3. Verify both are available in `PATH`:

```powershell
java -version
mvn -version
```

If Maven is not found, add Maven `bin` to your user `PATH` and reopen PowerShell.

Common locations:

- JDK: `C:\Program Files\Java\jdk-17`
- Maven: `C:\Program Files\apache-maven-3.9.x\bin`

Optional `JAVA_HOME` setup:

```powershell
setx JAVA_HOME "C:\Program Files\Java\jdk-17"
```

Then close and reopen PowerShell, and run:

```powershell
cd C:\path\to\JavaFx
mvn clean javafx:run
```

If PowerShell blocks scripts, run this once in an elevated PowerShell:

```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

### Linux Setup (Ubuntu/Debian)

1. Install JDK 17 and Maven:

```bash
sudo apt update
sudo apt install -y openjdk-17-jdk maven
```

2. Verify both tools are available:

```bash
java -version
mvn -version
```

3. (Optional) Set `JAVA_HOME` if needed:

```bash
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.bashrc
source ~/.bashrc
```

4. Run the project:

```bash
cd /path/to/JavaFx
mvn clean javafx:run
```

### Linux Setup (Fedora/RHEL)

Install JDK 17 and Maven:

```bash
sudo dnf install -y java-17-openjdk-devel maven
```

Run:

```bash
cd /path/to/JavaFx
mvn clean javafx:run
```

### Linux Setup (Arch)

Install JDK 17 and Maven:

```bash
sudo pacman -S --needed jdk17-openjdk maven
```

Run:

```bash
cd /path/to/JavaFx
mvn clean javafx:run
```

Quick Maven-only install on Ubuntu/Debian (if Java is already installed):

```bash
sudo apt install maven
```

Quick run command:

```bash
mvn javafx:run
```

### Windows Troubleshooting

- `mvn : The term 'mvn' is not recognized`
	- Maven is not in `PATH`. Add Maven `bin` and restart terminal.
- `java is not recognized`
	- JDK is not in `PATH` (or only JRE installed). Install JDK 17 and re-check `java -version`.
- JavaFX app does not open from terminal
	- Re-run with `mvn -e javafx:run` and check that Java/Maven versions are correctly detected.

### Linux Troubleshooting

- `mvn: command not found`
	- Maven is not installed or not in `PATH`. Install Maven with your distro package manager, then restart terminal.
- `java: command not found`
	- JDK 17 is missing. Install OpenJDK 17 and verify with `java -version`.
- GUI window does not appear on Linux/WSL
	- Ensure a display server is available and `DISPLAY` is set.
	- For WSL2, use WSLg (Windows 11) or an X server, then confirm with `echo $DISPLAY`.
- JavaFX run fails with plugin details
	- Run `mvn -e javafx:run` for full error details.

## Demo Usage

- Type a task and press `Enter` or click `Add`
- Select a task and click `Toggle Done`
- Remove selected with `Remove Selected`
- Clear completed with `Clear Done`
