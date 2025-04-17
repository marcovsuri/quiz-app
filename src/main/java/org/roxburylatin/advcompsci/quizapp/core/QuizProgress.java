package org.roxburylatin.advcompsci.quizapp.core;

/**
 * State which stores the progress of a quiz
 *
 * @see Quiz
 */
public class QuizProgress {
  private int numQuestionsAnswered = 0;
  private int numQuestionsAsked = 0;
  private int numQuestionsCorrect = 0;
  private boolean isLastAnswerCorrect = false;

  /**
   * Get the number of questions asked in the quiz
   *
   * @return number of questions asked so far
   */
  public int numQuestionsAsked() {
    return numQuestionsAsked;
  }

  /**
   * Get the number of questions answered correctly
   *
   * @return get the number of questions answered correctly in the quiz so far
   */
  public int numQuestionsCorrect() {
    return numQuestionsCorrect;
  }

  /**
   * Get the number of questions answered
   *
   * @return the number of questions answered so far in the quiz
   */
  public int numQuestionsAnswered() {
    return numQuestionsAnswered;
  }

  /**
   * Get whether the last answer was correct
   *
   * @return whether the answer to the last question was correct
   */
  public boolean isLastAnswerCorrect() {
    return isLastAnswerCorrect;
  }

  /** Increase the number of questions asked in the quiz */
  public void incrementQuestionsAsked() {
    numQuestionsAsked++;
  }

  /**
   * Record an answered question for the quiz
   *
   * @param isAnswerCorrect whether the answer was correct
   */
  public void recordAnsweredQuestion(boolean isAnswerCorrect) {
    numQuestionsAnswered++;
    isLastAnswerCorrect = isAnswerCorrect;
    if (isLastAnswerCorrect) {
      numQuestionsCorrect++;
    }
  }

  public boolean isQuizFinished() {
    return numQuestionsAnswered == 20;
  }
}
