package org.roxburylatin.advcompsci.quizapp.application.student;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EndViewController {
  @FXML private Label scoreLabel;

  @FXML
  public void initialize() {
    scoreLabel.setVisible(false);
  }

  @FXML
  private void handleGetScore() {
    int correct = StudentAppState.getQuiz().getProgress().numQuestionsCorrect();
    int total = StudentAppState.getQuiz().getProgress().numQuestionsAnswered();
    double percent = (double) correct / total * 100;

    scoreLabel.setText(String.format("You scored %d/%d (%.2f%%)", correct, total, percent));
    scoreLabel.setVisible(true);
  }
}
