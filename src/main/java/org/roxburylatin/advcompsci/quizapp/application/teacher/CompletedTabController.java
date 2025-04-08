package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class CompletedTabController {
  @FXML private ListView<StudentState> studentListView;

  @FXML private Button clearButton;

  private TeacherAppState appState;

  @FXML
  public void initialize() {
    // Initialize with empty list, will be set when appState is provided
    studentListView.setItems(FXCollections.observableArrayList());

    // Set up clear button action
    clearButton.setOnAction(event -> handleClear());
  }

  public void setAppState(TeacherAppState appState) {
    this.appState = appState;
    updateStudentList();
  }

  private void updateStudentList() {
    if (appState != null) {
      FilteredList<StudentState> completedStudents =
          new FilteredList<>(
              appState.students,
              student -> student.getProgress() == StudentState.Progress.COMPLETED);
      studentListView.setItems(completedStudents);
    }
  }

  private void handleClear() {
    if (appState != null) {
      // Remove all completed students
      appState.students.removeIf(
          student -> student.getProgress() == StudentState.Progress.COMPLETED);
      updateStudentList();
    }
  }
}
