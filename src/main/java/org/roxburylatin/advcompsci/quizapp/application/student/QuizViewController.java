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

    private Question currentQuestion;

    {
        HashMap<Question.Choice, String> choices = new HashMap<>();
        choices.put(Question.Choice.A, "1");
        choices.put(Question.Choice.B, "2");
        choices.put(Question.Choice.C, "3");
        choices.put(Question.Choice.D, "4");
        currentQuestion = new Question("Some question", choices, Question.Choice.C, Question.Difficulty.EASY);
    }

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
}