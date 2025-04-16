module org.roxburylatin.advcompsci.quizapp {
  requires javafx.controls;
  requires javafx.fxml;
  requires org.jetbrains.annotations;
  requires transitive javafx.graphics;
  requires javafx.web;
  requires javafx.base;
  requires org.json;
  requires com.fasterxml.jackson.dataformat.csv;
  requires com.fasterxml.jackson.databind;

  exports org.roxburylatin.advcompsci.quizapp.core;
  
  exports org.roxburylatin.advcompsci.quizapp.application.student;
  opens org.roxburylatin.advcompsci.quizapp.application.student to javafx.fxml;

  exports org.roxburylatin.advcompsci.quizapp.application.teacher;
  opens org.roxburylatin.advcompsci.quizapp.application.teacher to
      javafx.fxml;
}
