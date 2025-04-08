package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class InProgressTabController {
  @FXML private ListView<StudentState> studentListView;

  private TeacherAppState appState;

  @FXML
  public void initialize() {
    // Initialize with empty list, will be set when appState is provided
    studentListView.setItems(FXCollections.observableArrayList());
  }

  public void setAppState(TeacherAppState appState) {
    this.appState = appState;
    updateStudentList();
  }

  private void updateStudentList() {
    if (appState != null) {
      FilteredList<StudentState> inProgressStudents =
          new FilteredList<>(
              appState.students,
              student -> student.getProgress() == StudentState.Progress.IN_PROGRESS);
      studentListView.setItems(inProgressStudents);
    }
  }
}
