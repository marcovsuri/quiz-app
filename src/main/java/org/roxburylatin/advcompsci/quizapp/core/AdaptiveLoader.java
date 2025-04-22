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
  private final QuizProgress quizProgress;
  private @NotNull Question.Difficulty currentDifficulty;

  /**
   * Create an adaptive question loader CHANGE
   *
   * @param quizProgress progress of the quiz
   * @param startingDifficulty starting difficulty for the adaptive loader
   * @param questionGroups question groups for the quiz
   */
  public AdaptiveLoader(
      @NotNull QuizProgress quizProgress,
      @NotNull Question.Difficulty startingDifficulty,
      @NotNull HashMap<Question.Difficulty, QuestionGroup> questionGroups) {
    this.quizProgress = quizProgress;
    currentDifficulty = startingDifficulty;
    this.questionGroups = questionGroups;
  }

  @Override
  public Question loadQuestion() {
    // Change the current difficulty based on the last answer result
    currentDifficulty =
        quizProgress.isLastAnswerCorrect()
            ? QuestionLoader.increaseDifficulty(currentDifficulty)
            : QuestionLoader.decreaseDifficulty(currentDifficulty);

    Question q = questionGroups.get(currentDifficulty).getAndRemoveRandomQuestion();
    if (q == null) {
      if (currentDifficulty == Question.Difficulty.EASY) {
        q = questionGroups.get(Question.Difficulty.MEDIUM).getAndRemoveRandomQuestion();
        if (q == null) {
          q = questionGroups.get(Question.Difficulty.HARD).getAndRemoveRandomQuestion();
        }
      } else if (currentDifficulty == Question.Difficulty.MEDIUM) {
        q = questionGroups.get(Question.Difficulty.EASY).getAndRemoveRandomQuestion();
        if (q == null) {
          q = questionGroups.get(Question.Difficulty.HARD).getAndRemoveRandomQuestion();
        }
      } else {
        q = questionGroups.get(Question.Difficulty.MEDIUM).getAndRemoveRandomQuestion();
        if (q == null) {
          q = questionGroups.get(Question.Difficulty.EASY).getAndRemoveRandomQuestion();
        }
      }
    }

    return q;
  }

  @Override
  public @NotNull QuestionLoader evaluateStrategy() {
    // Never change strategy once using AdaptiveLoader => always return self
    return this;
  }
}
