module org.roxburylatin.advcompsci.quizapp {
  requires javafx.controls;
  requires javafx.fxml;
  requires org.jetbrains.annotations;
  requires transitive javafx.graphics;
  requires javafx.base;

  exports org.roxburylatin.advcompsci.quizapp.application.student;

  opens org.roxburylatin.advcompsci.quizapp.application.student to javafx.fxml;

  exports org.roxburylatin.advcompsci.quizapp.core;
}
