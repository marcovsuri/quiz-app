package org.roxburylatin.advcompsci.quizapp.application.student;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.roxburylatin.advcompsci.quizapp.core.Question;
import org.roxburylatin.advcompsci.quizapp.core.QuestionGroup;
import org.roxburylatin.advcompsci.quizapp.core.Quiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AppState {
  private static Quiz quiz;

  static {
    // TODO - Testing remove once connected with rest of app
    ArrayList<Question> easyQuestions = new ArrayList<>();
    for (int i = 0; i <= 5; i++) {
      String title = "Easy Question " + (i + 1);
      HashMap<Question.Choice, String> choices = new HashMap<>();
      Random random = new Random();
      int crtChoiceNum = random.nextInt(4) + 1;

      choices.put(Question.Choice.A, "Option 1" + ((crtChoiceNum == 1) ? " (Correct)" : ""));
      choices.put(Question.Choice.B, "Option 2" + ((crtChoiceNum == 1) ? " (Correct)" : ""));
      choices.put(Question.Choice.C, "Option 3" + ((crtChoiceNum == 1) ? " (Correct)" : ""));
      choices.put(Question.Choice.D, "Option 4" + ((crtChoiceNum == 1) ? " (Correct)" : ""));

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
      choices.put(Question.Choice.B, "Option 2" + ((crtChoiceNum == 1) ? " (Correct)" : ""));
      choices.put(Question.Choice.C, "Option 3" + ((crtChoiceNum == 1) ? " (Correct)" : ""));
      choices.put(Question.Choice.D, "Option 4" + ((crtChoiceNum == 1) ? " (Correct)" : ""));

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
      choices.put(Question.Choice.B, "Option 2" + ((crtChoiceNum == 1) ? " (Correct)" : ""));
      choices.put(Question.Choice.C, "Option 3" + ((crtChoiceNum == 1) ? " (Correct)" : ""));
      choices.put(Question.Choice.D, "Option 4" + ((crtChoiceNum == 1) ? " (Correct)" : ""));

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

    quiz = new Quiz(questionGroups);
  }


  public static Quiz getQuiz() {
    return quiz;
  }

  public static void main(String[] args) {
    System.out.println("HELLO WORLD");
  }

}
