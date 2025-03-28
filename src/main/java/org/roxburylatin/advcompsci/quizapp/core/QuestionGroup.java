package org.roxburylatin.advcompsci.quizapp.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import org.jetbrains.annotations.NotNull;

/**
 * A group of questions by difficulty for {@link Quiz}
 *
 * @see Question
 */
public class QuestionGroup {
  /** Questions in the group */
  private final ArrayList<Question> questions;

  /** Difficulty of the group */
  private final Question.Difficulty difficulty;

  /**
   * Create a new question group
   *
   * @param questions questions in the group
   * @param difficulty difficulty of the questions in the group
   * @throws IllegalArgumentException if questions provided do not match expected difficulty
   */
  public QuestionGroup(
      @NotNull ArrayList<Question> questions, @NotNull Question.Difficulty difficulty)
      throws IllegalArgumentException {
    // Ensure that all the questions match the appropriate difficulty
    questions.forEach(
        q -> {
          if (!q.getDifficulty().equals(difficulty)) {
            throw new IllegalArgumentException("Questions are not the appropriate difficulty");
          }
        });

    this.questions = questions;
    this.difficulty = difficulty;
  }

  /**
   * Generate question groups from a questions file
   *
   * @param file file from which to generate groups
   * @return questions organized into groups, based on difficulty
   * @throws FileNotFoundException if the file cannot be read
   * @throws IllegalArgumentException if the file is not formatted correctly for parsing
   */
  public static @NotNull HashMap<Question.Difficulty, QuestionGroup> generateFromFile(
      @NotNull File file) throws FileNotFoundException, IllegalArgumentException {
    ArrayList<Question> easyQuestions = new ArrayList<>();
    ArrayList<Question> mediumQuestions = new ArrayList<>();
    ArrayList<Question> hardQuestions = new ArrayList<>();

    try (Scanner in = new Scanner(file)) {
      while (in.hasNextLine()) {
        // Read question properties in file
        String title = in.nextLine().trim();

        HashMap<Question.Choice, String> choices = new HashMap<>();
        choices.put(Question.Choice.A, in.nextLine().trim());
        choices.put(Question.Choice.B, in.nextLine().trim());
        choices.put(Question.Choice.C, in.nextLine().trim());
        choices.put(Question.Choice.D, in.nextLine().trim());

        // TODO - Handle parsing errors
        String crctChoice = in.nextLine().substring(4).trim().toUpperCase();
        Question.Choice correctChoice = Question.Choice.valueOf(crctChoice);
        String dif = in.nextLine().substring(11).trim().toUpperCase();
        Question.Difficulty difficulty = Question.Difficulty.valueOf(dif);

        Question question = new Question(title, choices, correctChoice, difficulty);

        // Sort question correctly
        switch (difficulty) {
          case EASY:
            easyQuestions.add(question);
            break;
          case MEDIUM:
            mediumQuestions.add(question);
            break;
          case HARD:
            hardQuestions.add(question);
            break;
        }

        in.nextLine();
      }
    }

    // Assign groups to HashMap
    HashMap<Question.Difficulty, QuestionGroup> groups = new HashMap<>();

    groups.put(
        Question.Difficulty.EASY, new QuestionGroup(easyQuestions, Question.Difficulty.EASY));
    groups.put(
        Question.Difficulty.MEDIUM, new QuestionGroup(mediumQuestions, Question.Difficulty.MEDIUM));
    groups.put(
        Question.Difficulty.HARD, new QuestionGroup(hardQuestions, Question.Difficulty.HARD));

    return groups;
  }

  /**
   * Get and remove a random question from the question group
   *
   * @return removed question from the group
   */
  public Question getAndRemoveRandomQuestion() {
    return questions.isEmpty() ? null : questions.remove(new Random().nextInt(questions.size()));
  }
}
