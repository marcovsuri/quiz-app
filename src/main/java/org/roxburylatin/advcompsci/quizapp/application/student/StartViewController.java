package org.roxburylatin.advcompsci.quizapp.application.student;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import org.roxburylatin.advcompsci.quizapp.core.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class StartViewController {
    @FXML
    private Button startButton;

    @FXML
    private void handleStartQuiz() throws IOException {
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
        questionGroups.put(Question.Difficulty.EASY,
                new QuestionGroup(easyQuestions, Question.Difficulty.EASY));
        questionGroups.put(Question.Difficulty.MEDIUM,
                new QuestionGroup(mediumQuestions, Question.Difficulty.MEDIUM));
        questionGroups.put(Question.Difficulty.HARD,
                new QuestionGroup(hardQuestions, Question.Difficulty.HARD));

        Quiz quiz = new Quiz(questionGroups);
        StudentAppState.setQuiz(quiz);

        // Get the current stage
        Stage stage = (Stage) startButton.getScene().getWindow();

        // Load the quiz view
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("question-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // Set the new scene
        stage.setScene(scene);
    }
}
