package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class RequestsTabController {
  @FXML
  private ListView<StudentState> studentListView;

  @FXML
  private Button acceptButton;

  @FXML
  private Button denyButton;

  @FXML
  public void initialize() {
    // Initialize with empty list, will be set when appState is provided
    studentListView.setItems(FXCollections.observableArrayList());

    // Set up button actions
    acceptButton.setOnAction(event -> handleAccept());
    denyButton.setOnAction(event -> handleDeny());

    // Listen for updates from TeacherAppState
    TeacherAppState.needsUpdateProperty().addListener((obs, oldVal, newVal) -> updateStudentList());

    updateStudentList();
  }

  private void updateStudentList() {
    studentListView
        .setItems(TeacherAppState.getStudentsByProgress(StudentState.Progress.REQUESTED));
  }

  private void handleAccept() {
    StudentState selectedStudent = studentListView.getSelectionModel().getSelectedItem();
    if (selectedStudent != null) {
      TeacherAppState.moveStudent(selectedStudent, StudentState.Progress.IN_PROGRESS);
    }
  }

  private void handleDeny() {
    StudentState selectedStudent = studentListView.getSelectionModel().getSelectedItem();
    if (selectedStudent != null) {
      // Remove the student from the list
      TeacherAppState.moveStudent(selectedStudent, StudentState.Progress.REQUESTED);
      // This will be filtered out by the FilteredList
    }
  }
}
