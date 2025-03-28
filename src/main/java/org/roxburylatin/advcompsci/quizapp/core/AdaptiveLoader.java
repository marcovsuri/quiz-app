package org.roxburylatin.advcompsci.quizapp.core;

import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

/**
 * An Adaptive Question Loader. Determines the difficulty of the questions based on previous
 * results. Concrete implementation as strategy for {@link QuestionLoader}.
 *
 * @see QuestionLoader
 */
public class AdaptiveLoader implements QuestionLoader {
  private final @NotNull HashMap<Question.Difficulty, QuestionGroup> questionGroups;
  private @NotNull Question.Difficulty currentDifficulty;

  /**
   * Create an adaptive question loader
   *
   * @param startingDifficulty starting difficulty for the adaptive loader
   * @param questionGroups question groups for the quiz
   */
  public AdaptiveLoader(
      @NotNull Question.Difficulty startingDifficulty,
      @NotNull HashMap<Question.Difficulty, QuestionGroup> questionGroups) {
    currentDifficulty = startingDifficulty;
    this.questionGroups = questionGroups;
  }

  @Override
  public Question loadQuestion(boolean lastAnswerCorrect) {
    // Change the current difficulty based on the last answer result
    currentDifficulty =
        lastAnswerCorrect
            ? QuestionLoader.increaseDifficulty(currentDifficulty)
            : QuestionLoader.decreaseDifficulty(currentDifficulty);

    return questionGroups.get(currentDifficulty).getAndRemoveRandomQuestion();
  }

  @Override
  public @NotNull QuestionLoader evaluateStrategy(
      int _numQuestionsAsked,
      int _numQuestionsCorrect,
      @NotNull HashMap<Question.Difficulty, QuestionGroup> _questionGroups) {
    // Never change strategy once using AdaptiveLoader => always return self
    return this;
  }
}
