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
    updateStudentList();
  }

  private void updateStudentList() {
    studentListView
        .setItems(TeacherAppState.getStudentsByProgress(StudentState.Progress.COMPLETED));
  }

  private void handleClear() {
    // Remove all completed students
    TeacherAppState.getStudentsByProgress(StudentState.Progress.COMPLETED).clear();
    updateStudentList();
  }
}
