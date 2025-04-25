package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

/**
 * The InProgressTabController class manages the UI for the in-progress students tab in the teacher
 * view of the quiz application. It handles the display of students who are currently taking their
 * quizzes and updates the list view accordingly.
 */
public class InProgressTabController {
  @FXML private ListView<Student> studentListView;

  /**
   * Initializes the InProgressTabController. This method sets up the student list view and listens
   * for updates from the AppState to refresh the student list when necessary.
   */
  @FXML
  public void initialize() {
    // Initialize with empty list, will be set when appState is provided
    studentListView.setItems(FXCollections.observableArrayList());

    // Listen for updates from TeacherAppState
    AppState.needsUpdateProperty().addListener((obs, oldVal, newVal) -> updateStudentList());

    updateStudentList();
  }

  /**
   * Updates the student list view with the students who are currently in progress with their
   * quizzes. This method retrieves the list of in-progress students from the AppState and updates
   * the UI accordingly. It is called whenever the AppState signals an update.
   *
   * @see AppState#getStudentsByProgress(Student.Progress)
   * @see Student.Progress#IN_PROGRESS
   */
  private void updateStudentList() {
    studentListView.setItems(AppState.getStudentsByProgress(Student.Progress.IN_PROGRESS));
  }
}
