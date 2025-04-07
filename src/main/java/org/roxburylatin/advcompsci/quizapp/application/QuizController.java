package org.roxburylatin.advcompsci.quizapp.application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizController {

    @FXML private Button buttonA;
    @FXML private Button buttonB;
    @FXML private Button buttonC;
    @FXML private Button buttonD;
    @FXML private Button fiftyFiftyButton;

    {

    }
    // Set this value based on the current question
    private String correctAnswer = "A";  // Example: "A", "B", "C", or "D"

    // This method is triggered by pressing the 50/50 button
    @FXML
    private void handleFiftyFifty() {
        // Create a list of all buttons
        List<Button> allButtons = List.of(buttonA, buttonB, buttonC, buttonD);
        List<Button> incorrectButtons = new ArrayList<>();

        // Find buttons that are incorrect
        for (Button button : allButtons) {
            if (!button.getText().startsWith(correctAnswer + ".")) {
                incorrectButtons.add(button);
            }
        }

        // Shuffle and pick 2 to strike through
        Collections.shuffle(incorrectButtons);
        for (int i = 0; i < 2 && i < incorrectButtons.size(); i++) {
            Button btn = incorrectButtons.get(i);
            btn.setStyle("-fx-text-fill: gray; -fx-strikethrough: true;");
            btn.setDisable(true); // Optional: disable the button so it can't be selected
        }

        // Disable the 50/50 button itself
        fiftyFiftyButton.setDisable(true);
        fiftyFiftyButton.setStyle("-fx-opacity: 0.5;");
    }

    // Optional method to set the correct answer from outside
    public void setCorrectAnswer(String letter) {
        correctAnswer = letter;
    }
}