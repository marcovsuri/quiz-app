package org.roxburylatin.advcompsci.quizapp.application.student;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.roxburylatin.advcompsci.quizapp.application.Request;
import org.roxburylatin.advcompsci.quizapp.backend.Client;
import org.roxburylatin.advcompsci.quizapp.core.*;

public class StudentAppState {
    private static final BooleanProperty needsUpdate = new SimpleBooleanProperty(false);
    static Client<Request> client;
    static String firstName = null;
    static String lastName = null;
    static Integer chapterNum = null;
    static boolean quizSubmitted = false;
    private static Quiz quiz;

    static BooleanProperty needsUpdateProperty() {
        return needsUpdate;
    }

    public static void signalUpdate() {
        needsUpdate.set(!needsUpdate.get());
        System.out.println("Updated UI");
    }

     static Question getCurrentQuestion() {
        if (quiz == null)
            return null;

        return quiz.loadQuestion();
    }

    static void submitAnswer(Question.Choice answer) {
        if (quiz == null)
            return;

        quiz.submitAnswer(answer);
        signalUpdate();
    }

    static Quiz getQuiz() {
        return quiz;
    }

    static void setQuiz(Quiz quiz) {
        StudentAppState.quiz = quiz;
    }

}
