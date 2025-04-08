package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.roxburylatin.advcompsci.quizapp.application.teacher.StudentState.Progress;

public class TeacherAppState {
  private static final ObservableList<StudentState> allStudents =
      FXCollections.observableArrayList();
  private static final BooleanProperty needsUpdate = new SimpleBooleanProperty(false);

  static {
    // TODO - remove (TESTING ONLY)
    StudentState student1 = new StudentState("Michael", "DiLallo");
    student1.setProgress(Progress.COMPLETED);
    allStudents.add(student1);

    StudentState student2 = new StudentState("Avish", "Kumar");
    student2.setProgress(Progress.IN_PROGRESS);
    allStudents.add(student2);

    StudentState student3 = new StudentState("Marco", "Suri");
    student3.setProgress(Progress.REQUESTED);
    allStudents.add(student3);
  }

  public static ObservableList<StudentState> getStudentsByProgress(StudentState.Progress progress) {
    FilteredList<StudentState> filteredList =
        new FilteredList<>(allStudents, student -> student.getProgress() == progress);
    return filteredList;
  }

  public static BooleanProperty needsUpdateProperty() {
    return needsUpdate;
  }

  public static void signalUpdate() {
    needsUpdate.set(!needsUpdate.get());
    System.out.println("Updated UI");
  }

  public static void addStudent(StudentState student) {
    allStudents.add(student);
  }

  public static void removeStudent(StudentState student) {
    allStudents.remove(student);
  }

  public static void clearCompletedStudents() {
    // Remove all students with COMPLETED progress
    allStudents.removeIf(student -> student.getProgress() == Progress.COMPLETED);
  }
}
