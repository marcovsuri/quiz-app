package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class InProgressTabController {
  @FXML
  private ListView<StudentState> studentListView;

  @FXML
  public void initialize() {
    // Initialize with empty list, will be set when appState is provided
    studentListView.setItems(FXCollections.observableArrayList());
    updateStudentList();
  }

  private void updateStudentList() {
    FilteredList<StudentState> inProgressStudents = new FilteredList<>(TeacherAppState.students,
        student -> student.getProgress() == StudentState.Progress.IN_PROGRESS);
    studentListView.setItems(inProgressStudents);
  }
}
