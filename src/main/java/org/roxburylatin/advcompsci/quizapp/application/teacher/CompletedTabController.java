package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class CompletedTabController {
  @FXML private ListView<Student> studentListView;

  @FXML private Button clearButton;

  @FXML
  public void initialize() {
    // Initialize with empty list, will be set when appState is provided
    studentListView.setItems(FXCollections.observableArrayList());

    // Set up clear button action
    clearButton.setOnAction(event -> handleClear());

    // Listen for updates from TeacherAppState
    AppState.needsUpdateProperty().addListener((obs, oldVal, newVal) -> updateStudentList());

    updateStudentList();
  }

  private void updateStudentList() {
    studentListView.setItems(
        AppState.getStudentsByProgress(Student.Progress.COMPLETED));
  }

  private void handleClear() {
    // We need to modify the clear functionality since we're using a filtered list
    // This will be handled by the TeacherAppState
    AppState.clearCompletedStudents();
    System.out.println("Cleared completed students");
    AppState.signalUpdate();
  }
}
