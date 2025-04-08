package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.beans.property.*;

public class StudentState {
  private final StringProperty firstName = new SimpleStringProperty();
  private final StringProperty lastName = new SimpleStringProperty();
  private final ObjectProperty<Progress> progress = new SimpleObjectProperty<>();

  public StudentState(String firstName, String lastName) {
    this.firstName.set(firstName);
    this.lastName.set(lastName);
    this.progress.set(Progress.REQUESTED);
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
    return firstName.get() + " " + lastName.get();
  }

  public enum Progress {
    REQUESTED,
    IN_PROGRESS,
    COMPLETED
  }
}
