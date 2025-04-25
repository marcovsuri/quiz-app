package org.roxburylatin.advcompsci.quizapp.application.teacher;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The {@code Application} class serves as the entry point for the Teacher Quiz App. It extends
 * {@link javafx.application.Application} to create a JavaFX application.
 *
 * <p>The main method launches the JavaFX application, and the {@code start} method initializes the
 * primary stage with the specified FXML layout and scene dimensions.
 *
 * @see javafx.application.Application
 * @see javafx.stage.Stage
 * @see javafx.fxml.FXMLLoader
 */
public class Application extends javafx.application.Application {

  /**
   * The main method serves as the entry point for the JavaFX application. Users should use this
   * method to launch the application. It launches the application by calling the {@link
   * javafx.application.Application#launch(String...)} method.
   *
   * @param args Command-line arguments passed to the application (not used).
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * The start method initializes the primary stage of the JavaFX application. This is an internal
   * method called by the JavaFX runtime. It loads the "landing-view.fxml" file to define the user
   * interface and sets the scene dimensions.
   *
   * @param stage The primary stage for this application, onto which the application scene can be
   *     set.
   * @throws IOException If there is an error loading the FXML file.
   */
  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("teacher-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load());

    // Load the CSS file
    String cssPath = Application.class.getResource("teacher-view-styles.css").toExternalForm();
    scene.getStylesheets().add(cssPath);

    stage.setTitle("Quiz App (Teacher View)");
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();

    // Quit app when primary window closes
    stage.setOnCloseRequest(
        event -> {
          System.out.println("Shutting down...");
          Platform.exit(); // Clean JavaFX shutdown
          System.exit(0); // Optional: kill any stray threads (use carefully)
        });
  }
}
