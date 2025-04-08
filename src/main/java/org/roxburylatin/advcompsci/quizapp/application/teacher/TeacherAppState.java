package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TeacherAppState {
  public final ObservableList<StudentState> students = FXCollections.observableArrayList();

  // TODO - remove (TESTING ONLY)
  {
    students.add(new StudentState("Michael", "DiLallo"));
    students.add(new StudentState("Avish", "Kumar"));
    students.add(new StudentState("Marco", "Suri"));

    students.getFirst().setProgress(StudentState.Progress.COMPLETED);
    students.get(1).setProgress(StudentState.Progress.IN_PROGRESS);
    students.get(2).setProgress(StudentState.Progress.REQUESTED);
  }
}
