package org.roxburylatin.advcompsci.quizapp.core;

import java.util.HashMap;

/**
 * A Quiz Question
 *
 * @see Quiz
 */
public class Question {
  /** Question title */
  private final String title;

  /** Question choices * */
  private final HashMap<Choice, String> choices;

  /** Question's correct choice */
  private final Choice correctChoice;

  /** Question's difficulty */
  private final Difficulty difficulty;

  /**
   * Create a new quiz question
   *
   * @param title question title
   * @param choices question choices
   * @param correctChoice correct choice for the question
   * @param difficulty question difficulty
   */
  public Question(
      String title, HashMap<Choice, String> choices, Choice correctChoice, Difficulty difficulty) {
    this.title = title;
    this.choices = choices;
    this.correctChoice = correctChoice;
    this.difficulty = difficulty;
  }

  /**
   * Get the question difficulty
   *
   * @return question difficulty
   */
  public Difficulty getDifficulty() {
    return difficulty;
  }

  /**
   * Grade the question based on the answer
   *
   * @param answerChoice answer choice
   * @return whether the answer choice is correct
   */
  public boolean grade(Choice answerChoice) {
    return answerChoice == correctChoice;
  }

  /**
   * Get the title of the question
   *
   * @return title of the question
   */
  public String getTitle() {
    return title;
  }

  /**
   * Get the text corresponding to a specific choice
   *
   * @param choice choice
   * @return text corresponding to choice
   */
  public String getChoice(Choice choice) {
    return choices.get(choice);
  }

  /**
   * This method gets the correct choice for a given question.
   * @return The correct choice (A,B,C,D) for a specific question.
   */
  public Choice getCorrectChoice () {
    return correctChoice;
  }

  /** Choices for a quiz question */
  public enum Choice {
    A(1),
    B(2),
    C(3),
    D(4);

    private final int value;

    Choice(int value) {
      this.value = value;
    }

    /**
     * Turn an int into a Choice (1=A;2=B;3=C;4=D)
     *
     * @param value int
     * @return corresponding choice
     * @throws IllegalArgumentException if the int does not correspond to a choice
     */
    public static Choice fromInt(int value) throws IllegalArgumentException {
      for (Choice choice : Choice.values()) {
        if (choice.value == value) {
          return choice;
        }
      }
      throw new IllegalArgumentException("No enum constant with value " + value);
    }
  }

  /** Quiz question difficulties */
  public enum Difficulty {
    EASY,
    MEDIUM,
    HARD
  }
}