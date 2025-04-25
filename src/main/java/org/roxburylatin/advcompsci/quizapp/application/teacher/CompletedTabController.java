package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 * The CompletedTabController class manages the UI for the completed students tab in the teacher
 * view of the quiz application. It handles the display of students who have completed their quizzes
 * and provides functionality to clear the list of completed students.
 */
public class CompletedTabController {
  @FXML private ListView<Student> studentListView;

  @FXML private Button clearButton;

  /**
   * Initializes the CompletedTabController. This method sets up the student list view and
   * configures the clear button action. It also listens for updates from the AppState to refresh
   * the student list when necessary.
   */
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

  /**
   * Updates the student list view with the students who have completed their quizzes. This method
   * retrieves the list of completed students from the AppState and updates the UI accordingly. It
   * is called whenever the AppState signals an update.
   *
   * @see AppState#getStudentsByProgress(Student.Progress)
   */
  private void updateStudentList() {
    studentListView.setItems(AppState.getStudentsByProgress(Student.Progress.COMPLETED));
  }

  /**
   * Handles the clear button action. This method clears the list of completed students and updates
   * the UI accordingly. Note: The clear functionality is modified to work with the filtered list in
   * {@code AppState}.
   *
   * @see AppState#clearCompletedStudents()
   * @see Student.Progress#COMPLETED
   */
  private void handleClear() {
    // We need to modify the clear functionality since we're using a filtered list
    // This will be handled by the TeacherAppState
    AppState.clearCompletedStudents();
    System.out.println("Cleared completed students");
    AppState.signalUpdate();
  }
}
