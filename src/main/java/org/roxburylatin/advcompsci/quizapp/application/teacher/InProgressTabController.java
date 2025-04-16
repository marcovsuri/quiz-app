package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class InProgressTabController {
  @FXML
  private ListView<Student> studentListView;

  @FXML
  public void initialize() {
    // Initialize with empty list, will be set when appState is provided
    studentListView.setItems(FXCollections.observableArrayList());

    // Listen for updates from TeacherAppState
    AppState.needsUpdateProperty().addListener((obs, oldVal, newVal) -> updateStudentList());

    updateStudentList();
  }

  private void updateStudentList() {
    studentListView
        .setItems(AppState.getStudentsByProgress(Student.Progress.IN_PROGRESS));
  }
}
