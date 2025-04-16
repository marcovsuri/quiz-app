package org.roxburylatin.advcompsci.quizapp.application.student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.roxburylatin.advcompsci.quizapp.application.Request;
import org.roxburylatin.advcompsci.quizapp.backend.Client;
import org.roxburylatin.advcompsci.quizapp.backend.ServerException;
import org.roxburylatin.advcompsci.quizapp.core.*;

public class LandingViewController {
  @FXML private Button goButton;
  @FXML private TextField firstNameField;
  @FXML private TextField lastNameField;
  @FXML private TextField ipField;
  @FXML private TextField portField;

  private Quiz generateDemoQuiz() {
    // TODO - Testing remove once connected with rest of app
    ArrayList<Question> easyQuestions = new ArrayList<>();
    for (int i = 0; i <= 5; i++) {
      String title = "Easy Question " + (i + 1);
      HashMap<Question.Choice, String> choices = new HashMap<>();
      Random random = new Random();
      int crtChoiceNum = random.nextInt(4) + 1;

      choices.put(Question.Choice.A, "Option 1" + ((crtChoiceNum == 1) ? " (Correct)" : ""));
      choices.put(Question.Choice.B, "Option 2" + ((crtChoiceNum == 2) ? " (Correct)" : ""));
      choices.put(Question.Choice.C, "Option 3" + ((crtChoiceNum == 3) ? " (Correct)" : ""));
      choices.put(Question.Choice.D, "Option 4" + ((crtChoiceNum == 4) ? " (Correct)" : ""));

      Question.Choice crtChoice = Question.Choice.A;

      switch (crtChoiceNum) {
        case 1 -> crtChoice = Question.Choice.A;
        case 2 -> crtChoice = Question.Choice.B;
        case 3 -> crtChoice = Question.Choice.C;
        case 4 -> crtChoice = Question.Choice.D;
      }

      Question question = new Question(title, choices, crtChoice, Question.Difficulty.EASY);
      easyQuestions.add(question);
    }

    ArrayList<Question> mediumQuestions = new ArrayList<>();
    for (int i = 0; i <= 5; i++) {
      String title = "Medium Question " + (i + 1);
      HashMap<Question.Choice, String> choices = new HashMap<>();
      Random random = new Random();
      int crtChoiceNum = random.nextInt(4) + 1;

      choices.put(Question.Choice.A, "Option 1" + ((crtChoiceNum == 1) ? " (Correct)" : ""));
      choices.put(Question.Choice.B, "Option 2" + ((crtChoiceNum == 2) ? " (Correct)" : ""));
      choices.put(Question.Choice.C, "Option 3" + ((crtChoiceNum == 3) ? " (Correct)" : ""));
      choices.put(Question.Choice.D, "Option 4" + ((crtChoiceNum == 4) ? " (Correct)" : ""));

      Question.Choice crtChoice = Question.Choice.A;

      switch (crtChoiceNum) {
        case 1 -> crtChoice = Question.Choice.A;
        case 2 -> crtChoice = Question.Choice.B;
        case 3 -> crtChoice = Question.Choice.C;
        case 4 -> crtChoice = Question.Choice.D;
      }

      Question question = new Question(title, choices, crtChoice, Question.Difficulty.MEDIUM);
      mediumQuestions.add(question);
    }

    ArrayList<Question> hardQuestions = new ArrayList<>();
    for (int i = 0; i <= 5; i++) {
      String title = "Hard Question " + (i + 1);
      HashMap<Question.Choice, String> choices = new HashMap<>();
      Random random = new Random();
      int crtChoiceNum = random.nextInt(4) + 1;

      choices.put(Question.Choice.A, "Option 1" + ((crtChoiceNum == 1) ? " (Correct)" : ""));
      choices.put(Question.Choice.B, "Option 2" + ((crtChoiceNum == 2) ? " (Correct)" : ""));
      choices.put(Question.Choice.C, "Option 3" + ((crtChoiceNum == 3) ? " (Correct)" : ""));
      choices.put(Question.Choice.D, "Option 4" + ((crtChoiceNum == 4) ? " (Correct)" : ""));

      Question.Choice crtChoice = Question.Choice.A;

      switch (crtChoiceNum) {
        case 1 -> crtChoice = Question.Choice.A;
        case 2 -> crtChoice = Question.Choice.B;
        case 3 -> crtChoice = Question.Choice.C;
        case 4 -> crtChoice = Question.Choice.D;
      }

      Question question = new Question(title, choices, crtChoice, Question.Difficulty.HARD);
      hardQuestions.add(question);
    }

    HashMap<Question.Difficulty, QuestionGroup> questionGroups = new HashMap<>();
    questionGroups.put(
        Question.Difficulty.EASY, new QuestionGroup(easyQuestions, Question.Difficulty.EASY));
    questionGroups.put(
        Question.Difficulty.MEDIUM, new QuestionGroup(mediumQuestions, Question.Difficulty.MEDIUM));
    questionGroups.put(
        Question.Difficulty.HARD, new QuestionGroup(hardQuestions, Question.Difficulty.HARD));

    return new Quiz(questionGroups);
  }

  @FXML
  private void handleStartQuiz() throws IOException {
    // Get user input
    String firstName = firstNameField.getText();
    String lastName = lastNameField.getText();
    String ipAddress = ipField.getText();
    String portText = portField.getText();

    // Ensure all fields are occupied
    if (firstName.isEmpty() || lastName.isEmpty() || ipAddress.isEmpty() || portText.isEmpty()) {
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

    // Set name globally
    StudentAppState.firstName = firstName;
    StudentAppState.lastName = lastName;

    // Set chapter number globally
    StudentAppState.chapterNum = 1; // TODO - change

    // Add client
    StudentAppState.client = new Client<>(ipAddress, port);

    // Add JSON parameters for requesting quizzes
    JSONObject json = new JSONObject();

    json.put("firstName", firstName);
    json.put("lastName", lastName);
    json.put("chapterNum", 1); // TODO - change

    // Get quiz
    String quizCsvContents;
    try {
      quizCsvContents = StudentAppState.client.send(Request.REQUEST_QUIZ, json);
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
    StudentAppState.setQuiz(quiz);

    // Get the current stage
    Stage stage = (Stage) goButton.getScene().getWindow();

    // Load the quiz view
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("question-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 800, 600);

    // Set the new scene
    stage.setScene(scene);
  }
}
