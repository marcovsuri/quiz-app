package org.roxburylatin.advcompsci.quizapp.application.student;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.roxburylatin.advcompsci.quizapp.application.Request;
import org.roxburylatin.advcompsci.quizapp.backend.Client;
import org.roxburylatin.advcompsci.quizapp.backend.ServerException;
import org.roxburylatin.advcompsci.quizapp.core.*;

/**
 * The LandingViewController class is responsible for managing the landing view of the quiz
 * application. It handles user input for starting a quiz, including first name, last name, IP
 * address, port number, and chapter number. This class validates user input and initiates the quiz
 * request to the server.
 *
 * <p>FXML Annotations: - The {@code goButton} is a button that triggers the quiz start process. -
 * The {@code firstNameField} is a text field for entering the user's first name. - The {@code
 * lastNameField} is a text field for entering the user's last name. - The {@code ipField} is a text
 * field for entering the server's IP address. - The {@code portField} is a text field for entering
 * the server's port number. - The {@code chapterComboBox} is a combo box for selecting the chapter
 * number.
 *
 * <p>Methods: - {@code handleStartQuiz()}: Validates user input and initiates the quiz request to
 * the server.
 */
public class LandingViewController {
  @FXML private Button goButton;
  @FXML private TextField firstNameField;
  @FXML private TextField lastNameField;
  @FXML private TextField ipField;
  @FXML private TextField portField;
  @FXML private ComboBox<String> chapterComboBox;

  /**
   * Handles the start quiz button click event. This method validates user input, constructs a
   * request to the server, and initiates the quiz process. It ensures that all required fields are
   * filled and that the port number is valid. If any validation fails, an error alert is displayed.
   * If the quiz request is successful, it loads the quiz view.
   *
   * @throws IOException if there is an error loading the quiz view.
   */
  @FXML
  private void handleStartQuiz() throws IOException {
    // Get user input
    String firstName = firstNameField.getText();
    String lastName = lastNameField.getText();
    String ipAddress = ipField.getText();
    String portText = portField.getText();
    String chapterNumVal = chapterComboBox.getValue();

    // Ensure all fields are occupied
    if (firstName.isEmpty()
        || lastName.isEmpty()
        || ipAddress.isEmpty()
        || portText.isEmpty()
        || chapterNumVal == null) {
      Alert alert = new Alert(Alert.AlertType.ERROR);

      alert.setTitle("Error requesting quiz");
      alert.setHeaderText("Please fill in all the inputs (Quiz App)");
      alert.showAndWait();
      return;
    }

    // Ensure port number is a number
    int port;
    try {
      port = Integer.parseInt(portText);
    } catch (NumberFormatException _) {
      Alert alert = new Alert(Alert.AlertType.ERROR);

      alert.setTitle("Error requesting quiz");
      alert.setHeaderText("Please enter a valid port number (Quiz App)");
      alert.showAndWait();
      return;
    }

    int chapterNum;
    try {
      chapterNum = Integer.parseInt(chapterNumVal.replaceAll("[^0-9]", ""));
    } catch (NumberFormatException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);

      alert.setTitle("Error requesting quiz");
      alert.setHeaderText("Could not determine chapter number");
      alert.showAndWait();
      return;
    }

    // Set name globally
    AppState.firstName = firstName;
    AppState.lastName = lastName;

    // Set chapter number globally
    AppState.chapterNum = chapterNum;

    // Add client
    AppState.client = new Client<>(ipAddress, port);

    // Add JSON parameters for requesting quizzes
    JSONObject json = new JSONObject();

    json.put("firstName", firstName);
    json.put("lastName", lastName);
    json.put("chapterNum", chapterNum);

    // Get quiz
    String quizCsvContents;
    try {
      quizCsvContents = AppState.client.send(Request.REQUEST_QUIZ, json);
    } catch (ServerException e) {
      System.out.println(e.getMessage());
      Alert alert = new Alert(Alert.AlertType.ERROR);

      alert.setTitle("Error requesting quiz");
      alert.setHeaderText("Could not retrieve quiz");
      alert.showAndWait();
      return;
    }

    // Load quiz from csv contents
    Quiz quiz = null;
    try {
      quiz = Quiz.fromCsvContent(quizCsvContents);
    } catch (IOException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);

      alert.setTitle("Error requesting quiz");
      alert.setHeaderText("Received incorrect quiz format");
      alert.showAndWait();
    }

    // Set Quiz
    AppState.setQuiz(quiz);

    // Get the current stage
    Stage stage = (Stage) goButton.getScene().getWindow();

    // Load the quiz view
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("question-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 800, 600);

    // Set the new scene
    stage.setScene(scene);
  }
}
