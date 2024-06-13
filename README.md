# RoboRally Game Implementation

This project is a computer implementation of the board game RoboRally. It is written in Java and uses JavaFX for the graphical user interface.

## Prerequisites for playing RoboRally

Before you begin, ensure you have met the following requirements:

- You have installed a recent version of Java (Java 21 is used during development, so it is recommended).
- You have a machine running either one of these OS: Windows/Linux/MacOS.

## Running RoboRally

To run RoboRally, follow these steps:

1. Open a terminal in the project root directory.
2. Compile the project using the following command:
    ```bash
    javac -cp path/to/javafx-sdk-11.0.2/lib --module-path path/to/javafx-sdk-11.0.2/lib --add-modules javafx.controls,javafx.fxml -d bin src/*.java
    ```
   Replace `path/to/javafx-sdk-11.0.2/lib` with the path to your JavaFX SDK library.
3. Run the program using the following command:
    ```bash
    java --module-path path/to/javafx-sdk-11.0.2/lib --add-modules javafx.controls,javafx.fxml -cp bin Main
    ```
   Replace `path/to/javafx-sdk-11.0.2/lib` with the path to your JavaFX SDK library.
