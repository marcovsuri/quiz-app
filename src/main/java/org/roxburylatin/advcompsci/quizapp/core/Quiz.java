package org.roxburylatin.advcompsci.quizapp.core;

import java.io.IOException;
import java.util.HashMap;

import org.jetbrains.annotations.NotNull;

/**
 * A Quiz
 */
public class Quiz {
    private final QuizProgress quizProgress = new QuizProgress();
    private @NotNull QuestionLoader loader;
    private Question currentQuestion;

    /**
     * Create a quiz
     *
     * @param questionGroups question groups for the quiz
     */
    public Quiz(@NotNull HashMap<Question.Difficulty, QuestionGroup> questionGroups) {
        this.loader = new ConsecutiveLoader(quizProgress, questionGroups);
    }

  /**
   * Creates a new Quiz instance from the provided CSV content.
   *
   * @param contents The CSV content as a non-null string. The content should adhere to the expected
   *     format for generating question groups.
   * @return A new Quiz instance populated with question groups parsed from the CSV content.
   * @throws IOException If an error occurs while processing the CSV content.
   * @see QuestionGroup#generateFromCsvContents(String)
   */
  public static @NotNull Quiz fromCsvContent(@NotNull String contents) throws IOException {
    // Generate the question groups from the file
    HashMap<Question.Difficulty, QuestionGroup> questionGroups =
        QuestionGroup.generateFromCsvContents(contents);
    return new Quiz(questionGroups);
  }

    /**
     * Get the quiz progress
     *
     * @return quiz progress
     */
    public QuizProgress getProgress() {
        return quizProgress;
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
            currentQuestion = loader.loadQuestion();
            quizProgress.incrementQuestionsAsked();
        }

        return currentQuestion;
    }

    /**
     * Submit an answer to the selected question
     *
     * @param answer answer to the question
     * @throws IllegalStateException if attempting to answer a question before loading a new question
     *                               using {@link Quiz#loadQuestion()}
     */
    public void submitAnswer(Question.Choice answer) throws IllegalStateException {
        if (currentQuestion == null) {
            // TODO - Exception Message (correct exception type just needs nice message)
            throw new IllegalStateException();
        }

        // Grade the selected question
        quizProgress.recordAnsweredQuestion(currentQuestion.grade(answer));

        // Update question loader
        updateLoader();

        // Reset current question
        currentQuestion = null;
    }

    /**
     * Update the question loader
     *
     * @see QuestionLoader
     */
    private void updateLoader() {
        loader = loader.evaluateStrategy();
    }

    public boolean isQuizFinished() {
        if (quizProgress.numQuestionsAsked() <= 20) {
            return false;
        }
        return true;
    }

    public double scoreQuiz() {
        double score = (double) quizProgress.numQuestionsCorrect() / quizProgress.numQuestionsAsked();
        return score;
    }
}
