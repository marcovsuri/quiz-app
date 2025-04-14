package org.roxburylatin.advcompsci.quizapp.application.student;

import javafx.beans.property.BooleanProperty;
import org.roxburylatin.advcompsci.quizapp.core.*;
import javafx.beans.property.SimpleBooleanProperty;

public class StudentAppState {
    private static Quiz quiz;
    private static final BooleanProperty needsUpdate = new SimpleBooleanProperty(false);

    public static BooleanProperty needsUpdateProperty() {
        return needsUpdate;
    }

    public static void signalUpdate() {
        needsUpdate.set(!needsUpdate.get());
        System.out.println("Updated UI");
    }

    public static Question getCurrentQuestion() {
        if (quiz == null)
            return null;

        return quiz.loadQuestion();
    }

    public static void setQuiz(Quiz quiz) {
        StudentAppState.quiz = quiz;
    }

    public static void submitAnswer(Question.Choice answer) {
        if (quiz == null)
            return;

        quiz.submitAnswer(answer);
        signalUpdate();
    }
}
