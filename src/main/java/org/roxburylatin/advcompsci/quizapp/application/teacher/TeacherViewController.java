package org.roxburylatin.advcompsci.quizapp.application.teacher;

import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * The TeacherViewController class manages the UI for the teacher's view in the quiz application. It
 * handles the display of student progress across different tabs (requests, in-progress, and
 * completed) and manages the server start/stop functionality.
 */
public class TeacherViewController {
  @FXML private Button startServerButton;
  @FXML private Text serverStatusText;
  @FXML private VBox requestsTab;
  @FXML private VBox inProgressTab;
  @FXML private VBox completedTab;
  @FXML private TextField portField;
  @FXML private Text ipDisplay;

  /**
   * Retrieves the local IP address of the machine. This method iterates through the network
   * interfaces and returns the first non-loopback IPv4 address found.
   *
   * @return the local IP address as a string, or "Unknown" if no suitable address is found
   */
  private static String getLocalIpAddress() {
    try {
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      for (NetworkInterface ni : Collections.list(interfaces)) {
        if (ni.isLoopback() || !ni.isUp()) continue;

        for (InetAddress addr : Collections.list(ni.getInetAddresses())) {
          if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
            return addr.getHostAddress();
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "Unknown";
  }

  /**
   * Initializes the TeacherViewController. This method sets up the UI components, loads the FXML
   * files for each tab, and configures the server button action. It also displays the local IP
   * address.
   */
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

      // Show IP address
      ipDisplay.setText(getLocalIpAddress());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Checks if a specific port is in use by attempting to create a socket connection to it. If the
   * connection is successful, the port is considered in use. If an exception is thrown, the port is
   * considered free.
   *
   * @param port the port number to check
   * @return true if the port is in use, false otherwise
   */
  private boolean isPortInUse(int port) {
    try (Socket socket = new Socket("localhost", port)) {
      return true; // Port is in use
    } catch (IOException e) {
      return false; // Port is free
    }
  }

  /**
   * Handles the server button action. This method starts or stops the server based on its current
   * state. It also validates the port input and updates the UI accordingly.
   */
  private void handleServerButton() {
    boolean isServerRunning = AppState.server.isRunning();

    if (!isServerRunning) {
      int port;
      try {
        String portInput = portField.getText();

        // Step 1: Check if input is empty
        if (portInput.isEmpty()) {
          throw new IllegalArgumentException(
              "Please fill out the port input before starting the server.");
        }

        // Step 2: Parse the port number
        port = Integer.parseInt(portInput);

        // Step 3: Validate port range (1024-65535)
        if (port < 1024 || port > 65535) {
          throw new IllegalArgumentException("Please enter a port number between 1024 and 65535.");
        }

        // Step 4: Check if port is in use by trying to connect (doesn't bind it)
        if (isPortInUse(port)) {
          throw new IllegalArgumentException(
              "This port is already in use. Please choose a different one.");
        }

        // Step 5: Try binding to the port to check if it's available
        try (ServerSocket serverSocket = new ServerSocket(port)) {
          serverSocket.setReuseAddress(true); // Optional, allows immediate re-use after close
        } catch (IOException e) {
          throw new IllegalArgumentException(
              "Error starting server on port " + port + ": " + e.getMessage());
        }

      } catch (NumberFormatException e) {
        // Handle invalid number format
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Starting Server");
        alert.setHeaderText("Please enter a valid numeric port number.");
        alert.showAndWait();
        return;
      } catch (IllegalArgumentException e) {
        // Handle all validation errors
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Starting Server");
        alert.setHeaderText(e.getMessage());
        alert.showAndWait();
        return;
      }
      // Start Server
      AppState.server.setPort(port);
      AppState.server.start();
    } else {
      // Stop Server
      AppState.server.stop();
    }

    // Update UI
    serverStatusText.setText(isServerRunning ? "Server Inactive" : "Server Active");
    startServerButton.setText(isServerRunning ? "Start Server" : "Stop Server");
    portField.setDisable(!isServerRunning);
  }
}
