package org.roxburylatin.advcompsci.quizapp.application.student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.roxburylatin.advcompsci.quizapp.application.Request;
import org.roxburylatin.advcompsci.quizapp.backend.ServerException;
import org.roxburylatin.advcompsci.quizapp.core.Question;

public class QuizViewController {
  @FXML private Button fiftyFiftyButton;
  @FXML private Button askTeacherButton;
  //  @FXML private Button anotherOneButton;
  @FXML private Button submitButton;

  @FXML private WebView questionView;

  @FXML private WebView choicesView;

  @FXML private Text questionNumDisplay;

  @FXML private Text title;

  @FXML
  public void initialize() {
    // Listen for changes in the current question
    AppState.needsUpdateProperty()
        .addListener(
            (_, _, newValue) -> {
              loadView(AppState.getCurrentQuestion());
            });

    // Load styles in webviews
    questionView
        .getEngine()
        .setUserStyleSheetLocation(getClass().getResource("embedded-styles.css").toString());
    choicesView
        .getEngine()
        .setUserStyleSheetLocation(getClass().getResource("embedded-styles.css").toString());

    // Load the initial question
    loadView(AppState.getCurrentQuestion());
  }

  private void loadView(Question question) {
    if (question != null) {
      fiftyFiftyButton.setVisible(true);
      askTeacherButton.setVisible(true);
      submitButton.setVisible(true);
      questionView.setVisible(true);
      choicesView.setVisible(true);

      // Set the question text
      questionView.getEngine().loadContent(question.getTitle());

      // Set the choices
      choicesView.getEngine().loadContent(generateChoicesContent(question));

      // Set the choices
      questionNumDisplay.setText(
          "Question " + AppState.getQuiz().getProgress().numQuestionsAsked() + "/20");

      title.setText(
          "Quiz App ("
              + AppState.firstName
              + " "
              + AppState.lastName
              + ", "
              + AppState.chapterNum
              + ")");

    } else {
      // Reset helper buttons
      fiftyFiftyButton.setVisible(false);
      askTeacherButton.setVisible(false);

      submitButton.setVisible(false);

      questionView.setVisible(false);
      choicesView.setVisible(false);
      questionNumDisplay.setText("Loading...");

      title.setText(
          "Quiz App ("
              + AppState.firstName
              + " "
              + AppState.lastName
              + ", "
              + AppState.chapterNum
              + ")");
    }
  }

  private String generateChoicesContent(Question currentQuestion) {
    StringBuilder html = new StringBuilder();

    html.append("<!DOCTYPE html>");
    html.append("<html><head></head><body>");

    // Add the prompt
    html.append("<form id='quizForm'>");

    // Render the choices
    List<Question.Choice> choices =
        List.of(Question.Choice.A, Question.Choice.B, Question.Choice.C, Question.Choice.D);

    for (Question.Choice choice : choices) {
      String choiceText = currentQuestion.getChoice(choice);

      html.append("<div class='choice'>")
          .append("<input type='radio' name='answer' value='")
          .append(choice)
          .append("'>")
          .append("<strong class='choiceLabel'>")
          .append(choice)
          .append(". </strong>")
          .append(choiceText)
          .append("</div>");
    }

    html.append("</form></body></html>");
    return html.toString();
  }

  @FXML
  private void handleFiftyFifty() {
    Question.Choice[] choices = {
      Question.Choice.A, Question.Choice.B, Question.Choice.C, Question.Choice.D
    };
    ArrayList<Question.Choice> removables = new ArrayList<>();

    for (Question.Choice choice : choices) {
      if (!choice.equals(AppState.getCurrentQuestion().getCorrectChoice())) {
        removables.add(choice);
      }
    }

    Random rand = new Random();

    // Get two distinct random indices
    int firstIndex = rand.nextInt(removables.size());
    int secondIndex;
    do {
      secondIndex = rand.nextInt(removables.size());
    } while (secondIndex == firstIndex);

    choicesView
        .getEngine()
        .executeScript(
            "var btn = document.querySelector('input[name=\"answer\"][value=\""
                + removables.get(firstIndex).toString()
                + "\"]');"
                + "if (btn) { btn.checked = false; btn.disabled = true; }");

    choicesView
        .getEngine()
        .executeScript(
            "var btn = document.querySelector('input[name=\"answer\"][value=\""
                + removables.get(secondIndex).toString()
                + "\"]');"
                + "if (btn) { btn.checked = false; btn.disabled = true; }");

    fiftyFiftyButton.setDisable(true);
  }

  @FXML
  private void handleAskTeacher() {
    if (AppState.client == null || AppState.firstName == null || AppState.lastName == null) {
      // Should never happen...
      Alert alert = new Alert(Alert.AlertType.ERROR);

      alert.setTitle("Error asking for help");
      alert.setHeaderText("Unable to make requests to the teacher (client or name is null)");
      alert.showAndWait();
    }

    // Add properties to JSON object
    JSONObject json = new JSONObject();
    json.put("firstName", AppState.firstName);
    json.put("lastName", AppState.lastName);

    // Send request
    try {
      AppState.client.send(Request.ASK_FOR_HELP, json);

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
    if (AppState.quizSubmitted) {
      // Occurs if loading end screen does not work and user is still able to click submit button
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Cannot submit");
      alert.setHeaderText(null);
      alert.setContentText("Your quiz is already submitted. Please close the app.");
      alert.showAndWait();
    }

    // Get the selected choice
    Object selectedValue =
        choicesView
            .getEngine()
            .executeScript(
                "(() => {"
                    + "   const checked = document.querySelector('input[name=\"answer\"]:checked');"
                    + "   return checked ? checked.value : null;"
                    + "})()");

    Question.Choice selectedChoice;
    switch ((String) selectedValue) {
      case null -> selectedChoice = null;
      case "A" -> selectedChoice = Question.Choice.A;
      case "B" -> selectedChoice = Question.Choice.B;
      case "C" -> selectedChoice = Question.Choice.C;
      case "D" -> selectedChoice = Question.Choice.D;
      default -> selectedChoice = null;
    }

    if (selectedChoice == null) {
      // Show an alert if no answer is selected
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("No Answer Selected");
      alert.setHeaderText(null);
      alert.setContentText("Please select an answer choice");
      alert.showAndWait();
      return;
    }

    // Submit answer
    AppState.submitAnswer(selectedChoice);

    if (AppState.getQuiz().getProgress().isQuizFinished()) {
      // Quiz is finished

      // Submit Quiz
      if (AppState.client == null
          || AppState.firstName == null
          || AppState.lastName == null
          || AppState.chapterNum == null) {
        // Should never happen...
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error submitting quiz");
        alert.setHeaderText(
            "Unable to submit quiz to teacher (client, name, or chapter number is null)");
        alert.showAndWait();
      }

      // Add properties to JSON object
      JSONObject json = new JSONObject();
      json.put("firstName", AppState.firstName);
      json.put("lastName", AppState.lastName);
      json.put("chapterNum", AppState.chapterNum);
      json.put("numQuestionsCorrect", AppState.getQuiz().getProgress().numQuestionsCorrect());
      json.put("numQuestionsTotal", AppState.getQuiz().getProgress().numQuestionsAnswered());

      // Send request
      try {
        AppState.client.send(Request.SUBMIT_QUIZ, json);

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
