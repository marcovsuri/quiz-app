package org.roxburylatin.advcompsci.quizapp.application.student;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.roxburylatin.advcompsci.quizapp.application.Request;
import org.roxburylatin.advcompsci.quizapp.backend.Client;
import org.roxburylatin.advcompsci.quizapp.core.*;

/**
 * The AppState class serves as a centralized state manager for the quiz application. It maintains
 * the state of the application, including user details, quiz progress, and UI update signals. This
 * class provides static methods and properties to interact with and modify the application state.
 */
public class AppState {
  private static final BooleanProperty needsUpdate = new SimpleBooleanProperty(false);
  static Client<Request> client;
  static String firstName = null;
  static String lastName = null;
  static Integer chapterNum = null;
  static boolean quizSubmitted = false;
  private static Quiz quiz;

  /**
   * Gets the needs update property which indicates whether the UI needs to be updated. Should be
   * listened to by the UI for updates.
   *
   * @return the BooleanProperty indicating the update status.
   */
  static BooleanProperty needsUpdateProperty() {
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
   * Gets the current question from the quiz. If no quiz is set, or if the quiz doesn't have any
   * more questions, returns null.
   *
   * @return the current Question object or null if no quiz is set.
   */
  static Question getCurrentQuestion() {
    if (quiz == null) return null;

    return quiz.loadQuestion();
  }

  /**
   * Submits the user's answer to the current question. If no quiz is set, this method does nothing.
   *
   * @param answer the user's answer to the current question.
   */
  static void submitAnswer(Question.Choice answer) {
    if (quiz == null) return;

    quiz.submitAnswer(answer);
    signalUpdate();
  }

  static Quiz getQuiz() {
    return quiz;
  }

  static void setQuiz(Quiz quiz) {
    AppState.quiz = quiz;
  }
}
