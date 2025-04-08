package org.roxburylatin.advcompsci.quizapp.application.teacher;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;

public class RequestsTabController {
    @FXML
    private ListView<StudentState> studentListView;

    @FXML
    private Button acceptButton;

    @FXML
    private Button denyButton;

    private TeacherAppState appState;

    @FXML
    public void initialize() {
        // Initialize with empty list, will be set when appState is provided
        studentListView.setItems(FXCollections.observableArrayList());

        // Set up button actions
        acceptButton.setOnAction(event -> handleAccept());
        denyButton.setOnAction(event -> handleDeny());
    }

    public void setAppState(TeacherAppState appState) {
        this.appState = appState;
        updateStudentList();
    }

    private void updateStudentList() {
        if (appState != null) {
            FilteredList<StudentState> requestedStudents = new FilteredList<>(appState.students,
                    student -> student.getProgress() == StudentState.Progress.REQUESTED);
            studentListView.setItems(requestedStudents);
        }
    }

    private void handleAccept() {
        StudentState selectedStudent = studentListView.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            selectedStudent.setProgress(StudentState.Progress.IN_PROGRESS);
            updateStudentList();
        }
    }

    private void handleDeny() {
        StudentState selectedStudent = studentListView.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            // Remove the student from the list
            appState.students.remove(selectedStudent);
            updateStudentList();
        }
    }
}
