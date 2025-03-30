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
  private final @NotNull QuizProgress quizProgress;
  private @NotNull Question.Difficulty currentDifficulty = Question.Difficulty.EASY;
  private int numQuestionsInDifficulty = 0;

  /**
   * Create a consecutive question loader
   *
   * @param quizProgress progress of the quiz
   * @param questionGroups question groups
   */
  public ConsecutiveLoader(@NotNull QuizProgress quizProgress, @NotNull HashMap<Question.Difficulty, QuestionGroup> questionGroups) {
    this.quizProgress = quizProgress;
    this.questionGroups = questionGroups;
  }

  @Override
  public Question loadQuestion() {
    // Increase difficulty every three questions
    if (numQuestionsInDifficulty != 0 && numQuestionsInDifficulty % 3 == 0) {
      currentDifficulty = QuestionLoader.increaseDifficulty(currentDifficulty);
    }

    numQuestionsInDifficulty++;

    return questionGroups.get(currentDifficulty).getAndRemoveRandomQuestion();
  }

  @Override
  public @NotNull QuestionLoader evaluateStrategy() {
    if (quizProgress.numQuestionsAsked() == 3 && quizProgress.numQuestionsCorrect() <= 2) {
      // Got at least 1 wrong in the easy difficulty => change strategy to AdaptiveLoader
      return new AdaptiveLoader(quizProgress, Question.Difficulty.EASY, questionGroups);
    }

    if (quizProgress.numQuestionsAsked() == 6 && quizProgress.numQuestionsCorrect() <= 5) {
      // Got at least 1 wrong in the medium difficulty => change strategy to AdaptiveLoader
      return new AdaptiveLoader(quizProgress, Question.Difficulty.MEDIUM, questionGroups);
    }

    if (quizProgress.numQuestionsCorrect() >= 9) {
      // Has already answered nine questions correctly => change strategy to AdaptiveLoader
      return new AdaptiveLoader(quizProgress, Question.Difficulty.HARD, questionGroups);
    }

    // Otherwise => keep strategy as ConsecutiveLoader
    return this;
  }
}
