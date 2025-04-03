module org.roxburylatin.advcompsci.quizapp {
  requires javafx.controls;
  requires javafx.fxml;
  requires org.jetbrains.annotations;
  requires org.json;
  requires com.opencsv;

  opens org.roxburylatin.advcompsci.quizapp.application to javafx.fxml;
  exports org.roxburylatin.advcompsci.quizapp.application;

//  exports org.roxburylatin.advcompsci.quizapp;
}
