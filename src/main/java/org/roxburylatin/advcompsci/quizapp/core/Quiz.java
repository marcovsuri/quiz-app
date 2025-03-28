package org.roxburylatin.advcompsci.quizapp.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

/** A Quiz */
public class Quiz {
  private final @NotNull HashMap<Question.Difficulty, QuestionGroup> questionGroups;
  private int numQuestionsAsked = 0;
  private int numQuestionsCorrect = 0;
  private @NotNull QuestionLoader loader;
  private Question currentQuestion;
  private boolean lastAnswerCorrect = false;

  /**
   * Create a quiz
   *
   * @param questionGroups question groups for the quiz
   */
  public Quiz(@NotNull HashMap<Question.Difficulty, QuestionGroup> questionGroups) {
    this.questionGroups = questionGroups;
    this.loader = new ConsecutiveLoader(questionGroups);
  }

  /**
   * Load a quiz from a file questions, answer choices, difficulty, and correct answers are stored
   * (same loading strategy as {@link QuestionGroup#generateFromFile(File)}
   *
   * @param file file
   * @return new quiz
   * @throws FileNotFoundException if the file cannot be read
   */
  public static @NotNull Quiz fromFile(@NotNull File file) throws FileNotFoundException {
    // Generate the question groups from the file
    HashMap<Question.Difficulty, QuestionGroup> questionGroups =
        QuestionGroup.generateFromFile(file);
    return new Quiz(questionGroups);
  }

  /**
   * Load the next question based on the strategy of the current loader. Once a question is
   * selected, it will continue to be returned until answered with {@link
   * Quiz#submitAnswer(Question.Choice)}
   *
   * @return selected question
   * @see QuestionLoader
   */
  public Question loadQuestion() {
    if (currentQuestion == null) {
      // Current question does not exist => load new question from loader
      currentQuestion = loader.loadQuestion(lastAnswerCorrect);
      numQuestionsAsked++;
    }

    return currentQuestion;
  }

  /**
   * Submit an answer to the selected question
   *
   * @param answer answer to the question
   * @return whether the answer was correct
   * @throws IllegalStateException if attempting to answer a question before loading a new question
   *     using {@link Quiz#loadQuestion()}
   */
  public boolean submitAnswer(Question.Choice answer) throws IllegalStateException {
    if (currentQuestion == null) {
      // TODO - Exception Message (correct exception type just needs nice message)
      throw new IllegalStateException();
    }

    // Grade the selected question
    lastAnswerCorrect = currentQuestion.grade(answer);

    if (lastAnswerCorrect) {
      numQuestionsCorrect++;
    }

    // Update question loader
    updateLoader();

    // Reset current question
    currentQuestion = null;

    return lastAnswerCorrect;
  }

  /**
   * Update the question loader
   *
   * @see QuestionLoader
   */
  private void updateLoader() {
    loader = loader.evaluateStrategy(numQuestionsAsked, numQuestionsCorrect, questionGroups);
  }
}
