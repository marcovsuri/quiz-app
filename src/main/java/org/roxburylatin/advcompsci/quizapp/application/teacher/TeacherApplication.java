package org.roxburylatin.advcompsci.quizapp.application.teacher;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TeacherApplication extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader =
        new FXMLLoader(TeacherApplication.class.getResource("teacher-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load());

    // Load the CSS file
    String cssPath =
        TeacherApplication.class.getResource("teacher-view-styles.css").toExternalForm();
    scene.getStylesheets().add(cssPath);

    stage.setTitle("Quiz App (Teacher View)");
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
  }
}
