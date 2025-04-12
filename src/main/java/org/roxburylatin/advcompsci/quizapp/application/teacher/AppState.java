package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.roxburylatin.advcompsci.quizapp.application.teacher.Student.Progress;

public class AppState {
  private static final ObservableList<Student> allStudents =
      FXCollections.observableArrayList();
  private static final BooleanProperty needsUpdate = new SimpleBooleanProperty(false);

  static {
    // TODO - remove (TESTING ONLY)
    Student student1 = new Student("Michael", "DiLallo");
    student1.setProgress(Progress.COMPLETED);
    allStudents.add(student1);

    Student student2 = new Student("Avish", "Kumar");
    student2.setProgress(Progress.IN_PROGRESS);
    allStudents.add(student2);

    Student student3 = new Student("Marco", "Suri");
    student3.setProgress(Progress.REQUESTED);
    allStudents.add(student3);
  }

  public static ObservableList<Student> getStudentsByProgress(Student.Progress progress) {
    FilteredList<Student> filteredList =
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

  public static void addStudent(Student student) {
    allStudents.add(student);
  }

  public static void removeStudent(Student student) {
    allStudents.remove(student);
  }

  public static void clearCompletedStudents() {
    // Remove all students with COMPLETED progress
    allStudents.removeIf(student -> student.getProgress() == Progress.COMPLETED);
  }
}
