package org.roxburylatin.advcompsci.quizapp.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import com.opencsv.exceptions.CsvValidationException;
import org.jetbrains.annotations.NotNull;
import com.opencsv.*;

import static org.roxburylatin.advcompsci.quizapp.core.Question.Difficulty.*;

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
      @NotNull File file) throws IOException, IllegalArgumentException, CsvValidationException {
    ArrayList<Question> easyQuestions = new ArrayList<>();
    ArrayList<Question> mediumQuestions = new ArrayList<>();
    ArrayList<Question> hardQuestions = new ArrayList<>();

    CSVReader reader = new CSVReaderBuilder(new FileReader(file)).build();
    String [] nextLine;
    reader.readNext();
    while ((nextLine = reader.readNext()) != null) {
      Question.Difficulty difficulty;
      if (nextLine[1].equals("1")){
        if (nextLine[2].equals("1")){
          difficulty = EASY;
        } else if (nextLine[2].equals("2")) {
          difficulty = MEDIUM;
        }
        else{
          difficulty = HARD;
        }
        String title = nextLine[3];
        HashMap<Question.Choice, String> choices = new HashMap<>();
        choices.put(Question.Choice.A, nextLine[4]);
        choices.put(Question.Choice.B, nextLine[5]);
        choices.put(Question.Choice.C, nextLine[6]);
        choices.put(Question.Choice.D, nextLine[7]);

        int crctChoice = Integer.parseInt(nextLine[8]) + 3;
        Question.Choice correctChoice = Question.Choice.valueOf(nextLine[crctChoice]);

        Question question = new Question(title, choices, correctChoice, difficulty);

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

    groups.put(
        EASY, new QuestionGroup(easyQuestions, EASY));
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
