package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.HashMap;
import java.util.Map;
import org.roxburylatin.advcompsci.quizapp.application.teacher.StudentState.Progress;

public class TeacherAppState {
  private static final Map<StudentState.Progress, ObservableList<StudentState>> studentLists =
      new HashMap<>();

  static {
    // Initialize lists for each progress state
    for (StudentState.Progress progress : StudentState.Progress.values()) {
      studentLists.put(progress, FXCollections.observableArrayList());
    }

    // TODO - remove (TESTING ONLY)
    StudentState student1 = new StudentState("Michael", "DiLallo");
    student1.setProgress(Progress.COMPLETED);
    studentLists.get(Progress.COMPLETED).add(student1);
    StudentState student2 = new StudentState("Avish", "Kumar");
    student2.setProgress(Progress.IN_PROGRESS);
    studentLists.get(Progress.IN_PROGRESS).add(student2);
    StudentState student3 = new StudentState("Marco", "Suri");
    student3.setProgress(Progress.REQUESTED);
    studentLists.get(Progress.REQUESTED).add(student3);
  }

  public static ObservableList<StudentState> getStudentsByProgress(StudentState.Progress progress) {
    return studentLists.get(progress);
  }

  public static void moveStudent(StudentState student, StudentState.Progress newProgress) {
    // Remove from current list
    for (ObservableList<StudentState> list : studentLists.values()) {
      list.remove(student);
    }

    // Add to new list and update student's progress
    student.setProgress(newProgress);
    studentLists.get(newProgress).add(student);
  }
}
