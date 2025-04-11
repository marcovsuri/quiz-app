package org.roxburylatin.advcompsci.quizapp.application.student;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.roxburylatin.advcompsci.quizapp.core.Question;

import java.util.HashMap;

public class QuizViewController {

    @FXML private Button buttonA;
    @FXML private Button buttonB;
    @FXML private Button buttonC;
    @FXML private Button buttonD;
    @FXML private Button fiftyFiftyButton;
    @FXML private Button askTeacherButton;
    @FXML private Button submitButton;
    @FXML private Text helpText;
    @FXML private Text questionTitleText;

    private Question.Choice correctChoice = Question.Choice.C;

    @FXML
    private void handleFiftyFifty() {
        Button[] buttons = { buttonA, buttonB, buttonC, buttonD };
        int removed = 0;

        for (Button button : buttons) {
            if (!button.getText().startsWith(correctChoice.name() + ".")) {
                button.setStyle("-fx-text-fill: gray; -fx-strikethrough: true;");
                button.setDisable(true);
                removed++;
                if (removed == 2) break;
            }
        }

        fiftyFiftyButton.setDisable(true);
        fiftyFiftyButton.setStyle("-fx-opacity: 0.5;");
    }

    @FXML
    private void handleAskTeacher() {
        helpText.setVisible(true);
        askTeacherButton.setDisable(true);
        askTeacherButton.setStyle("-fx-opacity: 0.5;");
    }

    @FXML
    private void handleSubmit() {
        // Your submit logic here...

        // Hide help message
        helpText.setVisible(false);
    }

    @FXML
    public void initialize() {
        QuizViewState.needsUpdateProperty().addListener((obs, oldVal, newVal) -> updateView());

        QuizViewState.updateCurrentQuestion();
        updateView();
    }

    public void updateView() {
        Question currentQuestion = QuizViewState.getCurrentQuestion();
        if (currentQuestion == null) {
            return;
        }

        questionTitleText.setText(currentQuestion.getTitle());
        buttonA.setText(currentQuestion.getChoice(Question.Choice.A));
        buttonB.setText(currentQuestion.getChoice(Question.Choice.B));
        buttonC.setText(currentQuestion.getChoice(Question.Choice.C));
        buttonD.setText(currentQuestion.getChoice(Question.Choice.D));
    }
}