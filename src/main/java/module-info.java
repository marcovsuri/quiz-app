module org.roxburylatin.advcompsci.quizapp {
  requires javafx.controls;
  requires javafx.fxml;

  opens org.roxburylatin.advcompsci.quizapp to javafx.fxml;

  exports org.roxburylatin.advcompsci.quizapp;
}
