module org.roxburylatin.advcompsci.quizapp {
  requires javafx.controls;
  requires javafx.fxml;
  requires org.jetbrains.annotations;
  requires transitive javafx.graphics;
  requires javafx.web;
  requires javafx.base;
  requires org.json;
//  requires com.opencsv;
  requires com.fasterxml.jackson.dataformat.csv;

  exports org.roxburylatin.advcompsci.quizapp.application.student;
  opens org.roxburylatin.advcompsci.quizapp.application to
      javafx.fxml;

  exports org.roxburylatin.advcompsci.quizapp.application;
  exports org.roxburylatin.advcompsci.quizapp.application.teacher;

  opens org.roxburylatin.advcompsci.quizapp.application.teacher to
      javafx.fxml;

  opens org.roxburylatin.advcompsci.quizapp.application.student to javafx.fxml;

  exports org.roxburylatin.advcompsci.quizapp.core;
}
