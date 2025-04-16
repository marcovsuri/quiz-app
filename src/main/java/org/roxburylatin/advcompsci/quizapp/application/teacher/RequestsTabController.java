package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class RequestsTabController {
  @FXML private ListView<Student> studentListView;

  @FXML private Button acceptButton;

  @FXML private Button denyButton;

  @FXML
  public void initialize() {
    // Initialize with empty list, will be set when appState is provided
    studentListView.setItems(FXCollections.observableArrayList());

    // Set up button actions
    acceptButton.setOnAction(event -> handleAccept());
    denyButton.setOnAction(event -> handleDeny());

    // Listen for updates from TeacherAppState
    AppState.needsUpdateProperty().addListener((obs, oldVal, newVal) -> updateStudentList());

    updateStudentList();
  }

  private void updateStudentList() {
    studentListView.setItems(
        AppState.getStudentsByProgress(Student.Progress.REQUESTED));
  }

  private void handleAccept() {
    Student selectedStudent = studentListView.getSelectionModel().getSelectedItem();
    if (selectedStudent != null) {
      selectedStudent.setProgress(Student.Progress.IN_PROGRESS);
      AppState.signalUpdate();
      System.out.println("Student Accepted");
    }
  }

  private void handleDeny() {
    Student selectedStudent = studentListView.getSelectionModel().getSelectedItem();
    if (selectedStudent != null) {
      // Remove the student from the list
      AppState.removeStudent(selectedStudent);
      System.out.println("Student Denied");
      AppState.signalUpdate();
    }
  }
}
