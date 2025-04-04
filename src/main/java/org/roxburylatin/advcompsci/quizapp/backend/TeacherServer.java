package org.roxburylatin.advcompsci.quizapp.backend;

import com.opencsv.CSVWriter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * A server that listens for client connections and handles quiz data requests. The server manages
 * concurrent access to shared resources using locks and handles client connections for quiz data.
 */
public class TeacherServer {
  private final int port;
  private final @NotNull Lock runningLock;
  private final @NotNull Lock fileLock;
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
  private @NotNull String csvToString(@NotNull String filePath) {
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

  /**
   * Updates the student results for a specific chapter.
   *
   * @param studentFirstName the first name of the student
   * @param studentLastName the last name of the student
   * @param chapterNum the chapter number
   * @param numCorrect the number of correct answers
   */
  private void updateStudentResults(
      @NotNull String studentFirstName,
      @NotNull String studentLastName,
      int chapterNum,
      int numCorrect) {
    // Locks file writing
    fileLock.lock();

    String filePath =
        "student_results/" + studentFirstName + "_" + studentLastName + "_results.csv";

    // Headers
    String[] headers = {"first_name", "last_name", "chapter_num", "num_correct", "score"};

    // New row
    String[] newRow = {
      studentFirstName,
      studentLastName,
      String.valueOf(chapterNum),
      String.valueOf(numCorrect),
      String.valueOf((double) numCorrect / 30.0)
    };

    File file = new File(filePath);
    boolean fileExists = file.exists();

    // If file doesn't exist, create it
    if (!fileExists) {
      // Todo - make try catches nicer
      try {
        file.createNewFile();
      } catch (IOException e) {
        // TODO - exception handling
        e.printStackTrace();
      }
    }

    // Write to file
    try (CSVWriter writer = new CSVWriter(new FileWriter(file, true))) {
      if (!fileExists) {
        writer.writeNext(headers);
      }

      writer.writeNext(newRow);
    } catch (IOException e) {
      // TODO - handle exception
      e.printStackTrace();
    } finally {
      // Unlock file writing
      fileLock.unlock();
    }
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
                      String clientFirstName = in.readLine();
                      String clientLastName = in.readLine();
                      RequestType requestType = RequestType.valueOf(in.readLine());
                      String jsonString = in.readLine();
                      JSONObject json = new JSONObject(jsonString);

                      // Handle the client request based on the request type
                      if (requestType == RequestType.GET_QUIZ_DATA) {
                        // Get the quiz data initially => return the csv contents based on the
                        // chapter number
                        if (!json.has("chapterNum"))
                          throw new IllegalArgumentException("Chapter number missing in data");

                        int chapterNum = json.getInt("chapterNum");
                        // TODO - change csv file location
                        String csvContents =
                            csvToString("chapter_" + chapterNum + "_questions.csv");

                        out.print("SUCCESS");
                        out.println(csvContents);
                      } else if (requestType == RequestType.SUBMIT_QUIZ) {
                        // Check keys
                        if (!json.has("chapterNum"))
                          throw new IllegalArgumentException("No chapter number provided");
                        if (!json.has("numCorrect"))
                          throw new IllegalArgumentException(
                              "Number of correct answers missing in data");

                        // Parse data
                        int chapterNum = json.getInt("chapterNum");
                        int numCorrect = json.getInt("numCorrect");

                        // Update CSV results
                        updateStudentResults(
                            clientFirstName, clientLastName, chapterNum, numCorrect);
                        out.println("SUCCESS");
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
}
