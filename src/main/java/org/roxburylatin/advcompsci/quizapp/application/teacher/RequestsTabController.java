package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 * The RequestsTabController class manages the UI for the requests tab in the teacher view of the
 * quiz application. It handles the display of students who have requested to take a quiz and
 * provides functionality to accept or deny these requests.
 *
 * <p>Currently, the class is never used as the ability to accept or deny requests is not yet
 * implemented by the {@link Server} class.
 */
public class RequestsTabController {
  @FXML private ListView<Student> studentListView;

  @FXML private Button acceptButton;

  @FXML private Button denyButton;

  /**
   * Initializes the RequestsTabController. This method sets up the student list view and listens
   * for updates from the AppState to refresh the student list when necessary. It also configures
   * the actions for the accept and deny buttons.
   */
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

  /**
   * Updates the student list view with the students who have requested to take a quiz. This method
   * retrieves the list of requested students from the AppState and updates the UI accordingly. It
   * is called whenever the AppState signals an update.
   *
   * @see AppState#getStudentsByProgress(Student.Progress)
   * @see Student.Progress#REQUESTED
   */
  private void updateStudentList() {
    studentListView.setItems(AppState.getStudentsByProgress(Student.Progress.REQUESTED));
  }

  /**
   * Handles the accept button action. This method updates the selected student's progress to {@code
   * IN_PROGRESS} and signals an update to the AppState. It is called when the accept button is
   * clicked.
   *
   * @see Student#setProgress(Student.Progress)
   * @see Student.Progress#IN_PROGRESS
   */
  private void handleAccept() {
    Student selectedStudent = studentListView.getSelectionModel().getSelectedItem();
    if (selectedStudent != null) {
      selectedStudent.setProgress(Student.Progress.IN_PROGRESS);
      AppState.signalUpdate();
      System.out.println("Student Accepted");
    }
  }

  /**
   * Handles the deny button action. This method removes the selected student from the list and
   * signals an update to the AppState. It is called when the deny button is clicked.
   *
   * @see AppState#removeStudent(Student)
   */
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
