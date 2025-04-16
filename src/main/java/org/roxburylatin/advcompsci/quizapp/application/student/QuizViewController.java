package org.roxburylatin.advcompsci.quizapp.application.student;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.roxburylatin.advcompsci.quizapp.application.Request;
import org.roxburylatin.advcompsci.quizapp.backend.ServerException;
import org.roxburylatin.advcompsci.quizapp.core.Question;

public class QuizViewController {
  @FXML private Button fiftyFiftyButton;
  @FXML private Button askTeacherButton;
  @FXML private Button anotherOneButton;
  @FXML private Button submitButton;

  @FXML private WebView questionView;

  @FXML private RadioButton radioA;

  @FXML private RadioButton radioB;

  @FXML private RadioButton radioC;

  @FXML private RadioButton radioD;

  @FXML private WebView webViewA;
  @FXML private WebView webViewB;
  @FXML private WebView webViewC;
  @FXML private WebView webViewD;

  @FXML private ScrollPane questionScrollPane;
  @FXML private Text questionNumDisplay;

  @FXML
  public void initialize() {
    ToggleGroup answerToggleGroup = new ToggleGroup();
    radioA.setToggleGroup(answerToggleGroup);
    radioB.setToggleGroup(answerToggleGroup);
    radioC.setToggleGroup(answerToggleGroup);
    radioD.setToggleGroup(answerToggleGroup);

    // Listen for changes in the current question
    StudentAppState.needsUpdateProperty()
        .addListener(
            (_, _, newValue) -> {
              loadView(StudentAppState.getCurrentQuestion());
            });

    // Load the initial question
    loadView(StudentAppState.getCurrentQuestion());

    // Disable Another One Button (TODO implementation)
    anotherOneButton.setDisable(true);
  }

  private void loadView(Question question) {
    if (question != null) {
      radioA.setVisible(true);
      radioB.setVisible(true);
      radioC.setVisible(true);
      radioD.setVisible(true);

      fiftyFiftyButton.setVisible(true);
      askTeacherButton.setVisible(true);
      anotherOneButton.setVisible(true);
      submitButton.setVisible(true);

      // Reset button states
      radioA.setDisable(false);
      radioB.setDisable(false);
      radioC.setDisable(false);
      radioD.setDisable(false);

      // Unselect all radio buttons
      radioA.setSelected(false);
      radioB.setSelected(false);
      radioC.setSelected(false);
      radioD.setSelected(false);

      // Reset helper buttons
      fiftyFiftyButton.setDisable(false);
      askTeacherButton.setDisable(false);

      questionView.setVisible(true);
      questionScrollPane.setVisible(true);

      questionScrollPane.setVvalue(0.0);
      questionScrollPane.setHvalue(0.0);

      // Set the question text
      WebEngine questionViewEngine = questionView.getEngine();
      questionViewEngine.loadContent("<h1>" + question.getTitle() + "</h1>");

      // Set the choices
      webViewA.getEngine().loadContent(question.getChoice(Question.Choice.A));
      webViewB.getEngine().loadContent(question.getChoice(Question.Choice.B));
      webViewC.getEngine().loadContent(question.getChoice(Question.Choice.C));
      webViewD.getEngine().loadContent(question.getChoice(Question.Choice.D));

      questionNumDisplay.setText(
          "Question " + StudentAppState.getQuiz().getProgress().numQuestionsAsked() + "/20");

    } else {
      //            // Reset button states
      //            radioA.setVisible(false);
      //            radioB.setVisible(false);
      //            radioC.setVisible(false);
      //            radioD.setVisible(false);

      questionScrollPane.setVisible(false);

      // Reset helper buttons
      fiftyFiftyButton.setVisible(false);
      askTeacherButton.setVisible(false);
      anotherOneButton.setVisible(false);

      submitButton.setVisible(false);

      questionView.setVisible(false);
      questionNumDisplay.setText("Loading...");
    }
  }

  @FXML
  private void handleFiftyFifty() {
    RadioButton[] buttons = {radioA, radioB, radioC, radioD};
    int removed = 0;

    for (RadioButton button : buttons) {
      if (!button
          .getText()
          .startsWith(StudentAppState.getCurrentQuestion().getCorrectChoice().name() + ".")) {
        button.setDisable(true);
        removed++;
        if (removed == 2) break;
      }
    }

    fiftyFiftyButton.setDisable(true);
  }

  @FXML
  private void handleAskTeacher() {
    if (StudentAppState.client == null
        || StudentAppState.firstName == null
        || StudentAppState.lastName == null) {
      // Should never happen...
      Alert alert = new Alert(Alert.AlertType.ERROR);

      alert.setTitle("Error asking for help");
      alert.setHeaderText("Unable to make requests to the teacher (client or name is null)");
      alert.showAndWait();
    }

    // Add properties to JSON object
    JSONObject json = new JSONObject();
    json.put("firstName", StudentAppState.firstName);
    json.put("lastName", StudentAppState.lastName);

    // Send request
    try {
      StudentAppState.client.send(Request.ASK_FOR_HELP, json);

      // Disable after asking successfully
      askTeacherButton.setDisable(true);
    } catch (ServerException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);

      alert.setTitle("Error asking for help");
      alert.setHeaderText("Could not communicate with teacher");
      alert.showAndWait();
    }
  }

  @FXML
  private void handleSubmit() {
    if (StudentAppState.quizSubmitted) {
      // Occurs if loading end screen does not work and user is still able to click submit button
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Cannot submit");
      alert.setHeaderText(null);
      alert.setContentText("Your quiz is already submitted. Please close the app.");
      alert.showAndWait();
    }

    // Get the selected answer
    Question.Choice selectedAnswer = null;
    if (radioA.isSelected()) {
      selectedAnswer = Question.Choice.A;
    } else if (radioB.isSelected()) {
      selectedAnswer = Question.Choice.B;
    } else if (radioC.isSelected()) {
      selectedAnswer = Question.Choice.C;
    } else if (radioD.isSelected()) {
      selectedAnswer = Question.Choice.D;
    }

    if (selectedAnswer == null) {
      // Show an alert if no answer is selected
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("No Answer Selected");
      alert.setHeaderText(null);
      alert.setContentText("Please select an answer choice");
      alert.showAndWait();
      return;
    }

    // Submit answer
    StudentAppState.submitAnswer(selectedAnswer);

    if (StudentAppState.getQuiz().getProgress().isQuizFinished()) {
      // Quiz is finished

      // Submit Quiz
      if (StudentAppState.client == null
          || StudentAppState.firstName == null
          || StudentAppState.lastName == null
          || StudentAppState.chapterNum == null) {
        // Should never happen...
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error submitting quiz");
        alert.setHeaderText(
            "Unable to submit quiz to teacher (client, name, or chapter number is null)");
        alert.showAndWait();
      }

      // Add properties to JSON object
      JSONObject json = new JSONObject();
      json.put("firstName", StudentAppState.firstName);
      json.put("lastName", StudentAppState.lastName);
      json.put("chapterNum", StudentAppState.chapterNum);
      json.put(
          "numQuestionsCorrect", StudentAppState.getQuiz().getProgress().numQuestionsCorrect());
      json.put("numQuestionsTotal", StudentAppState.getQuiz().getProgress().numQuestionsAsked());

      // Send request
      try {
        StudentAppState.client.send(Request.SUBMIT_QUIZ, json);

        // Disable after asking successfully
        askTeacherButton.setDisable(true);
      } catch (ServerException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error submitting quiz");
        alert.setHeaderText("Could not submit quiz to teacher");
        alert.showAndWait();
        return;
      }

      try {
        // Get stage
        Stage stage = (Stage) questionNumDisplay.getScene().getWindow();

        // Load the quiz view
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("end-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // Set the new scene
        stage.setScene(scene);
      } catch (IOException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error loading UI");
        alert.setHeaderText(null);
        alert.setContentText("Unable to load final UI. Your quiz is submitted.");
        alert.showAndWait();
      }
    }
  }
}
