package org.roxburylatin.advcompsci.quizapp.core;

import org.jetbrains.annotations.NotNull;

/**
 * A Question Loader for {@link Quiz}. Question Loaders follow the strategy pattern. This is the
 * overarching interface by which concrete classes can be implemented for different strategies.
 *
 * @see AdaptiveLoader
 * @see ConsecutiveLoader
 */
public interface QuestionLoader {
  /**
   * Increase the difficulty level
   *
   * @param difficulty current difficulty
   * @return next difficulty level
   */
  static @NotNull Question.Difficulty increaseDifficulty(@NotNull Question.Difficulty difficulty) {
    switch (difficulty) {
      case EASY -> {
        return Question.Difficulty.MEDIUM;
      }
      case MEDIUM, HARD -> {
        return Question.Difficulty.HARD;
      }
      // TODO - Make this nicer => would prefer not to have this throw clause
      default -> throw new UnknownError();
    }
  }

  /**
   * Decrease the difficulty level
   *
   * @param difficulty current difficulty
   * @return previous difficulty level
   */
  static @NotNull Question.Difficulty decreaseDifficulty(@NotNull Question.Difficulty difficulty) {
    switch (difficulty) {
      case EASY, MEDIUM -> {
        return Question.Difficulty.EASY;
      }
      case HARD -> {
        return Question.Difficulty.MEDIUM;
      }
      // TODO - Make this nicer => would prefer not to have this throw clause
      default -> throw new UnknownError();
    }
  }

  /**
   * Load a question based on the specific strategy implemented
   *
   * @return next question
   */
  Question loadQuestion();

  /**
   * Evaluate and return the correct strategy based on the current strategy's criteria
   *
   * @return correct strategy to use based on the given parameters
   */
  @NotNull
  QuestionLoader evaluateStrategy();
}
