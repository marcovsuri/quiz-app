package org.roxburylatin.advcompsci.quizapp.application.student;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * The EndViewController class is responsible for managing the end view of the quiz application. It
 * handles the display of the user's score and provides functionality to update the score label
 * based on the quiz progress.
 *
 * <p>FXML Annotations: - The {@code scoreLabel} is a UI label element that displays the user's
 * score. - The {@code initialize()} method is called automatically after the FXML file is loaded to
 * perform any necessary initialization. - The {@code handleGetScore()} method is triggered to
 * calculate and display the user's score based on the quiz progress.
 *
 * <p>Methods: - {@code initialize()}: Hides the score label initially. - {@code handleGetScore()}:
 * Retrieves the number of correct and answered questions from the quiz progress, calculates the
 * percentage score, and updates the score label with the result.
 */
public class EndViewController {
  @FXML private Label scoreLabel;

  @FXML
  public void initialize() {
    scoreLabel.setVisible(false);
  }

  @FXML
  private void handleGetScore() {
    int correct = AppState.getQuiz().getProgress().numQuestionsCorrect();
    int total = AppState.getQuiz().getProgress().numQuestionsAnswered();
    double percent = (double) correct / total * 100;

    scoreLabel.setText(String.format("You scored %d/%d (%.2f%%)", correct, total, percent));
    scoreLabel.setVisible(true);
  }
}
