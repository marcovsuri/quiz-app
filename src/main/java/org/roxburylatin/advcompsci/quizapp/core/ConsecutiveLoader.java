package org.roxburylatin.advcompsci.quizapp.core;

import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

/**
 * A Consecutive Question Loader. Increases the difficulty of the question every three questions.
 * Concrete implementation as strategy for {@link QuestionLoader}.
 *
 * @see QuestionLoader
 */
public class ConsecutiveLoader implements QuestionLoader {
  private final @NotNull HashMap<Question.Difficulty, QuestionGroup> questionGroups;
  private @NotNull Question.Difficulty currentDifficulty = Question.Difficulty.EASY;
  private int numQuestionsInDifficulty = 0;

  /**
   * Create a consecutive question loader
   *
   * @param questionGroups question groups
   */
  public ConsecutiveLoader(@NotNull HashMap<Question.Difficulty, QuestionGroup> questionGroups) {
    this.questionGroups = questionGroups;
  }

  @Override
  public Question loadQuestion(boolean _lastAnswerCorrect) {
    // Increase difficulty every three questions
    if (numQuestionsInDifficulty != 0 && numQuestionsInDifficulty % 3 == 0) {
      currentDifficulty = QuestionLoader.increaseDifficulty(currentDifficulty);
    }

    numQuestionsInDifficulty++;

    return questionGroups.get(currentDifficulty).getAndRemoveRandomQuestion();
  }

  @Override
  public @NotNull QuestionLoader evaluateStrategy(
      int numQuestionsAsked,
      int numQuestionsCorrect,
      @NotNull HashMap<Question.Difficulty, QuestionGroup> questionGroups) {
    if (numQuestionsAsked == 3 && numQuestionsCorrect <= 2) {
      // Got at least 1 wrong in the easy difficulty => change strategy to AdaptiveLoader
      return new AdaptiveLoader(Question.Difficulty.EASY, questionGroups);
    }

    if (numQuestionsAsked == 6 && numQuestionsCorrect <= 5) {
      // Got at least 1 wrong in the medium difficulty => change strategy to AdaptiveLoader
      return new AdaptiveLoader(Question.Difficulty.MEDIUM, questionGroups);
    }

    if (numQuestionsCorrect >= 9) {
      // Has already answered nine questions correctly => change strategy to AdaptiveLoader
      return new AdaptiveLoader(Question.Difficulty.HARD, questionGroups);
    }

    // Otherwise => keep strategy as ConsecutiveLoader
    return this;
  }
}
