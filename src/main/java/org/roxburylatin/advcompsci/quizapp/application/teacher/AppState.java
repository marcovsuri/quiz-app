package org.roxburylatin.advcompsci.quizapp.application.teacher;

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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AppState {
  private static final ObservableList<Student> allStudents = FXCollections.observableArrayList();
  private static final BooleanProperty needsUpdate = new SimpleBooleanProperty(false);
  static Server<Request> server;
  static final Lock IOLock = new ReentrantLock();

  static {
    // TODO - remove (TESTING ONLY)
    Student student1 = new Student("Michael", "DiLallo");
    student1.setProgress(Progress.COMPLETED);
    allStudents.add(student1);

    Student student2 = new Student("Avish", "Kumar");
    student2.setProgress(Progress.IN_PROGRESS);
    allStudents.add(student2);

    Student student3 = new Student("Marco", "Suri");
    student3.setProgress(Progress.REQUESTED);
    allStudents.add(student3);
  }

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

          // Add student
          // Ensure UI changes happen on the right thread
          Platform.runLater(
              () -> {
                Student student = new Student(firstName, lastName);
                student.setProgress(Progress.IN_PROGRESS);
                addStudent(student);
                System.out.println("ADDED STUDENT");
              });

          int chapterNum = data.getInt("chapterNum");

          StringBuilder csvContentBuilder = new StringBuilder();

          // TODO - change csv location
          try (BufferedReader reader =
              new BufferedReader(new FileReader("chapter_" + chapterNum + "_questions.csv"))) {
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

    server.registerHandler(Request.ASK_FOR_HELP, (JSONObject data) -> {
      if (!data.has("firstName") || !data.has("lastName")) throw new ServerException("Incomplete parameters");

      String firstName = data.getString("firstName");
      String lastName = data.getString("lastName");

      Platform.runLater(() -> {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Student Requested Help (Quiz App)");
        alert.setHeaderText(firstName + " " + lastName + " has requested help!");
        alert.showAndWait();
      });

      return "";
    }, false);
  }

  public static ObservableList<Student> getStudentsByProgress(Student.Progress progress) {
    FilteredList<Student> filteredList =
        new FilteredList<>(allStudents, student -> student.getProgress() == progress);
    return filteredList;
  }

  public static BooleanProperty needsUpdateProperty() {
    return needsUpdate;
  }

  public static void signalUpdate() {
    needsUpdate.set(!needsUpdate.get());
    System.out.println("Updated UI");
  }

  public static void addStudent(Student student) {
    allStudents.add(student);
  }

  public static void removeStudent(Student student) {
    allStudents.remove(student);
  }

  public static void clearCompletedStudents() {
    // Remove all students with COMPLETED progress
    allStudents.removeIf(student -> student.getProgress() == Progress.COMPLETED);
  }
}
