package org.roxburylatin.advcompsci.quizapp.application.teacher;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TeacherController {
  @FXML
  private TabPane tabPane;
  @FXML
  private Button startServerButton;
  @FXML
  private Text serverStatusText;
  @FXML
  private VBox requestsTab;
  @FXML
  private VBox inProgressTab;
  @FXML
  private VBox completedTab;

  @FXML
  public void initialize() {
    try {
      // Load the FXML files
      FXMLLoader requestsLoader = new FXMLLoader(getClass().getResource("requests-tab.fxml"));
      FXMLLoader inProgressLoader = new FXMLLoader(getClass().getResource("in-progress-tab.fxml"));
      FXMLLoader completedLoader = new FXMLLoader(getClass().getResource("completed-tab.fxml"));

      // Load the content
      VBox requestsContent = requestsLoader.load();
      VBox inProgressContent = inProgressLoader.load();
      VBox completedContent = completedLoader.load();

      // Set the content to the tabs
      requestsTab.getChildren().addAll(requestsContent.getChildren());
      inProgressTab.getChildren().addAll(inProgressContent.getChildren());
      completedTab.getChildren().addAll(completedContent.getChildren());

      // Set up server button action
      startServerButton.setOnAction(event -> handleServerButton());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void handleServerButton() {
    // TODO: Implement server start/stop functionality
    boolean isServerRunning = serverStatusText.getText().equals("Server Active");
    serverStatusText.setText(isServerRunning ? "Server Inactive" : "Server Active");
    startServerButton.setText(isServerRunning ? "Start Server" : "Stop Server");
  }
}
