package org.roxburylatin.advcompsci.quizapp.application.student;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.roxburylatin.advcompsci.quizapp.core.Question;

public class QuizViewState {
    private static Question currentQuestion = null;
    private static final BooleanProperty needsUpdate = new SimpleBooleanProperty(false);

    public static BooleanProperty needsUpdateProperty() {
        return needsUpdate;
    }

    public static Question getCurrentQuestion() {
        return currentQuestion;
    }

    public static void updateCurrentQuestion() {
        currentQuestion = AppState.getQuiz().loadQuestion();

        needsUpdate.set(!needsUpdate.get());
    }

}
