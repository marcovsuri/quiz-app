package org.roxburylatin.advcompsci.quizapp.application.teacher;

import java.io.*;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
import org.json.JSONObject;
import org.roxburylatin.advcompsci.quizapp.application.Request;
import org.roxburylatin.advcompsci.quizapp.application.teacher.Student.Progress;
import org.roxburylatin.advcompsci.quizapp.backend.Server;
import org.roxburylatin.advcompsci.quizapp.backend.ServerException;

/**
 * The AppState class serves as a centralized state manager for the teacher view of the quiz
 * application. It maintains the state of the application, including the list of students, their
 * progress, and server interactions. This class provides static methods and properties to interact
 * with and modify the application state.
 */
public class AppState {
  static final Lock IOLock = new ReentrantLock();
  private static final ObservableList<Student> allStudents = FXCollections.observableArrayList();
  private static final BooleanProperty needsUpdate = new SimpleBooleanProperty(false);
  static Server<Request> server;

  static {
    // Server Initiation
    server = new Server<Request>(3000, Request.class, IOLock);
    server.registerHandler(
        Request.REQUEST_QUIZ,
        (JSONObject data) -> {
          if (!data.has("firstName") || !data.has("lastName") || !data.has("chapterNum"))
            throw new ServerException("Incomplete parameters");

          String firstName = data.getString("firstName");
          String lastName = data.getString("lastName");
          int chapterNum = data.getInt("chapterNum");

          // Add student
          // Ensure UI changes happen on the right thread
          Platform.runLater(
              () -> {
                Student student = new Student(firstName, lastName, chapterNum);
                student.setProgress(Progress.IN_PROGRESS);
                try {
                  addStudent(student);
                  System.out.println("ADDED STUDENT");
                } catch (IllegalArgumentException e) {
                  Alert alert = new Alert(Alert.AlertType.WARNING);

                  alert.setTitle("Duplicate Student");
                  alert.setHeaderText(
                      "A student with the name "
                          + firstName
                          + " "
                          + lastName
                          + " has registered for his quiz there is another student with the same name. The new student has been given the quiz.");
                  alert.showAndWait();
                }
              });

          StringBuilder csvContentBuilder = new StringBuilder();

          // Get file from ~/quiz-app/chapter-questions/chapter_(chapter #)_questions.csv
          String filePath =
              System.getProperty("user.home")
                  + "/quiz-app/chapter-questions/chapter_"
                  + chapterNum
                  + "_questions.csv";

          try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
              csvContentBuilder.append(line).append('\n');
            }
          } catch (IOException e) {
            throw new ServerException("Unable to get quiz data");
          }

          return csvContentBuilder.toString();
        },
        true);

    server.registerHandler(
        Request.ASK_FOR_HELP,
        (JSONObject data) -> {
          if (!data.has("firstName") || !data.has("lastName"))
            throw new ServerException("Incomplete parameters");

          String firstName = data.getString("firstName");
          String lastName = data.getString("lastName");

          Platform.runLater(
              () -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("Student Requested Help (Quiz App)");
                alert.setHeaderText(firstName + " " + lastName + " has requested help!");
                alert.showAndWait();
              });

          return "";
        },
        false);

    server.registerHandler(
        Request.SUBMIT_QUIZ,
        (JSONObject data) -> {
          if (!data.has("firstName")
              || !data.has("lastName")
              || !data.has("chapterNum")
              || !data.has("numQuestionsCorrect")
              || !data.has("numQuestionsTotal")) throw new ServerException("Incomplete parameters");

          String firstName = data.getString("firstName");
          String lastName = data.getString("lastName");
          int chapterNum = data.getInt("chapterNum");
          int numQuestionsCorrect = data.getInt("numQuestionsCorrect");
          int numQuestionsTotal = data.getInt("numQuestionsTotal");

          /* Write file to ~/quiz-app/student-results/(firstname, lowercase)_(lastname, lowercase)_quiz_results.csv */
          String filePath =
              System.getProperty("user.home")
                  + "/quiz-app/student-results/"
                  + firstName.toLowerCase()
                  + "_"
                  + lastName.toLowerCase()
                  + "_quiz_results.csv";

          boolean writeHeaders = !(new File(filePath)).exists();

          try (FileWriter writer = new FileWriter(filePath, true)) {
            if (writeHeaders) {
              writer.write("chapter_num,score");
              writer.write('\n');
            }
            writer.write(
                chapterNum + "," + ((double) numQuestionsCorrect / (double) numQuestionsTotal));
            writer.write('\n');
          } catch (IOException e) {
            throw new ServerException("Cannot write data");
          }

          // Move student to completed
          // Ensure UI changes happen on the right thread
          Platform.runLater(
              () -> {
                Student student = getStudent(firstName, lastName);
                if (student == null) {
                  Alert alert = new Alert(Alert.AlertType.WARNING);

                  alert.setTitle("Missing Student");
                  alert.setHeaderText(
                      "A student with the name "
                          + firstName
                          + " "
                          + lastName
                          + " has submitted his quiz but is unrecognized. His quiz progress is saved.");
                  alert.showAndWait();
                  return;
                }

                // Change student's status
                student.setProgress(Progress.COMPLETED);

                // Signal update after changing student's status
                signalUpdate();

                System.out.println("STUDENT FINISHED");
              });

          return "";
        },
        true);
  }

  /**
   * Gets the list of all students in the application.
   *
   * @param progress the progress status of the students to filter by.
   * @return an ObservableList containing all students.
   */
  public static ObservableList<Student> getStudentsByProgress(Student.Progress progress) {
    FilteredList<Student> filteredList =
        new FilteredList<>(allStudents, student -> student.getProgress() == progress);
    return filteredList;
  }

  /**
   * Gets the needs update property which indicates whether the UI needs to be updated. Should be
   * listened to by the UI for updates.
   *
   * @return the BooleanProperty indicating the update status.
   */
  public static BooleanProperty needsUpdateProperty() {
    return needsUpdate;
  }

  /**
   * Signals that the UI needs to be updated. This method toggles the needsUpdate property to notify
   * the UI of changes.
   */
  public static void signalUpdate() {
    needsUpdate.set(!needsUpdate.get());
    System.out.println("Updated UI");
  }

  /**
   * Adds a student to the list of all students.
   *
   * @param student the student to be added.
   */
  public static void addStudent(Student student) {
    allStudents.add(student);
  }

  /**
   * Gets a student by their first and last name.
   *
   * @param firstName the first name of the student.
   * @param lastName the last name of the student.
   * @return the student with the specified first and last name, or null if not found.
   */
  public static Student getStudent(String firstName, String lastName) {
    Student s =
        allStudents.stream()
            .filter(
                student ->
                    Objects.equals(student.getFirstName(), firstName)
                        && Objects.equals(student.getLastName(), lastName))
            .findFirst()
            .orElse(null);

    return s;
  }

  /**
   * Removes a student from the list of all students.
   *
   * @param student the student to be removed.
   */
  public static void removeStudent(Student student) {
    allStudents.remove(student);
  }

  /** Clears all students with {@code COMPLETED} progress from the list of all students. */
  public static void clearCompletedStudents() {
    // Remove all students with COMPLETED progress
    allStudents.removeIf(student -> student.getProgress() == Progress.COMPLETED);
  }
}
