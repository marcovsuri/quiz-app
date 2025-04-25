package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.beans.property.*;

/**
 * The Student class represents a student in the quiz application. It contains properties for the
 * student's first name, last name, progress status, and chapter number. This class is used to
 * manage and display student information in the teacher's view of the application.
 */
public class Student {
  private final StringProperty firstName = new SimpleStringProperty();
  private final StringProperty lastName = new SimpleStringProperty();
  private final ObjectProperty<Progress> progress = new SimpleObjectProperty<>();
  private final int chapterNum;

  /**
   * Constructor for the Student class. Initializes the student's first name, last name, and
   * progress status. The chapter number is set to the provided value.
   *
   * @param firstName student's first name
   * @param lastName student's last name
   * @param chapterNum chapter number associated with the student
   */
  public Student(String firstName, String lastName, int chapterNum) {
    this.firstName.set(firstName);
    this.lastName.set(lastName);
    this.progress.set(Progress.REQUESTED);
    this.chapterNum = chapterNum;
  }

  public Progress getProgress() {
    return progress.get();
  }

  public void setProgress(Progress progress) {
    this.progress.set(progress);
  }

  public String getFirstName() {
    return firstName.get();
  }

  public String getLastName() {
    return lastName.get();
  }

  @Override
  public String toString() {
    return firstName.get() + " " + lastName.get() + " (" + chapterNum + ")";
  }

  /**
   * The Progress enum represents the different states of a student's progress in the quiz
   * application. It includes three states: {@code REQUESTED}, {@code IN_PROGRESS}, and {@code
   * COMPLETED}.
   */
  public enum Progress {
    REQUESTED,
    IN_PROGRESS,
    COMPLETED
  }
}
