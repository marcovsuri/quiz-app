package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class CompletedTabController {
  @FXML
  private ListView<StudentState> studentListView;

  @FXML
  private Button clearButton;

  @FXML
  public void initialize() {
    // Initialize with empty list, will be set when appState is provided
    studentListView.setItems(FXCollections.observableArrayList());

    // Set up clear button action
    clearButton.setOnAction(event -> handleClear());

    // Listen for updates from TeacherAppState
    TeacherAppState.needsUpdateProperty().addListener((obs, oldVal, newVal) -> updateStudentList());

    updateStudentList();
  }

  private void updateStudentList() {
    studentListView
        .setItems(TeacherAppState.getStudentsByProgress(StudentState.Progress.COMPLETED));
  }

  private void handleClear() {
    // We need to modify the clear functionality since we're using a filtered list
    // This will be handled by the TeacherAppState
    TeacherAppState.clearCompletedStudents();
  }
}
