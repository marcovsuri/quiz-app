package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class InProgressTabController {
  @FXML
  private ListView<StudentState> studentListView;

  @FXML
  public void initialize() {
    // Initialize with empty list, will be set when appState is provided
    studentListView.setItems(FXCollections.observableArrayList());

    // Listen for updates from TeacherAppState
    TeacherAppState.needsUpdateProperty().addListener((obs, oldVal, newVal) -> updateStudentList());

    updateStudentList();
  }

  private void updateStudentList() {
    studentListView
        .setItems(TeacherAppState.getStudentsByProgress(StudentState.Progress.IN_PROGRESS));
  }
}
