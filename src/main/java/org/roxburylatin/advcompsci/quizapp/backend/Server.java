package org.roxburylatin.advcompsci.quizapp.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * A generic server class that handles requests of type T, where T is an enum. This server supports
 * multiple concurrent client connections and provides a framework for handling different types of
 * requests through registered handlers.
 *
 * @param <T> The enum type representing different request types the server can handle
 */
public class Server<T extends Enum<T>> {
  private final @NotNull HashMap<T, RequestHandler> requestHandlerMap = new HashMap<>();
  private final @NotNull HashMap<T, Boolean> requestIOMap = new HashMap<>();
  private final @NotNull Lock runningLock;
  private final @NotNull Lock fileLock;
  private final @NotNull Class<T> requestClass;
  private int port;
  private boolean running;
  private ServerSocket serverSocket;

  /**
   * Constructs a new Server instance with the specified port and request class.
   *
   * @param port The port number on which the server will listen for client connections
   * @param requestClass The class of the enum type representing different request types
   */
  public Server(int port, @NotNull Class<T> requestClass, @NotNull Lock IOLock) {
    this.port = port;
    running = false;
    runningLock = new ReentrantLock();
    fileLock = IOLock;
    this.requestClass = requestClass;
  }

  /**
   * Set the port on which to run the server
   *
   * @param port new port number
   */
  public void setPort(int port) {
    this.port = port;
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
   * Registers a handler for a specific request type.
   *
   * @param type The enum value representing the request type
   * @param handler The handler to be used for the request type
   * @param io Whether the request handler requires I/O operations
   */
  public void registerHandler(T type, RequestHandler handler, boolean io) {
    requestHandlerMap.put(type, handler);
    requestIOMap.put(type, io);
  }

  /** Starts the server. */
  public void start() {
    // If already running, exit early after unlocking
    if (isRunning()) return;

    // Update the running state if not already running
    setRunning(true);

    // Prevent race condition errors
    int port = this.port;

    Thread serverThread =
        new Thread(
            () -> {
              try (ServerSocket serverSocket = new ServerSocket()) {
                serverSocket.bind(
                    new InetSocketAddress("0.0.0.0", port)); // Allow external connections
                System.out.println("Teacher server started on port " + port);

                this.serverSocket = serverSocket;

                // Accept client connections
                while (isRunning()) {
                  try (Socket clientSocket = serverSocket.accept();
                      BufferedReader in =
                          new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    // Handle the client request based on the request type
                    try {
                      String clientIp = clientSocket.getInetAddress().getHostAddress();
                      System.out.println("Incoming connection from: " + clientIp);

                      // Accept only local (private) IP ranges
                      if (!(clientIp.startsWith("192.168.")
                          || clientIp.startsWith("10.")
                          || clientIp.startsWith("172."))) {
                        System.out.println(
                            "Rejected connection from non-local network: " + clientIp);
                        clientSocket.close();
                        continue; // back to waiting
                      }

                      // Ensure request has lines
                      String line1 = in.readLine();
                      String line2 = in.readLine();

                      if (line1 == null || line2 == null) {
                        throw new ServerException("The input must contain at least two lines.");
                      }

                      // Parse the client request
                      T requestType = Enum.valueOf(requestClass, line1);
                      JSONObject json = new JSONObject(line2);

                      // Check if the request type requires I/O operations
                      boolean usesIO = requestIOMap.get(requestType);

                      // If the request type requires I/O operations, lock the file lock
                      if (usesIO) {
                        fileLock.lock();
                      }

                      // Handle the client request
                      String response = requestHandlerMap.get(requestType).handle(json);

                      // If the request type requires I/O operations, unlock the file lock
                      if (usesIO) {
                        fileLock.unlock();
                      }

                      // Send the response to the client
                      out.println("SUCCESS");

                      String[] lines = response.split("\n");
                      for (String line : lines) {
                        out.println(line);
                      }
                    } catch (ServerException e) {
                      // Send an error response to the client
                      out.println("ERROR");
                      out.println(e.getMessage());
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
