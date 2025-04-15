package org.roxburylatin.advcompsci.quizapp.core;

import static org.roxburylatin.advcompsci.quizapp.core.Question.Difficulty.*;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import java.io.IOException;
import java.util.*;
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
   * Generates a mapping of question difficulties to their respective QuestionGroup objects from the
   * contents of a CSV file.
   *
   * <p>The CSV contents should be provided as a string, where each row represents a question and
   * contains the following columns:
   *
   * <ul>
   *   <li>Column 1: A flag indicating whether the row represents a question (value "1").
   *   <li>Column 2: The difficulty level of the question (1 for EASY, 2 for MEDIUM, 3 for HARD).
   *   <li>Column 3: The title of the question.
   *   <li>Columns 4-7: The text for the four answer choices (A, B, C, D).
   *   <li>Column 8: The integer representation of the correct answer choice (1 for A, 2 for B,
   *       etc.).
   * </ul>
   *
   * <p>The method parses the CSV contents, creates Question objects for each valid row, and groups
   * them by difficulty level into QuestionGroup objects.
   *
   * @param contents The CSV contents as a string.
   * @return A HashMap mapping each Question.Difficulty to its corresponding QuestionGroup.
   * @throws IOException If an error occurs while reading the CSV contents.
   */
  public static @NotNull HashMap<Question.Difficulty, QuestionGroup> generateFromCsvContents(
      @NotNull String contents) throws IOException {
    ArrayList<Question> easyQuestions = new ArrayList<>();
    ArrayList<Question> mediumQuestions = new ArrayList<>();
    ArrayList<Question> hardQuestions = new ArrayList<>();

    CsvMapper mapper = new CsvMapper();

    try (MappingIterator<List<String>> it =
        mapper
            .readerForListOf(String.class)
            .with(CsvParser.Feature.WRAP_AS_ARRAY)
            .readValues(contents)) {

      while (it.hasNextValue()) {
        // Read row
        List<String> row = it.nextValue();

        // Skip if bad question
        if (!row.get(1).equals("1")) continue;

        // Determine difficulty
        Question.Difficulty difficulty;
        if (row.get(2).equals("1")) {
          difficulty = EASY;
        } else if (row.get(2).equals("2")) {
          difficulty = MEDIUM;
        } else {
          difficulty = HARD;
        }

        // Get question title
        String title = row.get(3);

        // Get question choices
        HashMap<Question.Choice, String> choices = new HashMap<>();
        choices.put(Question.Choice.A, row.get(4));
        choices.put(Question.Choice.B, row.get(5));
        choices.put(Question.Choice.C, row.get(6));
        choices.put(Question.Choice.D, row.get(7));

        // Get the correct choice
        int crctChoice = Integer.parseInt(row.get(8));
        Question.Choice correctChoice = Question.Choice.fromInt(crctChoice);

        Question question = new Question(title, choices, correctChoice, difficulty);

        // Assign the question to the right array list
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
      }
    }

    // Assign groups to HashMap
    HashMap<Question.Difficulty, QuestionGroup> groups = new HashMap<>();

    groups.put(EASY, new QuestionGroup(easyQuestions, EASY));
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
