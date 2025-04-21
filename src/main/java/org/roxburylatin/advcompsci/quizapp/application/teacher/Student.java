package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.beans.property.*;

public class Student {
  private final StringProperty firstName = new SimpleStringProperty();
  private final StringProperty lastName = new SimpleStringProperty();
  private final ObjectProperty<Progress> progress = new SimpleObjectProperty<>();
  private final int chapterNum;

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

  public enum Progress {
    REQUESTED,
    IN_PROGRESS,
    COMPLETED
  }
}
