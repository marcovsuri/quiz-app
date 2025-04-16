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

public class AppState {
  static final Lock IOLock = new ReentrantLock();
  private static final ObservableList<Student> allStudents = FXCollections.observableArrayList();
  private static final BooleanProperty needsUpdate = new SimpleBooleanProperty(false);
  static Server<Request> server;

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

          String filePath = firstName + "_" + lastName + "_quiz_results.csv";

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

  public static void addStudent(Student student) throws IllegalArgumentException {
    // TODO - implement
    //    if (getStudent(student.getFirstName(), student.getLastName()) != null) {
    //      throw new IllegalArgumentException(
    //          "A student with the same first name and last name already exists.");
    //    }

    allStudents.add(student);
  }

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

  public static void removeStudent(Student student) {
    allStudents.remove(student);
  }

  public static void clearCompletedStudents() {
    // Remove all students with COMPLETED progress
    allStudents.removeIf(student -> student.getProgress() == Progress.COMPLETED);
  }
}
