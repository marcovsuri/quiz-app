package org.roxburylatin.advcompsci.quizapp.backend;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.json.JSONObject;

/**
 * A server that listens for client connections and handles quiz data requests. The server manages
 * concurrent access to shared resources using locks and handles client connections for quiz data.
 */
public class TeacherServer {
  private final int port;
  private final Lock runningLock;
  private final Lock fileLock;
  private boolean running;
  private ServerSocket serverSocket;

  /**
   * Creates a new TeacherServer that listens on the specified port.
   *
   * @param port the port number to listen on
   */
  public TeacherServer(int port) {
    this.port = port;
    running = false;
    runningLock = new ReentrantLock();
    fileLock = new ReentrantLock();
  }

  /**
   * Checks if the server is running.
   *
   * @return true if the server is running, false otherwise
   */
  public boolean isRunning() {
    // Lock the running state to prevent race conditions
    runningLock.lock();
    try {
      return running;
    } finally {
      // Unlock the running state
      runningLock.unlock();
    }
  }

  /**
   * Sets the running state of the server.
   *
   * @param value the new running state
   */
  private void setRunning(boolean value) {
    // Lock the running state to prevent race conditions
    runningLock.lock();
    try {
      running = value;
    } finally {
      // Unlock the running state
      runningLock.unlock();
    }
  }

  /**
   * Converts a CSV file to a string.
   *
   * @param filePath the path to the CSV file
   * @return the contents of the CSV file as a string
   */
  private String csvToString(String filePath) {
    fileLock.lock();
    StringBuilder csvContent = new StringBuilder();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        csvContent.append(line).append('\n');
      }
    } catch (IOException e) {
      // Todo - exception handling
      e.printStackTrace();
    }

    fileLock.unlock();

    return csvContent.toString();
  }

  /** Starts the server. */
  public void start() {
    // If already running, exit early after unlocking
    if (isRunning()) return;

    // Update the running state if not already running
    setRunning(true);

    // Create a new thread for the server socket
    Thread serverThread =
        new Thread(
            () -> {
              try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Teacher server started on port " + port);

                this.serverSocket = serverSocket;

                // Accept client connections
                while (isRunning()) {
                  try (Socket clientSocket = serverSocket.accept();
                      BufferedReader in =
                          new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    try {
                      // Parse the client request
                      String clientName = in.readLine();
                      RequestType requestType = RequestType.valueOf(in.readLine());
                      String jsonString = in.readLine();
                      JSONObject json = new JSONObject(jsonString);

                      // Handle the client request based on the request type
                      if (requestType == RequestType.GET_QUIZ_DATA) {
                        // Get the quiz data initially => return the csv contents based on the
                        // chapter
                        // number
                        if (!json.has("chapterNum"))
                          throw new IllegalArgumentException("Chapter number missing in data");

                        int chapterNum = json.getInt("chapterNum");
                        // TODO - change csv file location
                        String csvContents =
                            csvToString("chapter_" + chapterNum + "_questions.csv");

                        out.print("SUCCESS");
                        out.println(csvContents);
                      } else {
                        // Invalid request type
                        throw new IllegalArgumentException("Invalid request type");
                      }
                    } catch (IllegalArgumentException e) {
                      // Handle invalid request
                      out.println("ERROR: " + e.getMessage());
                    }
                  } catch (IOException e) {
                    // TODO - Error handling
                    if (isRunning()) {
                      System.err.println("Error accepting client connection: " + e.getMessage());
                    }
                  }
                }
              } catch (IOException e) {
                // TODO - Error handling
                System.err.println("Error starting server: " + e.getMessage());
                setRunning(false);
              }
            });

    // Start the server thread
    serverThread.start();
  }

  /** Stops the server. */
  public void stop() {
    if (isRunning()) {
      // Close the server socket
      try {
        serverSocket.close();
      } catch (IOException e) {
        System.err.println("Error closing server socket: " + e.getMessage());
      }
    }

    setRunning(false);
  }

  public enum RequestType {
    GET_QUIZ_DATA
  }
}
